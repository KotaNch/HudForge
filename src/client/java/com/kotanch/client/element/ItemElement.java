package com.kotanch.client.element;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;

public class ItemElement implements  HudElement{
    private static final int SIZE = 16;
    private final ItemStack stack;

    public ItemElement(ItemStack stack){
        this.stack = stack;
    }

    @Override
    public void draw(DrawContext ctx, int x, int y, int textColor, int textAlpha, MinecraftClient mc){
        if (stack == null || stack.isEmpty()) return;
        ctx.drawItem(stack,x,y);
    }

    @Override
    public int width(MinecraftClient mc){
        return (stack == null || stack.isEmpty()) ? 0 : SIZE;
    }

    @Override
    public int height(MinecraftClient mc) {
        return (stack == null || stack.isEmpty()) ? 0 : SIZE;
    }
}
