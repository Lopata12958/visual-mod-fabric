package com.visualmod.modules;

/**
 * Categories for organizing modules in the GUI
 */
public enum ModuleCategory {
    VISUALS("Visuals", "Visual effect modules"),
    PULSE("Pulse", "Pulse visual effects"),
    LAMINAR("Laminar", "Laminar visual effects"),
    OPKA("OPKA", "OPKA visual effects"),
    UTILITIES("Utilities", "Utility modules"),
    EFFECTS("Effects", "Additional effects"),
    SETTINGS("Settings", "General settings");
    
    private final String name;
    private final String description;
    
    ModuleCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
}