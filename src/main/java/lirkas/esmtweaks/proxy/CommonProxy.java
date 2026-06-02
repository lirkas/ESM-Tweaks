package lirkas.esmtweaks.proxy;

import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;

import funwayguy.epicsiegemod.ai.additions.AdditionAnimalAttack;
import funwayguy.epicsiegemod.ai.additions.AdditionAnimalRetaliate;
import funwayguy.epicsiegemod.ai.additions.AdditionAvoidExplosives;
import funwayguy.epicsiegemod.ai.additions.AdditionDemolition;
import funwayguy.epicsiegemod.ai.additions.AdditionDigger;
import funwayguy.epicsiegemod.ai.additions.AdditionGrief;
import funwayguy.epicsiegemod.ai.additions.AdditionPillaring;
import funwayguy.epicsiegemod.ai.modifiers.ModifierAttackMelee;
import funwayguy.epicsiegemod.ai.modifiers.ModifierBowAttack;
import funwayguy.epicsiegemod.ai.modifiers.ModifierCreeperSwell;
import funwayguy.epicsiegemod.ai.modifiers.ModifierNearestAttackable;
import funwayguy.epicsiegemod.ai.modifiers.ModifierNoPanic;
import funwayguy.epicsiegemod.ai.modifiers.ModifierRangedAttack;
import funwayguy.epicsiegemod.ai.modifiers.ModifierSwimming;
import funwayguy.epicsiegemod.ai.modifiers.ModifierVillagerAvoid;
import funwayguy.epicsiegemod.ai.modifiers.ModifierWander;
import funwayguy.epicsiegemod.ai.modifiers.ModifierZombieAttack;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.ai.addition.DiggingAITaskAddition;
import lirkas.esmtweaks.ai.modifier.MeleeAttackAITaskModifier;
import lirkas.esmtweaks.ai.modifier.ZombieAttackAITaskModifier;
import lirkas.esmtweaks.ai.registrar.AITaskRegistrar;
import lirkas.esmtweaks.config.ModConfig;
import lirkas.esmtweaks.event.registrar.CommonEventHandlerRegistrar;
import lirkas.esmtweaks.util.Util;


public abstract class CommonProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        ESMTweaks.logger = (Logger) event.getModLog();
        ESMTweaks.logger.setLevel(Level.forName(Util.getManifestValue("LogLevel", "INFO"), 400));

        ESMTweaks.logger.trace("CommonProxy preInit");

        ModConfig.init();

        // this is replaced with another event handler that wraps it
        CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
            "funwayguy.epicsiegemod.handlers.MainHandler", 
            "onEntityConstruct", EntityJoinWorldEvent.class);
        CommonEventHandlerRegistrar.INSTANCE.registerAllEventHandlers();

        this.unregisterESMHandlers();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        ESMTweaks.logger.trace("CommonProxy init");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        ESMTweaks.logger.trace("CommonProxy postInit");
    }

    @Override
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        ESMTweaks.logger.trace("CommonProxy serverAboutToStart");
        
        this.registerESMAdditions();
        this.registerESMModifers();

        //TODO: remove all redundant calls?

        // Digging AI
        if(ModConfig.AI.Digging.useTweakedAI.getValue()) {
            AITaskRegistrar.unregisterTasks(AdditionDigger.class);
            AITaskRegistrar.registerTask(new DiggingAITaskAddition());
        }
        else {
            AITaskRegistrar.unregisterTasks(DiggingAITaskAddition.class);
        }

        // Melee AI
        if(ModConfig.AI.Attack.Melee.useTweakedAI.getValue()) {
            AITaskRegistrar.unregisterTasks(ModifierZombieAttack.class);
            AITaskRegistrar.unregisterTasks(ModifierAttackMelee.class);
            AITaskRegistrar.registerTask(new MeleeAttackAITaskModifier());
            AITaskRegistrar.registerTask(new ZombieAttackAITaskModifier());
        }
        else {
            AITaskRegistrar.unregisterTasks(ZombieAttackAITaskModifier.class);
            AITaskRegistrar.unregisterTasks(MeleeAttackAITaskModifier.class);
        }
    }

    /**
     * Registers ESM additions according to config values.
     */
    public void registerESMAdditions() {

        AITaskRegistrar.unregisterTasks(AdditionAnimalAttack.class);
        if(ModConfig.Advanced.ESMCore.Additions.useAnimalAttack.getValue()){
            AITaskRegistrar.registerTask(new AdditionAnimalAttack());
        }
        AITaskRegistrar.unregisterTasks(AdditionAnimalRetaliate.class);
        if(ModConfig.Advanced.ESMCore.Additions.useAnimalRetaliate.getValue()){
            AITaskRegistrar.registerTask(new AdditionAnimalRetaliate());
        }
        AITaskRegistrar.unregisterTasks(AdditionAvoidExplosives.class);
        if(ModConfig.Advanced.ESMCore.Additions.useAvoidExplosives.getValue()){
            AITaskRegistrar.registerTask(new AdditionAvoidExplosives());
        }
        AITaskRegistrar.unregisterTasks(AdditionDigger.class);
        if(ModConfig.Advanced.ESMCore.Additions.useDigger.getValue()){
            AITaskRegistrar.registerTask(new AdditionDigger());
        }
        AITaskRegistrar.unregisterTasks(AdditionDemolition.class);
        if(ModConfig.Advanced.ESMCore.Additions.useDemolition.getValue()){
            AITaskRegistrar.registerTask(new AdditionDemolition());
        }
        AITaskRegistrar.unregisterTasks(AdditionPillaring.class);
        if(ModConfig.Advanced.ESMCore.Additions.usePillaring.getValue()){
            AITaskRegistrar.registerTask(new AdditionPillaring());
        }
        AITaskRegistrar.unregisterTasks(AdditionGrief.class);
        if(ModConfig.Advanced.ESMCore.Additions.useGriefing.getValue()){
            AITaskRegistrar.registerTask(new AdditionGrief());
        }
    }

    /**
     * Registers ESM modifiers according to config values.
     */
    public void registerESMModifers() {

        AITaskRegistrar.unregisterTasks(ModifierAttackMelee.class);
        if(ModConfig.Advanced.ESMCore.Modifiers.useAttackMelee.getValue()){
            AITaskRegistrar.registerTask(new ModifierAttackMelee());
        }
        AITaskRegistrar.unregisterTasks(ModifierBowAttack.class);
        if(ModConfig.Advanced.ESMCore.Modifiers.useBowAttack.getValue()){
            AITaskRegistrar.registerTask(new ModifierBowAttack());
        }
        AITaskRegistrar.unregisterTasks(ModifierRangedAttack.class);
        if(ModConfig.Advanced.ESMCore.Modifiers.useRangedAttack.getValue()){
            AITaskRegistrar.registerTask(new ModifierRangedAttack());
        }
        AITaskRegistrar.unregisterTasks(ModifierZombieAttack.class);
        if(ModConfig.Advanced.ESMCore.Modifiers.useZombieAttack.getValue()){
            AITaskRegistrar.registerTask(new ModifierZombieAttack());
        }
        AITaskRegistrar.unregisterTasks(ModifierCreeperSwell.class);
        if(ModConfig.Advanced.ESMCore.Modifiers.useCreeperSwell.getValue()){
            AITaskRegistrar.registerTask(new ModifierCreeperSwell());
        }
        AITaskRegistrar.unregisterTasks(ModifierNearestAttackable.class);
        if(ModConfig.Advanced.ESMCore.Modifiers.useNearestAttackable.getValue()){
            AITaskRegistrar.registerTask(new ModifierNearestAttackable());
        }
        AITaskRegistrar.unregisterTasks(ModifierNoPanic.class);
        if(ModConfig.Advanced.ESMCore.Modifiers.useNoPanic.getValue()){
            AITaskRegistrar.registerTask(new ModifierNoPanic());
        }
        AITaskRegistrar.unregisterTasks(ModifierVillagerAvoid.class);
        if(ModConfig.Advanced.ESMCore.Modifiers.useAvoidVillager.getValue()){
            AITaskRegistrar.registerTask(new ModifierVillagerAvoid());
        }
        AITaskRegistrar.unregisterTasks(ModifierSwimming.class);
        if(ModConfig.Advanced.ESMCore.Modifiers.useSwimming.getValue()){
            AITaskRegistrar.registerTask(new ModifierSwimming());
        }
        AITaskRegistrar.unregisterTasks(ModifierWander.class);
        if(ModConfig.Advanced.ESMCore.Modifiers.useWander.getValue()){
            AITaskRegistrar.registerTask(new ModifierWander());
        }
    }

    /**
     * Unregisters ESM event hanlders according to config values.
     */
    public void unregisterESMHandlers() {

        // MainHandler - Basically disables ESM
        if(!ModConfig.Advanced.ESMCore.Handlers.useMainHandler.getValue()) {
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
            "funwayguy.epicsiegemod.handlers.MainHandler", 
            "onEntityConstruct", EntityJoinWorldEvent.class
            );
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.MainHandler", 
                "onAttachCapability", AttachCapabilitiesEvent.class
            );
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.MainHandler", 
                "onTargetSet", LivingSetAttackTargetEvent.class
            );
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.MainHandler", 
                "onLivingUpdate", LivingEvent.LivingUpdateEvent.class
            );
            // There's a 5th one but might be best not to touch it
        }

        // EntityHanlder
        if(!ModConfig.Advanced.ESMCore.Handlers.useEntityHandler.getValue()) {
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.entities.GeneralEntityHandler", 
                "onEntitySpawn", EntityJoinWorldEvent.class
            );
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.entities.GeneralEntityHandler", 
                "onEntityKilled", LivingDeathEvent.class
            );
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.entities.GeneralEntityHandler", 
                "onWorldLoad", WorldEvent.Load.class
            );
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.entities.GeneralEntityHandler", 
                "onWorldUnload", WorldEvent.Unload.class
            );
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.entities.GeneralEntityHandler", 
                "onWorldSave", WorldEvent.Save.class
            );
        }

        // PlayerHandler
        if(!ModConfig.Advanced.ESMCore.Handlers.usePlayerHandler.getValue()) {
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.entities.PlayerHandler", 
                "onLivingUpdate", LivingEvent.LivingUpdateEvent.class
            );
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.entities.PlayerHandler", 
                "onRespawn", PlayerEvent.PlayerLoggedInEvent.class
            );
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.entities.PlayerHandler", 
                "onDimensionChange", PlayerEvent.PlayerChangedDimensionEvent.class
            );
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.entities.PlayerHandler", 
                "onPlayerRespawn", PlayerEvent.PlayerRespawnEvent.class
            );
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.entities.PlayerHandler", 
                "onPlayerSleepInBed", PlayerSleepInBedEvent.class
            );
        }

        // CreeperHandler
        if(!ModConfig.Advanced.ESMCore.Handlers.useCreeperHandler.getValue()) {
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.entities.CreeperHandler", 
                "onSpawn", EntityJoinWorldEvent.class
            );
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.entities.CreeperHandler", 
                "onExplode", ExplosionEvent.Start.class
            );
        }

        // EndermanHandler
        if(!ModConfig.Advanced.ESMCore.Handlers.useEndermanHandler.getValue()) {
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.entities.EndermanHandler", 
                "onEnderTeleport", EnderTeleportEvent.class
            );
        }

        // SpiderHandler
        if(!ModConfig.Advanced.ESMCore.Handlers.useSpiderHandler.getValue()) {
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.entities.SpiderHandler", 
                "onAttacked", LivingHurtEvent.class
            );
        }

        // SkeletonHandler
        if(!ModConfig.Advanced.ESMCore.Handlers.useSkeletonHandler.getValue()) {
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.entities.SkeletonHandler", 
                "onEntitySpawn", EntityJoinWorldEvent.class
            );
        }

        // WitchHandler
        if(!ModConfig.Advanced.ESMCore.Handlers.useWitchHandler.getValue()) {
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.entities.WitchHandler", 
                "onEntitySpawn", EntityJoinWorldEvent.class
            );
        }

        // Zombie Handler
        if(!ModConfig.Advanced.ESMCore.Handlers.useZombieHandler.getValue()) {
            CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
                "funwayguy.epicsiegemod.handlers.entities.ZombieHandler", 
                "onEntityDeath", LivingDeathEvent.class
            );
        }
    }
}