package lirkas.esmtweaks.event.registrar;

import net.minecraftforge.common.MinecraftForge;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.event.handler.PlayerEventHandler;


public class ClientEventHandlerRegistrar extends CommonEventHandlerRegistrar {
    
    public static final ClientEventHandlerRegistrar INSTANCE = new ClientEventHandlerRegistrar();

    @Override
    public void registerAllEventHandlers() {
        ESMTweaks.logger.debug("ClientEventHandler registerAllEventHandlers");
        MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
    }
}
