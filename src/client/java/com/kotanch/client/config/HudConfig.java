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
        return c;
    }
}
