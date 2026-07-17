package com.kotanch.client.config;

import com.kotanch.client.render.Anchor;

import java.util.ArrayList;
import java.util.List;

public class HudConfig {

    public List<WidgetConfig> widgets = new ArrayList<>();

    public static HudConfig defaults() {
        HudConfig c = new HudConfig();
        WidgetConfig hand = new WidgetConfig("template", Anchor.BOTTOM_CENTER,0, -20);
        hand.template = "{hand_icon} {durability}";
        c.widgets.add(hand);
        WidgetConfig test = new WidgetConfig("template",Anchor.CENTER,10,10);
        test.template = "HP {health}/{max_health}  Food {hunger}  Lvl {xp_level}";
        c.widgets.add(test);
        WidgetConfig armor = new WidgetConfig("template", Anchor.MIDDLE_RIGHT, -4, 0);
        armor.template =
                "{helmet_icon} {helmet}\n" +
                "{chestplate_icon} {chestplate}\n" +
                "{leggings_icon} {leggings}\n" +
                "{boots_icon} {boots}";
        c.widgets.add(armor);
        c.widgets.add(new WidgetConfig("time", Anchor.TOP_LEFT, 4, 48));
        c.widgets.add(new WidgetConfig("coordinates", Anchor.TOP_LEFT,4,4));
        c.widgets.add(new WidgetConfig("days", Anchor.TOP_LEFT,4,26));
        c.widgets.add(new WidgetConfig("fps", Anchor.TOP_RIGHT, -4, 4));
        c.widgets.add(new WidgetConfig("direction", Anchor.TOP_RIGHT, -4, 18));
        c.widgets.add(new WidgetConfig("biome", Anchor.BOTTOM_LEFT, 4, -14));
        return c;
    }
}
