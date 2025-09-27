package lirkas.esmtweaks.util;

import java.util.Random;


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
}