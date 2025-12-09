package com.visualmod.modules;

import com.visualmod.VisualMod;
import net.minecraft.client.MinecraftClient;

/**
 * Base class for all visual effect modules
 * Provides common functionality and lifecycle methods
 */
public abstract class Module {
    private final String name;
    private final String description;
    private final ModuleCategory category;
    private boolean enabled;
    private int keybind;
    
    /**
     * Constructor for Module
     * @param name Module name
     * @param description Module description
     * @param category Module category
     */
    public Module(String name, String description, ModuleCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = false;
        this.keybind = -1;
    }
    
    /**
     * Called when module is enabled
     */
    public void onEnable() {
        VisualMod.LOGGER.debug("Enabled module: {}", name);
    }
    
    /**
     * Called when module is disabled
     */
    public void onDisable() {
        VisualMod.LOGGER.debug("Disabled module: {}", name);
    }
    
    /**
     * Called every client tick when module is enabled
     * @param client Minecraft client instance
     */
    public void onTick(MinecraftClient client) {
        // Override in subclasses if needed
    }
    
    /**
     * Toggle module enabled state
     */
    public void toggle() {
        setEnabled(!enabled);
    }
    
    /**
     * Set module enabled state
     * @param enabled New enabled state
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }
    
    // Getters and setters
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public ModuleCategory getCategory() {
        return category;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public int getKeybind() {
        return keybind;
    }
    
    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }
}