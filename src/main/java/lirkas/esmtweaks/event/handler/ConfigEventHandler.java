package lirkas.esmtweaks.event.handler;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import lirkas.esmtweaks.ESMTweaks;


public class ConfigEventHandler {
 
    /**
     * Makes sure all the config variables in the config file are saved whenever the user
     * changes them from the game.
     * 
     * @param event The config change event.
     */
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event) {

        if(event.getModID().equals(ESMTweaks.MOD_ID)) {

            ESMTweaks.logger.debug("onConfigChanged");
            ConfigManager.sync(ESMTweaks.MOD_ID, Config.Type.INSTANCE);
        }
    }
}