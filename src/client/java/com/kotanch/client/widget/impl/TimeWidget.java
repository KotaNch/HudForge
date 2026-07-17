package com.kotanch.client.widget.impl;

import com.kotanch.client.element.HudLine;
import com.kotanch.client.element.TextElement;
import com.kotanch.client.widget.HudWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.List;

public class TimeWidget extends HudWidget {
    private  static  final long DAY = 24000L;
    private static final long NIGHT_START = 13000L;

    @Override
    public String displayName() {
        return "Time";
    }

    @Override
    public String typeId() {
        return "time";
    }

    @Override
    protected List<HudLine> lines(MinecraftClient mc){
        if (mc.world == null) return List.of();

    long tod = ((mc.world.getTimeOfDay() % DAY) + DAY) % DAY;

    long totalMinutes = (tod * 24 * 60 / DAY + 6 * 60) %(24 * 60);
    long hh = totalMinutes / 60;
    long mm = totalMinutes % 60;
    String clock = String.format("%02d:%02d", hh,mm);

    String toNight;
    if (tod < NIGHT_START){
        long secs = (NIGHT_START - tod) / 20;
        toNight = String.format("night in %d:%02d", secs / 60, secs % 60);
    }else {
        toNight = "night";
    }
    return  List.of(new HudLine().add(new TextElement(clock + " " + toNight)));
    }
}
