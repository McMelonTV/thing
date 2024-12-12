package com.nodiumhosting.reactui.client;

import javafx.application.Platform;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ReactUIClient implements ClientModInitializer {
    private static KeyBinding openHtmlKey;

    @Override
    public void onInitializeClient() {
        Platform.startup(() -> {});

        // Register key binding
        openHtmlKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.htmlmod.open_html_screen",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H, // Default key is 'H'
                "category.htmlmod.keys"
        ));

        // Open the HTML screen when the key is pressed
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (openHtmlKey.wasPressed() && client.currentScreen == null) {
                client.setScreen(new WebViewScreen());
            }
        });
    }
}
