package com.visualmod;

import com.visualmod.config.ConfigManager;
import com.visualmod.gui.ClickGuiScreen;
import com.visualmod.modules.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * Client-side initialization for Visual Mod
 * Handles client-specific setup including keybindings, modules, and GUI
 */
public class VisualModClient implements ClientModInitializer {
    private static ModuleManager moduleManager;
    private static ConfigManager configManager;
    
    // Keybinding for opening GUI (Right Shift)
    private static KeyBinding openGuiKey;
    
    @Override
    public void onInitializeClient() {
        VisualMod.init();
        
        // Initialize configuration manager
        configManager = new ConfigManager();
        configManager.loadConfig();
        VisualMod.LOGGER.info("Configuration loaded");
        
        // Initialize module manager
        moduleManager = new ModuleManager();
        moduleManager.registerModules();
        VisualMod.LOGGER.info("Modules registered");
        
        // Register keybindings
        registerKeybindings();
        
        // Register client tick events
        registerEvents();
        
        VisualMod.LOGGER.info("Visual Mod client initialized successfully");
    }
    
    /**
     * Register keybindings for the mod
     */
    private void registerKeybindings() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.visualmod.open_gui",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_RIGHT_SHIFT,
            "category.visualmod.general"
        ));
        
        VisualMod.LOGGER.info("Keybindings registered");
    }
    
    /**
     * Register client events
     */
    private void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Check for GUI keybinding
            if (openGuiKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new ClickGuiScreen());
                }
            }
            
            // Update all modules
            if (moduleManager != null) {
                moduleManager.onClientTick(client);
            }
        });
    }
    
    /**
     * Get the module manager instance
     * @return ModuleManager instance
     */
    public static ModuleManager getModuleManager() {
        return moduleManager;
    }
    
    /**
     * Get the configuration manager instance
     * @return ConfigManager instance
     */
    public static ConfigManager getConfigManager() {
        return configManager;
    }
}