package lirkas.esmtweaks.event.handler;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.config.ModConfig;


public class ConfigEventHandler {
 
    /**
     * Makes sure all the config variables in the config file are saved whenever the user
     * changes them from the game.
     * 
     * @param event The config change event.
     */
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {

        if(event.getModID().equals(ESMTweaks.MOD_ID)) {

            ESMTweaks.logger.trace("onConfigChanged");
            ModConfig.updateValues();
            ModConfig.configuration.save();
        }
    }
}