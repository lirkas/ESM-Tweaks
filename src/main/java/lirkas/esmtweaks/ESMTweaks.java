package lirkas.esmtweaks;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import lirkas.esmtweaks.proxy.IProxy;


@Mod(useMetadata = true, modid = ESMTweaks.MOD_ID, 
		version = ESMTweaks.VERSION, guiFactory = ESMTweaks.CONFIG_FACTORY_CLASSNAME)
public class ESMTweaks {

	public static final String MOD_ID = "esmtweaks";
	public static final String VERSION = "%VERSION%";
	public static final String SERVER_PROXY_CLASSNAME = "lirkas.esmtweaks.proxy.ServerProxy";
	public static final String CLIENT_PROXY_CLASSNAME = "lirkas.esmtweaks.proxy.ClientProxy";
	public static final String CONFIG_FACTORY_CLASSNAME = "lirkas.esmtweaks.config.ConfigScreenFactory";

	@Instance
	public static ESMTweaks instance;
	public static Logger logger = (Logger) LogManager.getLogger(MOD_ID);

	@SidedProxy(serverSide = SERVER_PROXY_CLASSNAME, clientSide = CLIENT_PROXY_CLASSNAME)
	public static IProxy proxy;


	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ESMTweaks.proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		ESMTweaks.proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		ESMTweaks.proxy.postInit(event);
	}

	@EventHandler
	public void serverAboutToStart(FMLServerAboutToStartEvent event) {
		ESMTweaks.proxy.serverAboutToStart(event);
	}
}