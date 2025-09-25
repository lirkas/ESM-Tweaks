package lirkas.esmtweaks.mods.toolprogression;

import net.minecraftforge.fml.common.Loader;


public class ToolProgression {
    
    public static final String MOD_ID = "toolprogression";


    public static final boolean isLoaded(){
    
        return Loader.isModLoaded(MOD_ID);
      
    }
}