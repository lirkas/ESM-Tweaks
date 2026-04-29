package lirkas.esmtweaks.event.handler;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import lirkas.esmtweaks.config.ModConfig;
import lirkas.esmtweaks.util.HarvestUtil;


public class PlayerEventHandler {
    
    @SubscribeEvent
    public void onPlayerLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        
        if(!event.getWorld().isRemote) {
            return;
        }

        // in-game visual feedback for block-related informations
        if(ModConfig.Debug.enableDebug.getValue()) {

            if(ModConfig.Debug.outputBlockInfoInChat.getValue()) {
                event.getEntityPlayer().sendMessage(
                    HarvestUtil.getBlockInfosTextMessage(
                        event.getPos(), event.getEntityPlayer(), ModConfig.Debug.useOffhandItemForChecks.getValue()));
            }
            
            if(ModConfig.Debug.showCanBreakBlockMessage.getValue()) {
                event.getEntityPlayer().sendStatusMessage(
                    HarvestUtil.getBlockBreakableTextMessage(
                        event.getPos(), event.getEntityPlayer(), ModConfig.Debug.useOffhandItemForChecks.getValue()), true);
            }
        }
    }
}