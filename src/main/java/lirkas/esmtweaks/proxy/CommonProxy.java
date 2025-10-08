package lirkas.esmtweaks.proxy;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;

import funwayguy.epicsiegemod.ai.additions.AdditionDigger;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.ai.addition.DiggingAITaskAddition;
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
        
        // unregistering digging AI tasks
        AITaskRegistrar.unregisterTasks(DiggingAITaskAddition.class);
        AITaskRegistrar.unregisterTasks(AdditionDigger.class);

        if(ModConfig.AI.Digging.useTweakedAI.getValue()) {
            AITaskRegistrar.registerTask(new DiggingAITaskAddition());
        }
        else {
            AITaskRegistrar.registerTask(new AdditionDigger());
        }
    }
}