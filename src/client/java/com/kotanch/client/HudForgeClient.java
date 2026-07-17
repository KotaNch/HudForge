package com.kotanch.client;

import com.kotanch.client.config.ConfigManager;
import com.kotanch.client.editor.HudEditorScreen;
import com.kotanch.client.render.HudRenderer;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HudForgeClient implements ClientModInitializer {
	public static final String MOD_ID = "hudforge";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static int statsTimer = 0;

	private static KeyBinding openEditorKey;
	private  static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(Identifier.of(MOD_ID, "general"));
	@Override
	public void onInitializeClient() {
		LOGGER.info("HudForge loaded");

		ConfigManager.load();

		HudElementRegistry.attachElementBefore(
				VanillaHudElements.CHAT,
				Identifier.of(MOD_ID,"widgets"),
				((context, tickCounter) -> HudRenderer.render(context))
		);
		openEditorKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.hudforge.open_editor",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_H,
				CATEGORY
		));

		ClientTickEvents.END_CLIENT_TICK.register(client ->{
			while (openEditorKey.wasPressed()){
				if (client.currentScreen == null) {
					client.setScreen(new HudEditorScreen());
				}
			}
			if (client.player != null && client.getNetworkHandler() != null){
				statsTimer++;
				if (statsTimer >= 20){
					statsTimer = 0;
					client.getNetworkHandler().sendPacket(new ClientStatusC2SPacket(ClientStatusC2SPacket.Mode.REQUEST_STATS));
				}
			} else{
				statsTimer = 0;
			}
		});
	}
}