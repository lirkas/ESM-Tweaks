package lirkas.esmtweaks.event.registrar;

import net.minecraftforge.common.MinecraftForge;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.event.handler.PlayerEventHandler;

/**
 * Central place for EventHandlers un/registration management.
 * Exclusively for clients.
 */
public class ClientEventHandlerRegistrar extends CommonEventHandlerRegistrar {
    
    public static final ClientEventHandlerRegistrar INSTANCE = new ClientEventHandlerRegistrar();

    @Override
    public void registerAllEventHandlers() {
        ESMTweaks.logger.debug("ClientEventHandlerRegistrar registerAllEventHandlers");
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
    }

    @Override
    public void unregisterAllEventHandlers() {
        ESMTweaks.logger.debug("ClientEventHandlerRegistrar unregisterAllEventHandlers");
    }

    @Override
    public void registerEventHandler(Object eventHandler) {
        ESMTweaks.logger.debug("ClientEventHandlerRegistrar registerEventHandler");
    }

    @Override
    public void unregisterEventHanlder(Object eventHandler) {
        ESMTweaks.logger.debug("ClientEventHandlerRegistrar unregisterEventHanlder");
    }
}
