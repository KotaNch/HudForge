package com.kotanch.client.config;

import com.kotanch.client.render.Anchor;

import java.util.ArrayList;
import java.util.List;

public class HudConfig {

    public List<WidgetConfig> widgets = new ArrayList<>();

    public static HudConfig defaults() {
        HudConfig c = new HudConfig();
        c.widgets.add(new WidgetConfig("time", Anchor.TOP_LEFT, 4, 48));
        c.widgets.add(new WidgetConfig("coordinates", Anchor.TOP_LEFT,4,4));
        c.widgets.add(new WidgetConfig("days", Anchor.TOP_LEFT,4,26));
        c.widgets.add(new WidgetConfig("fps", Anchor.TOP_RIGHT, -4, 4));
        c.widgets.add(new WidgetConfig("direction", Anchor.TOP_RIGHT, -4, 18));
        c.widgets.add(new WidgetConfig("biome", Anchor.BOTTOM_LEFT, 4, -14));
        return c;
    }
}
