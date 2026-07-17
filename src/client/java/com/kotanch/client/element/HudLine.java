package com.kotanch.client.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;

public class HudLine {
    private static final int GAP = 2;
    private final List<HudElement> elements = new ArrayList<>();

    public HudLine add(HudElement e){
        elements.add(e);
        return this;
    }

    public boolean isEmpty(){
        return elements.isEmpty();
    }

    public int width(MinecraftClient mc){
        int w = 0;
        for (int i = 0; i < elements.size(); i++) {
            w += elements.get(i).width(mc);
            if (i < elements.size() -1) w += GAP;
        }
        return  w;
    }

    public int height(MinecraftClient mc){
        int h = 0;
        for (HudElement e : elements) {
            h = Math.max(h, e.height(mc));
        }
        return h;
    }

    public void draw(DrawContext ctx, int x, int y, int textColor, int textAlpha, MinecraftClient mc) {
        int cx =x;
        int rowH = height(mc);
        for (HudElement e : elements) {
            int ey = y + (rowH - e.height(mc)) /2;
            e.draw(ctx, cx, ey, textColor, textAlpha, mc);
            cx += e.width(mc) + GAP;
        }
    }
}
