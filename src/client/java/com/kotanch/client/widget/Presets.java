package com.kotanch.client.widget;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Ready-made template widgets shown in the "+ Add" menu.
 * Each entry is a display name mapped to a template string.
 * Add a new preset = one line here.
 */
public final class Presets {
    public static final Map<String, String> ALL = new LinkedHashMap<>();

    static {
        ALL.put("Coordinates", "XYZ {x} {y} {z}");
        ALL.put("Coords + Dir", "XYZ {x} {y} {z}|Facing {direction}");
        ALL.put("Day & Time", "Day {day}|{time}");
        ALL.put("FPS", "{fps} fps");
        ALL.put("Armor", "{helmet_icon} {helmet}|{chestplate_icon} {chestplate}|{leggings_icon} {leggings}|{boots_icon} {boots}");
        ALL.put("Tool", "{hand_icon} {durability}");
        ALL.put("Health", "HP {health}/{max_health}");
        ALL.put("Hunger", "Food {hunger}/20");
        ALL.put("Speed", "Speed {speed}");
        ALL.put("Biome", "Biome {biome}");
        ALL.put("Chunk", "Chunk {chunk_x} {chunk_z}");
        ALL.put("Light", "Light {light}");
        ALL.put("Real time", "{rl_time}");
        ALL.put("Empty", "");
    }

    private Presets() {}
}