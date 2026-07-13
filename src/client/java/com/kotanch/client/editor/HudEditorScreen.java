package com.kotanch.client.editor;

import com.kotanch.client.config.ConfigManager;
import com.kotanch.client.config.WidgetConfig;
import com.kotanch.client.render.Anchor;
import com.kotanch.client.render.HudRenderer;
import com.kotanch.client.widget.HudWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

public class HudEditorScreen extends Screen {

    private static final int GRID = 10;

    private List<HudWidget> widgets;
    private int selected = -1;
    private boolean dragging = false;
    private  int grabDx, grabDy;

    public HudEditorScreen() {
        super(Text.literal("HudForge Editor"));
    }

    @Override
    protected void init() {
        widgets = HudRenderer.buildWidgets();
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

        MinecraftClient mc = MinecraftClient.getInstance();
        int[] p = HudRenderer.resolvePos(widgets.get(hit),mc, this.width, this.height);
        grabDx = (int) mx - p[0];
        grabDy = (int) my - p[1];
        return true;
    }

    @Override
    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        if (dragging && selected >= 0) {
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
        if (dragging && selected >= 0){
            MinecraftClient mc = MinecraftClient.getInstance();
            int[] p  = HudRenderer.resolvePos(widgets.get(selected), mc, this.width, this.height);
            placeAbsolute(widgets.get(selected), p[0], p[1], true);
            ConfigManager.save();
            dragging = false;
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
}
