package com.kotanch.client.widget.impl;

import com.kotanch.client.element.HudLine;
import com.kotanch.client.element.TextElement;
import com.kotanch.client.widget.HudWidget;
import net.minecraft.client.MinecraftClient;

import java.util.List;

public class CoordinatesWidget extends HudWidget {
    @Override
    public String displayName(){
        return "Coordinates";
    }
    @Override
    public String typeId(){
        return "coordinates";
    }

    @Override
    protected List<HudLine> lines(MinecraftClient mc){
        if (mc.player == null) return  List.of();

        double x = mc.player.getX();
        double y = mc.player.getY();
        double z = mc.player.getZ();

        return List.of(new HudLine().add(new TextElement(String.format("XYZ %.1f  %.1f  %.1f", x, y, z))));

    }


}
