package com.visualmod.modules;

import com.visualmod.VisualMod;
import com.visualmod.modules.impl.laminar.*;
import com.visualmod.modules.impl.opka.*;
import com.visualmod.modules.impl.pulse.*;
import com.visualmod.modules.impl.utilities.*;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manager for all visual effect modules
 * Handles module registration, lifecycle, and access
 */
public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    
    /**
     * Register all modules
     */
    public void registerModules() {
        VisualMod.LOGGER.info("Registering modules...");
        
        // Pulse Visual modules
        registerModule(new PulseVisualModule());
        registerModule(new ScreenShakeModule());
        
        // Laminar Visual modules
        registerModule(new CustomParticlesModule());
        registerModule(new TrailEffectsModule());
        registerModule(new CustomFogModule());
        registerModule(new SmoothCameraModule());
        
        // OPKA Visual modules
        registerModule(new SwordAnimationModule());
        registerModule(new GlowEffectsModule());
        registerModule(new HitParticlesModule());
        
        // Utility modules
        registerModule(new AutoSprintModule());
        registerModule(new HitSoundModule());
        registerModule(new CounterModule());
        registerModule(new FPSDisplayModule());
        registerModule(new CoordinatesModule());
        
        VisualMod.LOGGER.info("Registered {} modules", modules.size());
    }
    
    /**
     * Register a single module
     * @param module Module to register
     */
    private void registerModule(Module module) {
        modules.add(module);
        VisualMod.LOGGER.debug("Registered module: {}", module.getName());
    }
    
    /**
     * Get all registered modules
     * @return List of all modules
     */
    public List<Module> getModules() {
        return new ArrayList<>(modules);
    }
    
    /**
     * Get modules by category
     * @param category Category to filter by
     * @return List of modules in the specified category
     */
    public List<Module> getModulesByCategory(ModuleCategory category) {
        return modules.stream()
            .filter(m -> m.getCategory() == category)
            .collect(Collectors.toList());
    }
    
    /**
     * Get a module by name
     * @param name Module name
     * @return Module if found, null otherwise
     */
    public Module getModuleByName(String name) {
        return modules.stream()
            .filter(m -> m.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Toggle a module by name
     * @param name Module name
     * @return true if toggled successfully
     */
    public boolean toggleModule(String name) {
        Module module = getModuleByName(name);
        if (module != null) {
            module.toggle();
            return true;
        }
        return false;
    }
    
    /**
     * Called on client tick
     * Updates all enabled modules
     * @param client Minecraft client instance
     */
    public void onClientTick(MinecraftClient client) {
        try {
            modules.stream()
                .filter(Module::isEnabled)
                .forEach(module -> {
                    try {
                        module.onTick(client);
                    } catch (Exception e) {
                        VisualMod.LOGGER.error("Error ticking module {}: {}", 
                            module.getName(), e.getMessage());
                    }
                });
        } catch (Exception e) {
            VisualMod.LOGGER.error("Error in module manager tick: {}", e.getMessage());
        }
    }
}