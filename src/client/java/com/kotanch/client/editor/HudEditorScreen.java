package com.kotanch.client.editor;

import com.kotanch.client.config.ConfigManager;
import com.kotanch.client.config.WidgetConfig;
import com.kotanch.client.render.Anchor;
import com.kotanch.client.render.HudRenderer;
import com.kotanch.client.widget.HudWidget;
import com.kotanch.client.widget.WidgetRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class HudEditorScreen extends Screen {

    private static final int GRID = 10;

    private static final int PANEL_W = 120;

    private static final int SLIDER_W = PANEL_W - 16;
    private static final int SLIDER_H = 8;

    private int draggingSlider = -1;

    private List<HudWidget> widgets;
    private int selected = -1;
    private boolean dragging = false;
    private boolean movedWhileDragging = false;
    private  int grabDx, grabDy;

    private boolean paletteOpen = false;

    private static final int ADD_BTN_X = 6;
    private static final int ADD_BTN_Y = 6;
    private static final int ADD_BTN_W = 44;
    private static final int ADD_BTN_H = 14;
    private static final int PALETTE_ROW_H = 14;


    public HudEditorScreen() {
        super(Text.literal("HudForge Editor"));
    }

    @Override
    protected void init() {
        rebuild();
    }


    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta){
        ctx.fill(0,0,this.width,this.height, 0x66000000);
        drawGrid(ctx);

        MinecraftClient mc = MinecraftClient.getInstance();

        for (int i = 0; i < widgets.size(); i++){
            HudWidget w = widgets.get(i);
            int[] p = HudRenderer.resolvePos(w, mc, this.width, this.height);
            int x = p[0], y = p[1];
            int ww = w.getWidth(mc);
            int wh = w.getHeight(mc);

            if (w.config().enabled) {
                w.renderAt(ctx, x, y, mc);
            }else{
                ctx.fill(x,y, x +ww, y + wh, 0x33FF5555);
                ctx.drawText(this.textRenderer,
                        Text.literal(w.displayName()+ " (off)"),
                        x + 3, y + 3,0xFFFF9999, true );
            }

            int color = w.config().enabled ? 0x66FFFFFF : 0x66FF5555;
            drawOutline(ctx,x,y,ww,wh,color);
        }

        drawPanel(ctx);
        drawPalette(ctx);

        ctx.drawText(
                this.textRenderer,
                Text.literal("LMB drag - RMB toggle - Shift = no grid - Esc"),
                6, this.height - 12, 0xFFFFFFFF, true
        );
    }
    private void drawGrid(DrawContext ctx) {
        int col = 0x11FFFFFF;
        for (int x = 0; x <= this.width; x += GRID){
            ctx.fill(x,0, x + 1, this.height, col);
        }
        for (int y =0; y <= this.height; y += GRID){
            ctx.fill(0,y, this.width, y + 1, col);
        }
    }
    private  void drawOutline(DrawContext ctx, int x, int y, int w, int h, int color){
        ctx.fill(x,y,x + w, y + 1, color);
        ctx.fill(x,y + h -1,x +w, y + h,color);
        ctx.fill(x,y,x + 1, y + h, color);
        ctx.fill(x + w -1, y, x + w, y + h, color);
    }

    private int hitTest(double mx, double my){
        MinecraftClient mc = MinecraftClient.getInstance();
        for (int i = widgets.size() -1; i >= 0; i--){
            HudWidget w = widgets.get(i);
            int[] p = HudRenderer.resolvePos(w,mc, this.width, this.height);
            int x = p[0];
            int y = p[1];
            int ww = w.getWidth(mc);
            int wh = w.getHeight(mc);
            if (mx >= x && mx <= x + ww && my >= y && my <= y + wh) return  i;
        }
        return  -1;
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled){
        double mx = click.x();
        double my = click.y();

        if (mx >= ADD_BTN_X && mx <= ADD_BTN_X + ADD_BTN_W && my >= ADD_BTN_Y && my <= ADD_BTN_Y + ADD_BTN_H){
            paletteOpen = !paletteOpen;
            return true;
        }
        if (selected >= 0 && !dragging && mx >= this.width - PANEL_W) {
            int[] box = enabledBoxBounds();
            if (mx >= box[0] && mx <= box[0] + 10 && my >= box[1] && my <= box[1] + 10) {
                HudWidget w = widgets.get(selected);
                w.config().enabled = !w.config().enabled;
                ConfigManager.save();
                return true;
            }
            if (handleSlider(0,mx,my)){
                draggingSlider = 0;
                return true;
            }
            if(handleSlider(1,mx,my)){
                draggingSlider = 1;
                return true;
            }
            return true;
        }
        int hit = hitTest(mx,my);
        if (hit < 0) {
            selected = -1;
            return super.mouseClicked(click,doubled);
        }

        if (click.buttonInfo().button() == 1){
            HudWidget w = widgets.get(hit);
            w.config().enabled = !w.config().enabled;
            ConfigManager.save();
            return true;
        }
        selected = hit;
        dragging = true;
        movedWhileDragging = false;

        MinecraftClient mc = MinecraftClient.getInstance();
        int[] p = HudRenderer.resolvePos(widgets.get(hit),mc, this.width, this.height);
        grabDx = (int) mx - p[0];
        grabDy = (int) my - p[1];
        return true;
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        if (draggingSlider >= 0 && selected >= 0){
            handleSlider(draggingSlider, click.x(), click.y());
            return true;
        }
        if (dragging && selected >= 0) {
            movedWhileDragging = true;
            int nx = (int) click.x() - grabDx;
            int ny = (int) click.y() - grabDy;
            if (!click.hasShift()) {
                nx = Math.round(nx / (float) GRID) * GRID;
                ny = Math.round(ny / (float) GRID) * GRID;
            }
            placeAbsolute(widgets.get(selected), nx, ny, false);
            return true;
        }
        return super.mouseDragged(click, offsetX, offsetY);
    }

    @Override
    public  boolean mouseReleased(Click click) {
        if (draggingSlider >= 0){
            draggingSlider = -1;
            ConfigManager.save();
            return true;
        }
        if (dragging && selected >= 0){
            MinecraftClient mc = MinecraftClient.getInstance();
            int[] p  = HudRenderer.resolvePos(widgets.get(selected), mc, this.width, this.height);
            placeAbsolute(widgets.get(selected), p[0], p[1], true);
            ConfigManager.save();
            dragging = false;

            if (movedWhileDragging){
                selected = -1;
            }
            return  true;
        }
        return super.mouseReleased(click);
    }

    private void placeAbsolute(HudWidget w, int ax, int ay, boolean reAnchor){
        MinecraftClient mc = MinecraftClient.getInstance();
        int ww = w.getWidth(mc), wh = w.getHeight(mc);
        WidgetConfig c = w.config();

        Anchor a = reAnchor ? Anchor.nearest(ax,ay, ww, wh, this.width, this.height) : c.anchor;
        c.anchor = a;
        c.offsetX = ax -a.originX(this.width, ww, 0);
        c.offsetY = ay - a.originY(this.height, wh, 0);
    }

    @Override
    public void close() {
        ConfigManager.save();
        super.close();
    }
    private void drawPanel(DrawContext ctx) {
        if (selected < 0 || dragging) return;

        HudWidget w = widgets.get(selected);

        int px = this.width - PANEL_W;
        int py = 0;
        int ph = this.height;

        ctx.fill(px, py, this.width, ph, 0xCC101010);
        ctx.fill(px, py, px + 1, ph, 0x66FFFFFF);

        ctx.drawText(this.textRenderer, Text.literal(w.displayName()),
                px + 8, 10, 0xFFFFFFFF, true);
        ctx.fill(px + 8, 22, this.width -8, 23, 0x44FFFFFF);

        int[] box = enabledBoxBounds();
        boolean on = w.config().enabled;

        ctx.fill(box[0],box[1], box[0] + 10, box[1] + 10,0xFF000000 );
        drawOutline(ctx, box[0], box[1], 10,10, 0xFFAAAAAA);
        if (on) {
            ctx.fill(box[0] + 2, box[1] + 2, box[0] + 8, box[1] + 8, 0xFF55FF55);
        }
        ctx.drawText(this.textRenderer, Text.literal("Enabled"), box[0] + 16, box[1] + 1,0xFFFFFFFF, true);

        drawSlider(ctx,0, "Background", w.config().backgroundOpacity);
        drawSlider(ctx,1, "Text", w.config().textOpacity);
    }

    private int[] enabledBoxBounds(){
        int px = this.width - PANEL_W;
        return new int[]{px + 8, 32};
    }

    private int[] sliderBounds(int index) {
        int px = this.width - PANEL_W;
        int x = px + 8;
        int y = 52 + index * 26;
        return  new int[]{x,y,SLIDER_W, SLIDER_H};
    }

    private  void drawSlider(DrawContext ctx, int index, String label, int value){
        int[] b = sliderBounds(index);
        int x = b[0], y = b[1], w = b[2], h = b[3];

        ctx.drawText(this.textRenderer, Text.literal(label + ": " + value), x,y -10,0xFFFFFFFF, true);

        ctx.fill(x,y, x + w, y + h,0xFF333333);

        int knobX = x + Math.round((value/255f) * (w - 4));
        ctx.fill(x, y, knobX, y + h, 0xFF5588FF);
        ctx.fill(knobX, y -1, knobX + 4, y + h + 1, 0xFFFFFFFF);
    }

    private boolean handleSlider(int index, double mx, double my) {
        int[] b = sliderBounds(index);
        int x = b[0], y = b[1], w = b[2], h = b[3];

        if (mx < x || mx > x + w || my < y - 2 || my > y + h + 2) return  false;

        float frac = (float) (mx-x) / (w -4);
        int value = Math.round(frac * 255f);
        value = Math.max(0, Math.min(255,value));

        WidgetConfig c = widgets.get(selected).config();
        if (index ==0) c.backgroundOpacity = value;
        else c.textOpacity = value;
        return  true;
    }
    private void rebuild(){
        widgets = HudRenderer.buildWidgets();
    }

    private void drawPalette(DrawContext ctx) {
        ctx.fill(ADD_BTN_X, ADD_BTN_Y, ADD_BTN_X + ADD_BTN_W, ADD_BTN_Y + ADD_BTN_H,0xCC202020);
        drawOutline(ctx, ADD_BTN_X, ADD_BTN_Y, ADD_BTN_W, ADD_BTN_H, 0x88FFFFFF);
        ctx.drawText(this.textRenderer, Text.literal( "+ Add"), ADD_BTN_X + 6, ADD_BTN_Y + 3,0xFFFFFFFF, true);

        if (!paletteOpen) return;

        List<String[]> types = WidgetRegistry.available();
        int lx = ADD_BTN_X;
        int ly = ADD_BTN_Y + ADD_BTN_H + 2;
        int lw = 110;

        ctx.fill(lx, ly, lx + lw, ly + types.size() * PALETTE_ROW_H, 0xEE181818);
        drawOutline(ctx,lx, ly,  lw,  types.size() * PALETTE_ROW_H, 0x88FFFFFF);

        for (int i = 0; i < types.size(); i ++){
            int ry = ly + i * PALETTE_ROW_H;
            ctx.drawText(this.textRenderer, Text.literal(types.get(i)[1]),lx + 5, ry + 3,0xFFDDDDDD, true);
        }

    }
}
