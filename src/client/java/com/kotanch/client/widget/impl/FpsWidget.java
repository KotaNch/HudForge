package com.kotanch.client.widget.impl;

import com.kotanch.client.element.HudLine;
import com.kotanch.client.element.TextElement;
import com.kotanch.client.widget.HudWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.List;

public class FpsWidget extends HudWidget {
    @Override public String displayName() { return "FPS";}
    @Override public String typeId() {return "fps";}

    @Override
    protected List<HudLine> lines(MinecraftClient mc) {
        return List.of(new HudLine().add(new TextElement(mc.getCurrentFps() + " fps")));
    }
}
