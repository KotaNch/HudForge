package com.kotanch.client.render;

import com.kotanch.client.config.ConfigManager;
import com.kotanch.client.config.WidgetConfig;
import com.kotanch.client.widget.HudWidget;
import com.kotanch.client.widget.WidgetRegistry;
import com.kotanch.client.widget.impl.CoordinatesWidget;
import com.kotanch.client.widget.impl.DaysWidget;
import net.fabricmc.loader.impl.lib.sat4j.pb.constraints.MinCardConstructor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

public final class HudRenderer {
    private HudRenderer() {}

    public  static List<HudWidget> buildWidgets(){
        List<HudWidget> out = new ArrayList<>();
        for (WidgetConfig cfg: ConfigManager.get().widgets){
            HudWidget w = WidgetRegistry.create(cfg);
            if (w != null) out.add(w);
        }
        return out;
    }

    public static int[] resolvePos(HudWidget w, MinecraftClient mc, int screenW,int screenH){
        WidgetConfig c = w.config();
        int ww = w.getWidth(mc);
        int wh = w.getHeight(mc);
        int x = c.anchor.originX(screenW,ww,c.offsetX);
        int y = c.anchor.originY(screenH,wh,c.offsetY);
        return new int[]{x,y};
    }
    public static void render(DrawContext ctx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.currentScreen instanceof com.kotanch.client.editor.HudEditorScreen) return;
        if (mc.player == null || mc.world == null) return;
        if (mc.options.hudHidden) return;

        int sw = mc.getWindow().getScaledWidth();
        int sh = mc.getWindow().getScaledHeight();

        for (HudWidget w: buildWidgets()){
            if (!w.config().enabled) continue;
            int[] p = resolvePos(w,mc,sw,sh);
            w.renderAt(ctx,p[0], p[1],mc);
        }
    }
}
