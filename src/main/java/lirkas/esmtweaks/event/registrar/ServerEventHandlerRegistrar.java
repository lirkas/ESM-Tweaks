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
        ESMTweaks.logger.debug("ServerEventHandlerRegistrar registerAllEventHandlers");
    }

    @Override
    public void unregisterAllEventHandlers() {
        ESMTweaks.logger.debug("ServerEventHandlerRegistrar unregisterAllEventHandlers");
    }

    @Override
    public void registerEventHandler(Object eventHandler) {
        ESMTweaks.logger.debug("ServerEventHandlerRegistrar registerEventHandler");
    }

    @Override
    public void unregisterEventHanlder(Object eventHandler) {
        ESMTweaks.logger.debug("ServerEventHandlerRegistrar unregisterEventHanlder");
    }
}
