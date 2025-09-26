package lirkas.esmtweaks.event;

import net.minecraftforge.common.MinecraftForge;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.util.HarvestUtil;


public class ClientEventRegistrar extends CommonEventHandler {
    
    public static final ClientEventRegistrar INSTANCE = new ClientEventRegistrar();

    @Override
    public void registerAllEventHandlers(){
        
        ESMTweaks.logger.debug("ClientEventHandler registerAllEventHandlers");
        MinecraftForge.EVENT_BUS.register(new HarvestUtil.EventHandler());
    }
}
