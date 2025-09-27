package lirkas.esmtweaks.mods.epicsiegemod;

import net.minecraftforge.fml.common.Loader;


public class EpicSiegeMod {
    
    public static final String MOD_ID = "epicsiegemod";


    public static final boolean isLoaded() {
        return Loader.isModLoaded(MOD_ID);
    }
}
