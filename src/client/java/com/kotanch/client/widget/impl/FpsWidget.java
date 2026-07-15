package com.kotanch.client.widget.impl;

import com.kotanch.client.widget.HudWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.List;

public class FpsWidget extends HudWidget {
    @Override public String displayName() { return "FPS";}
    @Override public String typeId() {return "fps";}

    @Override
    protected List<Text> lines(MinecraftClient mc) {
        return List.of(Text.literal(mc.getCurrentFps() + " fps"));
    }
}
