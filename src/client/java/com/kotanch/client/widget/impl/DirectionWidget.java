package com.kotanch.client.widget.impl;

import com.kotanch.client.element.HudLine;
import com.kotanch.client.element.TextElement;
import com.kotanch.client.widget.HudWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class DirectionWidget extends HudWidget {
    private static final String[] NAMES = {"S", "SW", "W", "NW", "N", "NE", "E", "SE"};
    private static String[] AXIS =  {"+Z", "+Z -X", "-X", "-Z -X", "-Z", "-Z +X", "+X", "+Z +X"};

    @Override public String displayName() {return "Direction";}
    @Override public String typeId() {return "direction";}

    @Override
    protected List<HudLine> lines(MinecraftClient mc) {
        if (mc.player == null) return List.of();
        float yaw = MathHelper.wrapDegrees(mc.player.getYaw()) + 180f;
        int i = Math.round(yaw / 45f) & 7;
        return List.of(new HudLine().add(new TextElement(NAMES[i] + " (" + AXIS[i] + ")")));
    }
}
