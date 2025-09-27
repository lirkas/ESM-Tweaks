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
        if(ModConfig.MISC.enableDebug) {

            if(ModConfig.MISC.displayBlockInfoInChat) {
                event.getEntityPlayer().sendMessage(
                    HarvestUtil.getBlockInfosTextMessage(
                        event.getPos(), event.getEntityPlayer(), ModConfig.MISC.useOffHandItem));
            }
            
            if(ModConfig.MISC.displayCanBreakBlockMessage) {
                event.getEntityPlayer().sendStatusMessage(
                    HarvestUtil.getBlockBreakableTextMessage(
                        event.getPos(), event.getEntityPlayer(), ModConfig.MISC.useOffHandItem), true);
            }
        }
    }
}
