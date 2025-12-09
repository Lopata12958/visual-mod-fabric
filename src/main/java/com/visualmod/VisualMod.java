package com.visualmod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main mod class for Visual Mod
 * Provides visual effects including Pulse Visual, Laminar Visual, and OPKA Visual
 * 
 * @author Lopata12958
 * @version 1.0.0
 */
public class VisualMod {
    public static final String MOD_ID = "visualmod";
    public static final String MOD_NAME = "Visual Mod";
    public static final String VERSION = "1.0.0";
    
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    
    /**
     * Initialize the mod
     * Called during mod initialization phase
     */
    public static void init() {
        LOGGER.info("Initializing {} v{}", MOD_NAME, VERSION);
        LOGGER.info("Loading visual effect modules...");
    }
    
    /**
     * Get the mod logger instance
     * @return Logger instance for this mod
     */
    public static Logger getLogger() {
        return LOGGER;
    }
}