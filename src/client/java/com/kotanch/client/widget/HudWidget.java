package com.kotanch.client.widget;

import com.kotanch.client.config.WidgetConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;

public abstract class HudWidget {
    protected  static final int PADDING =3;
    protected static final int  LINE_HEIGHT = 10;
    protected static final int GLYPH_HEIGHT = 8;

    public abstract  String displayName();

    protected  abstract List<Text> lines(MinecraftClient mc);

    public int getWidth(MinecraftClient mc) {
        TextRenderer tr = mc.textRenderer;
        int max = 0;
        for (Text line : lines(mc)){
            max = Math.max(max, tr.getWidth(line));
        }
        return max + PADDING * 2;
    }

    public int getHeight(MinecraftClient mc){
        int n = lines(mc).size();
        if (n == 0) return 0;
        return PADDING * 2 + (n-1) * LINE_HEIGHT + GLYPH_HEIGHT;
    }

    public void renderAt(DrawContext ctx, int x, int y, MinecraftClient mc){
        List<Text> l = lines(mc);
        if (l.isEmpty()) return;
        int w = getWidth(mc);
        int h = getHeight(mc);

        int bgAlpha = config != null ? config.backgroundOpacity : 140;
        int textAlpha = config != null ? config.backgroundOpacity : 255;

        ctx.fill(x,y,x + w, y+h, (bgAlpha << 24));

        int ty = y + PADDING;
        for (Text line : l){
            ctx.drawText(mc.textRenderer,line, x + PADDING,ty, (textAlpha << 24) | 0xFFFFFFFF, true);
            ty += LINE_HEIGHT;
        }
    }

    public abstract  String typeId();

    protected WidgetConfig config;

    public void attach(WidgetConfig config) {
        this.config = config;
    }
    public WidgetConfig config(){
        return config;
    }

}
