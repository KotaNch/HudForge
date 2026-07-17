package com.kotanch.client.config;

import com.kotanch.client.render.Anchor;

public class WidgetConfig {
    public String type;
    public boolean enabled = true;

    public Anchor anchor = Anchor.TOP_LEFT;
    public int offsetX = 4;
    public int offsetY = 4;

    public int textColor = 0xFFFFFF;
    public int backgroundOpacity = 0;
    public int textOpacity = 255;
    public float scale = 0.75f;

    public String template = "";
    public WidgetConfig() {}

    public WidgetConfig(String type, Anchor anchor, int offsetX, int offsetY){
        this.type = type;
        this.anchor = anchor;
        this.offsetX = offsetX;
        this.offsetY = offsetY;

    }
}
