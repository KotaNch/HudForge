package com.kotanch.client.widget.impl;

import com.kotanch.client.element.HudLine;
import com.kotanch.client.element.TextElement;
import com.kotanch.client.widget.HudWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.List;

public class BiomeWidget extends HudWidget {
    @Override public String displayName() { return "Biome";}
    @Override public String typeId() {return "biome";}

    @Override
    protected List<HudLine> lines(MinecraftClient mc) {
        if (mc.world == null || mc.player == null ) return List.of();
        String id = mc.world.getBiome(mc.player.getBlockPos())
                .getKey()
                .map(k -> k.getValue().getPath())
                .orElse("unknown");
        return  List.of(new HudLine().add(new TextElement("Biome: " + id)));
    }
}
