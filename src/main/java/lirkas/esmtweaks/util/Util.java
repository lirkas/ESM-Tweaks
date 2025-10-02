package lirkas.esmtweaks.util;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;


public class Util {
    
    /**
     * Generates a random number between 1 and 'maxValue' (inclusive).
     * If the number is smaller or equal to 'chance', then you win, else you lose. 
     * e.g. calling 'isLucky(20, 100, new Random())' 
     * means approximatively 20/100 chance to return true.
     *  
     * @param chance if the generated number is smaller than or equal to this value, returns true.
     * @param maxValue the highest positive number that can be generated.
     * @param rng the random source.
     * @return true if the randomly generated value is smaller or equal to 'chance', else false.
     */
    public static boolean isLucky(int chance, int maxValue, Random rng) {
        return chance >= rng.nextInt(maxValue) + 1;
    }

    /**
     * Checks if we currently are in an online multiplayer world.
     * Should only be used for client-side code.
     * 
     * @param minecraft Minecraft instance.
     * @return true if the current world is an online server.
     */
    public static boolean isWorldMultiplayerServer(Minecraft minecraft) {

        if(minecraft.getCurrentServerData() != null && !minecraft.getCurrentServerData().isOnLAN()) {
            return true;
        }

        return false;
    }

    /**
     * Retreives and returns the string matching this key if it exists in a translation resource file,
     * or 'defaultValue' if nothing is found.
     * 
     * @param languageKey The resource key.
     * @param defaultValue The string to return if nothing was found.
     * @return The text String.
     */
    public static String getTranslationOrDefault(String languageKey, String defaultValue) {

        // protects against language keys that are not defined in the .lang file
        if (!I18n.format(languageKey).equals(languageKey))
            return I18n.format(languageKey);
        else
            return defaultValue;
    }
}