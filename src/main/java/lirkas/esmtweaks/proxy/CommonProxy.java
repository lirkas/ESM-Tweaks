package lirkas.esmtweaks.proxy;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;

import funwayguy.epicsiegemod.ai.additions.AdditionDigger;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.ai.addition.DiggingAITaskAddition;
import lirkas.esmtweaks.ai.registrar.AITaskRegistrar;
import lirkas.esmtweaks.config.ModConfig;
import lirkas.esmtweaks.event.registrar.CommonEventHandlerRegistrar;


public abstract class CommonProxy implements IProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        ESMTweaks.logger = event.getModLog();
        ESMTweaks.logger.debug("CommonProxy preInit");

        // this is replaced with another event handler that wraps it
        CommonEventHandlerRegistrar.INSTANCE.unregisterEventHanlder(
            "funwayguy.epicsiegemod.handlers.MainHandler", 
            "onEntityConstruct", EntityJoinWorldEvent.class);
        CommonEventHandlerRegistrar.INSTANCE.registerAllEventHandlers();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        ESMTweaks.logger.debug("CommonProxy init");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        ESMTweaks.logger.debug("CommonProxy postInit");
    }

    @Override
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        ESMTweaks.logger.debug("CommonProxy serverAboutToStart");
        
        if(ModConfig.AI.isAltDiggingAIEnabled) {
            AITaskRegistrar.unregisterTasks(AdditionDigger.class);
            AITaskRegistrar.registerTask(new DiggingAITaskAddition());
        }
        else {
            AITaskRegistrar.unregisterTasks(DiggingAITaskAddition.class);
            AITaskRegistrar.registerTask(new AdditionDigger());
        }
    }
}