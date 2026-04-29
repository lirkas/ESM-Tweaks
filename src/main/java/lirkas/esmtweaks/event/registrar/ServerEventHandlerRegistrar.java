package lirkas.esmtweaks.event.registrar;

import lirkas.esmtweaks.ESMTweaks;

/**
 * Central place for EventHandlers un/registration management.
 * * Exclusively for servers.
 */
public class ServerEventHandlerRegistrar implements IEventHandlerRegistrar {
    
    public static final ServerEventHandlerRegistrar INSTANCE = new ServerEventHandlerRegistrar();

    @Override
    public void registerAllEventHandlers() {
        ESMTweaks.logger.trace("ServerEventHandlerRegistrar registerAllEventHandlers");
    }

    @Override
    public void unregisterAllEventHandlers() {
        ESMTweaks.logger.trace("ServerEventHandlerRegistrar unregisterAllEventHandlers");
    }

    @Override
    public void registerEventHandler(Object eventHandler) {
        ESMTweaks.logger.trace("ServerEventHandlerRegistrar registerEventHandler");
    }

    @Override
    public void unregisterEventHanlder(Object eventHandler) {
        ESMTweaks.logger.trace("ServerEventHandlerRegistrar unregisterEventHanlder");
    }
}