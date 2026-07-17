package com.kotanch.client.data;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.client.MinecraftClient;

public final class DataRegistry {
    private static final Map<String, DataSource> SOURCES = new LinkedHashMap<>();

    static {
        register("x", mc -> mc.player == null ? "?" : String.format("%.1f", mc.player.getX()));
        register("y", mc -> mc.player == null ? "?" : String.format("%.1f", mc.player.getY()));
        register("z", mc -> mc.player == null ? "?" : String.format("%.1f", mc.player.getZ()));
        register("day", mc -> mc.world == null ? "?" : String.valueOf(mc.world.getTimeOfDay()/ 24000L));
        register("fps", mc -> String.valueOf(mc.getCurrentFps()));
    }

    private  DataRegistry() {}

    public static void register(String id, DataSource source){
        SOURCES.put(id, source);
    }

    public static Map<String, DataSource> sources() {
        return SOURCES;
    }

    public static String apply(String template, MinecraftClient mc){
        if (template == null || template.isEmpty()) return  "";

        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < template.length()){
            char c = template.charAt(i);
            if (c == '{'){
                int end = template.indexOf('}', i);
                if (end > i) {
                    String id = template.substring(i + 1, end);
                    DataSource src = SOURCES.get(id);
                    if (src != null) {
                        out.append(src.get(mc));
                    } else {
                        out.append('{').append(id).append('}');
                    }
                    i = end + 1;
                    continue;
                }
            }
            out.append(c);
            i++;
        }
        return out.toString();
    }
}
