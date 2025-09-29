package lirkas.esmtweaks.event.handler;

import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import funwayguy.epicsiegemod.api.ITaskAddition;
import funwayguy.epicsiegemod.api.TaskRegistry;
import funwayguy.epicsiegemod.handlers.MainHandler;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.ai.addition.DiggingAITaskAddition;
import lirkas.esmtweaks.config.ModConfig;
import lirkas.esmtweaks.util.EntityUtil;
import lirkas.esmtweaks.util.Util;

/**
 * Handle general events related to non-player entities.
 */
public class EntityEventHandler {
    
    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        
        // check if this is needed here or not
        if(event.getEntity().world.isRemote) {
            return;
        }
        
        if(ModConfig.AI.updateAITaskOnDeath && EntityLiving.class.isInstance(event.getEntityLiving())) {

            EntityLiving entityLiving = (EntityLiving) event.getEntityLiving();
            ESMTweaks.logger.debug("updating AITask on death for " + entityLiving.getName());
            
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

        // only doing things if this is an EntityLiving
        if(!EntityLiving.class.isInstance(event.getEntity())) {
            return;
        }

        EntityLiving entityLiving = (EntityLiving) event.getEntity();

        ESMTweaks.logger.debug("onEntityConstruct " + entityLiving.getName());

        // should not be called if ESM onEntityConstruct is still registered as a listener
        new MainHandler().onEntityConstruct(event);

        ESMTweaks.logger.debug("Tasks : ");
        for(String taskName : EntityUtil.getAITasks(entityLiving, true)) {
            ESMTweaks.logger.debug("    " + taskName);
        }
    }

    /**
     * Called when an entity spawns for the first time.
     */
    @SubscribeEvent
    public void onEntityLivingSpawn(LivingSpawnEvent.SpecialSpawn event) {
        
        // only doing things if this is an EntityLiving
        if(!EntityLiving.class.isInstance(event.getEntityLiving())) {
            return;
        }

        ESMTweaks.logger.debug("onEntityLivingSpawn "  + event.getEntity().getName());

        EntityLiving entityLiving = (EntityLiving) event.getEntityLiving();

        ESMTweaks.logger.debug(event.getEntityLiving().getName() + " has spawned at" + 
            " x: " + event.getEntityLiving().getPosition().getX() +
            " y: " + event.getEntityLiving().getPosition().getY() +
            " z: " + event.getEntityLiving().getPosition().getZ());

        // going through each task in the registry to see if one in there
        // matches the DiggingAI and if the mob is capable to receive it.
        for(ITaskAddition taskAddition : TaskRegistry.INSTANCE.getAllAdditions()) {
            if(DiggingAITaskAddition.class.isInstance(taskAddition) && taskAddition.isValid(entityLiving)) {
                
                this.onDiggingCapableEntitySpawn(entityLiving);
            }
        }
    }

    /**
     * Currently called before the DiggingAI Task is added to this entity by ESM.
     * This event does not guarantee that the entity will receive the DiggingAI Task,
     * since the event adding it can still be cancelled after this method.
     * This method is only called for this mod's DiggingAI Task.
     */
    public void onDiggingCapableEntitySpawn(EntityLiving entityLiving) {

        // 25% chance to obtain a pickaxe, if the config allows it.
        // note the chance to be given a tool from vanilla mechanics is still happening (possibly before this event?)
        if(ModConfig.AI.shouldBeGivenTool && Util.isLucky(ModConfig.AI.toolChance, 100, entityLiving.getRNG())) {

            if(ModConfig.AI.shouldOverrideTool || entityLiving.getHeldItemMainhand().isEmpty()) {
                entityLiving.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
            }
        }
    }
}
