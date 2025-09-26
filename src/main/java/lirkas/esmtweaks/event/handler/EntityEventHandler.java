package lirkas.esmtweaks.event.handler;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.config.ModConfig;

/**
 * Handle events related to mob entities
 */
public class EntityEventHandler {
    
    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        
        if(ModConfig.AI.updateAITaskOnDeath && EntityLiving.class.isInstance(event.getEntityLiving())) {

            EntityLiving entityLiving = (EntityLiving) event.getEntityLiving();
            ESMTweaks.logger.debug("updating AITask on death for " + entityLiving.getDisplayName());
        
            // this allows for AI tasks to reset when the mob dies but it could cause unexpected issues 
            // with other mobs since it targets all mobs that have tasks attached to them.
            entityLiving.tasks.onUpdateTasks();
        }
    }
}
