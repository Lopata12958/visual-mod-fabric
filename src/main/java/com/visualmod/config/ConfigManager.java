package com.visualmod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.visualmod.VisualMod;
import com.visualmod.VisualModClient;
import com.visualmod.modules.Module;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Manages configuration saving and loading
 * Stores module states, settings, and keybindings
 */
public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path configDir;
    private final File configFile;
    
    /**
     * Initialize configuration manager
     * Creates config directory and file if they don't exist
     */
    public ConfigManager() {
        configDir = FabricLoader.getInstance().getConfigDir().resolve("visualmod");
        configFile = configDir.resolve("config.json").toFile();
        
        try {
            Files.createDirectories(configDir);
            if (!configFile.exists()) {
                configFile.createNewFile();
                VisualMod.LOGGER.info("Created new config file: {}", configFile.getAbsolutePath());
            }
        } catch (IOException e) {
            VisualMod.LOGGER.error("Failed to create config directory or file: {}", e.getMessage());
        }
    }
    
    /**
     * Load configuration from file
     */
    public void loadConfig() {
        if (!configFile.exists()) {
            VisualMod.LOGGER.warn("Config file does not exist, using defaults");
            return;
        }
        
        try (FileReader reader = new FileReader(configFile)) {
            JsonObject config = GSON.fromJson(reader, JsonObject.class);
            if (config == null) {
                VisualMod.LOGGER.warn("Config file is empty, using defaults");
                return;
            }
            
            // Load module states
            if (config.has("modules")) {
                JsonObject modules = config.getAsJsonObject("modules");
                loadModuleStates(modules);
            }
            
            VisualMod.LOGGER.info("Configuration loaded successfully");
        } catch (IOException e) {
            VisualMod.LOGGER.error("Failed to load config: {}", e.getMessage());
        } catch (Exception e) {
            VisualMod.LOGGER.error("Error parsing config: {}", e.getMessage());
        }
    }
    
    /**
     * Save current configuration to file
     */
    public void saveConfig() {
        try (FileWriter writer = new FileWriter(configFile)) {
            JsonObject config = new JsonObject();
            
            // Save module states
            JsonObject modules = new JsonObject();
            if (VisualModClient.getModuleManager() != null) {
                for (Module module : VisualModClient.getModuleManager().getModules()) {
                    JsonObject moduleConfig = new JsonObject();
                    moduleConfig.addProperty("enabled", module.isEnabled());
                    moduleConfig.addProperty("keybind", module.getKeybind());
                    modules.add(module.getName(), moduleConfig);
                }
            }
            config.add("modules", modules);
            
            GSON.toJson(config, writer);
            VisualMod.LOGGER.info("Configuration saved successfully");
        } catch (IOException e) {
            VisualMod.LOGGER.error("Failed to save config: {}", e.getMessage());
        }
    }
    
    /**
     * Load module states from JSON object
     * @param modules JSON object containing module configurations
     */
    private void loadModuleStates(JsonObject modules) {
        if (VisualModClient.getModuleManager() == null) return;
        
        for (String moduleName : modules.keySet()) {
            Module module = VisualModClient.getModuleManager().getModuleByName(moduleName);
            if (module != null) {
                JsonObject moduleConfig = modules.getAsJsonObject(moduleName);
                
                if (moduleConfig.has("enabled")) {
                    module.setEnabled(moduleConfig.get("enabled").getAsBoolean());
                }
                
                if (moduleConfig.has("keybind")) {
                    module.setKeybind(moduleConfig.get("keybind").getAsInt());
                }
            }
        }
    }
    
    /**
     * Export current configuration to a preset file
     * @param presetName Name of the preset
     */
    public void exportPreset(String presetName) {
        File presetFile = configDir.resolve("presets").resolve(presetName + ".json").toFile();
        try {
            Files.createDirectories(presetFile.toPath().getParent());
            presetFile.createNewFile();
            // Copy current config to preset
            Files.copy(configFile.toPath(), presetFile.toPath());
            VisualMod.LOGGER.info("Exported preset: {}", presetName);
        } catch (IOException e) {
            VisualMod.LOGGER.error("Failed to export preset: {}", e.getMessage());
        }
    }
}