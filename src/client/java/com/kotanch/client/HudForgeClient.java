package com.kotanch.client;

import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
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
				(context, tickCounter) -> {
					MinecraftClient mc = MinecraftClient.getInstance();
					if (mc.player == null || mc.world == null) return;
					if (mc.options.hudHidden) return;

					String text = String.format("XYZ %.1f %.1f %.1f",
							mc.player.getX(), mc.player.getY(), mc.player.getZ());

					context.drawText(mc.textRenderer, Text.literal(text),4,4,0xFFFFFFFF,true);
				}
		);
	}
}