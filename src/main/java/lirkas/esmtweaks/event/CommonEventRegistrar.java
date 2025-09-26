package lirkas.esmtweaks.event;

import net.minecraftforge.common.MinecraftForge;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.event.handler.EntityEventHandler;


public class CommonEventRegistrar implements IEventRegistrar {

    public static final CommonEventRegistrar INSTANCE = new CommonEventRegistrar();

    @Override
    public void registerAllEventHandlers() {
        ESMTweaks.logger.debug("CommonEventHandler registerAllEventHandlers");
        MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
    }

    @Override
    public void unregisterAllEventHandlers() {
        ESMTweaks.logger.debug("CommonEventHandler unregisterAllEventHandlers");
    }

    @Override
    public void registerEventHandler(Object eventHandler) {
        ESMTweaks.logger.debug("CommonEventHandler registerEventHandler");
        MinecraftForge.EVENT_BUS.register(eventHandler);
    }

    @Override
    public void unregisterEventHanlder(Object eventHandler) {
        ESMTweaks.logger.debug("CommonEventHandler unregisterEventHanlder");
        MinecraftForge.EVENT_BUS.unregister(eventHandler);
    }
}