package com.kotanch.client.data;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.MaceItem;

@FunctionalInterface
public interface DataSource {
    String get(MinecraftClient mc);
}