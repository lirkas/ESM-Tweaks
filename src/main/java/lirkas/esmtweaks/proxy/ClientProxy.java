package lirkas.esmtweaks.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.event.registrar.ClientEventHandlerRegistrar;


@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ESMTweaks.logger.trace("ClientProxy preInit");
        ClientEventHandlerRegistrar.INSTANCE.registerAllEventHandlers();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        ESMTweaks.logger.trace("ClientProxy init");
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        ESMTweaks.logger.trace("ClientProxy postInit");
    }
}