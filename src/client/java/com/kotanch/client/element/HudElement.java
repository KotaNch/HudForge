package com.kotanch.client.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public interface HudElement {
    void draw(DrawContext ctx, int x, int y, int textColor, int textAlpha, MinecraftClient mc);
    int width(MinecraftClient mc);
    int height(MinecraftClient mc);
}
