package com.kotanch.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kotanch.client.HudForgeClient;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static net.minecraft.client.util.tracy.TracyLoader.load;
import static net.minecraft.server.dedicated.management.dispatch.ServerRpcDispatcher.save;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("hudforge.json");

    private static HudConfig config;
    private ConfigManager() {}

    public static HudConfig get() {
        if (config == null) load();
        return config;
    }

    public static void load(){
        try{
            if (Files.exists(PATH)) {
                String json = Files.readString(PATH);
                config = GSON.fromJson(json, HudConfig.class);
            }
        }catch (Exception e) {
            HudForgeClient.LOGGER.error("Failed to read config, using defaults",e);
        }

        if (config ==null || config.widgets ==null){
            config = HudConfig.defaults();
            save();
        }
    }

    public static void save() {
        try {
            Files.createDirectories(PATH.getParent());
            Files.writeString(PATH, GSON.toJson(config));
        } catch (IOException e){
            HudForgeClient.LOGGER.error("Failed to save config",e);
        }
    }
}
