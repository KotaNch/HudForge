package com.kotanch.client;

import com.kotanch.client.render.HudRenderer;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HudForgeClient implements ClientModInitializer {
	public static final String MOD_ID = "hudforge";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("HudForge loaded");

		HudElementRegistry.attachElementBefore(
				VanillaHudElements.CHAT,
				Identifier.of(MOD_ID,"widgets"),
				((context, tickCounter) -> HudRenderer.render(context))
		);
	}
}