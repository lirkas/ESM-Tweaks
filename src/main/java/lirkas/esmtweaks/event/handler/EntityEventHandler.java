package lirkas.esmtweaks.event.handler;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.config.ModConfig;

/**
 * Handle general events related to entities
 */
public class EntityEventHandler {
    
    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        
        if(ModConfig.AI.updateAITaskOnDeath && EntityLiving.class.isInstance(event.getEntityLiving())) {

            EntityLiving entityLiving = (EntityLiving) event.getEntityLiving();
            ESMTweaks.logger.debug("updating AITask on death for " + entityLiving.getDisplayName());
            
            //testing
            ESMTweaks.logger.debug("AI List for " + entityLiving.getDisplayName());
            for(EntityAITaskEntry taskEntry : entityLiving.tasks.taskEntries) {
                ESMTweaks.logger.debug("    "+taskEntry.action.getClass()+"    ");
            }

            // this allows for AI tasks to reset when the mob dies but it could cause unexpected issues 
            // with other mobs since it targets all mobs that have tasks attached to them.
            entityLiving.tasks.onUpdateTasks();
        }
    }

    @SubscribeEvent
    public void onEntityConstruct(EntityJoinWorldEvent event) {

        if(event.getWorld().isRemote) {
            return;
        }
        
        if(event.getEntity() instanceof EntityLiving) {

			EntityLiving entityLiving = (EntityLiving)event.getEntity();
            ESMTweaks.logger.debug("onEntityConstruct : " + entityLiving.getDisplayName());

        }
    }
}
