package lirkas.esmtweaks.config;

import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.fml.client.config.GuiConfig;

import lirkas.esmtweaks.ESMTweaks;


public class ModConfigGUIScreen extends GuiConfig {

    public ModConfigGUIScreen(GuiScreen parentScreen) {
        super(parentScreen, 
        ModConfig.getConfigElements(parentScreen), ESMTweaks.MOD_ID, false, false, "ESM Tweaks Config Screen");
    }
}