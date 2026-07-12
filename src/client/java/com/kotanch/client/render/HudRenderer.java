package com.kotanch.client.render;

import com.kotanch.client.widget.HudWidget;
import com.kotanch.client.widget.impl.CoordinatesWidget;
import com.kotanch.client.widget.impl.DaysWidget;
import net.fabricmc.loader.impl.lib.sat4j.pb.constraints.MinCardConstructor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public class HudRenderer {
    private static final List<HudWidget> WIDGETS = new ArrayList<>();

    static {
        WIDGETS.add(new CoordinatesWidget());
        WIDGETS.add(new DaysWidget());
    }
    private HudRenderer(){}

    public static void render(DrawContext ctx){
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.world == null) return;
        if (mc.options.hudHidden) return;

        int x = 4;
        int y = 4;

        for (HudWidget w: WIDGETS){
            w.renderAt(ctx,x,y,mc);
            y += w.getHeight(mc) + 2;
        }
    }
}
