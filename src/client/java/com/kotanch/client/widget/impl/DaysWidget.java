package com.kotanch.client.widget.impl;

import com.kotanch.client.widget.HudWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.List;

public class DaysWidget extends HudWidget {
    public static final long TICKS_PER_DAY = 24000L;

    @Override
    public String displayName(){
        return "Days";
    }

    @Override
    public String typeId(){
        return "days";
    }

    @Override
    protected List<Text> lines(MinecraftClient mc) {
        if (mc.world == null) return List.of();

        long day = mc.world.getTimeOfDay()/ TICKS_PER_DAY;
        return  List.of(Text.literal("Day " + day));
    }

}
