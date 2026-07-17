package com.kotanch.client.widget;

import com.kotanch.client.config.WidgetConfig;
import com.kotanch.client.element.HudLine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.List;

public abstract class HudWidget {
    protected  static final int PADDING =3;
    protected static final int LINE_GAP = 2;

    protected WidgetConfig config;


    public abstract  String displayName();
    public abstract  String typeId();

    protected  abstract List<HudLine> lines(MinecraftClient mc);

    public void attach(WidgetConfig config) {
        this.config = config;
    }

    public WidgetConfig config(){
        return config;
    }

    public int getWidth(MinecraftClient mc) {
        int max = 0;
        for (HudLine line : lines(mc)){
            max = Math.max(max, line.width(mc));
        }
        return max + PADDING * 2;
    }

    public int getHeight(MinecraftClient mc){
        List<HudLine> l = lines(mc);
        if (l.isEmpty()) return 0;
        int h = PADDING * 2;
        for (int i = 0; i < l.size(); i++){
            h += l.get(i).height(mc);
            if (i < l.size() -1) h += LINE_GAP;
        }
        return  h;
    }

    public void renderAt(DrawContext ctx, int x, int y, MinecraftClient mc){
        List<HudLine> l = lines(mc);
        if (l.isEmpty()) return;
        int w = getWidth(mc);
        int h = getHeight(mc);

        int bgAlpha = config != null ? config.backgroundOpacity : 140;
        int textAlpha = config != null ? config.textOpacity : 255;
        int rgb = config != null ? config.textColor : 0xFFFFFF;

        ctx.fill(x,y,x + w, y+h, (bgAlpha << 24));

        int ty = y + PADDING;
        for (HudLine line : l){
            line.draw(ctx, x + PADDING, ty, rgb, textAlpha, mc);
            ty += line.height(mc) + LINE_GAP;
        }
    }


}
