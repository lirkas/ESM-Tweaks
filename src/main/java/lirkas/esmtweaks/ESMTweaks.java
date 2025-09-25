package lirkas.esmtweaks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;

import lirkas.esmtweaks.proxy.IProxy;


@Mod(useMetadata = true, modid = ESMTweaks.MOD_ID)
public class ESMTweaks {

	public static final String MOD_ID = "esmtweaks";
	public static final String SERVER_PROXY_CLASSNAME = "lirkas.esmtweaks.proxy.ServerProxy";
	public static final String CLIENT_PROXY_CLASSNAME = "lirkas.esmtweaks.proxy.ClientProxy";

	@Instance
	public static ESMTweaks instance;
	public static Logger logger = LogManager.getLogger();

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