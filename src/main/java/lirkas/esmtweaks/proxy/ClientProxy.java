package lirkas.esmtweaks.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.event.ClientEventHandler;


@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ESMTweaks.logger.debug("ClientProxy preInit");
        ClientEventHandler.INSTANCE.registerAllEventHandlers();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        ESMTweaks.logger.debug("ClientProxy init");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        ESMTweaks.logger.debug("ClientProxy postInit");
    }
}