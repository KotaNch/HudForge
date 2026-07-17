package com.kotanch.client.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class TextElement implements HudElement{
    private final String text;

    public TextElement(String text) {
        this.text = text;
    }

    @Override
    public void draw(DrawContext ctx, int x, int y, int textColor, int textAlpha, MinecraftClient mc){
        ctx.drawText(mc.textRenderer, Text.literal(text), x, y, (textAlpha << 24) | textColor, true);
    }

    @Override
    public int width(MinecraftClient mc){
        return mc.textRenderer.getWidth(text);
    }

    @Override
    public int height(MinecraftClient mc){
        return 8;
    }
}
