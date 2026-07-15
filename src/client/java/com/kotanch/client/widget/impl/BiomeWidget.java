package com.kotanch.client.widget.impl;

import com.kotanch.client.widget.HudWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.List;

public class BiomeWidget extends HudWidget {
    @Override public String displayName() { return "Biome";}
    @Override public String typeId() {return "biome";}

    @Override
    protected List<Text> lines(MinecraftClient mc) {
        if (mc.world == null || mc.player == null ) return List.of();
        String id = mc.world.getBiome(mc.player.getBlockPos())
                .getKey()
                .map(k -> k.getValue().getPath())
                .orElse("unknown");
        return  List.of(Text.literal("Biome: " + id));
    }
}
