package lirkas.esmtweaks.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresWorldRestart;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import lirkas.esmtweaks.ESMTweaks;

/**
 * The categories and option order in the config GUI are alphabetically sorted (ascending), 
 * based on the name assigned to them (from the annotation not the variable or class name).
 */
@Config(modid = ESMTweaks.MOD_ID)
public class ModConfig {

    @Name("AI")
    public static final AICategory AI = new AICategory();

    @Name("Misc")
    @Comment({
        "Debbugging and other miscellaneous options."
    })
    public static final MiscCategory MISC = new MiscCategory();


    public static class AICategory {

        @Name("Alternative Digging AI")
        @Comment({
            "Replace ESM Digging AI by this mod's one. " +
            "This is required for the other AI options to take effect.",
            "Can only be changed when the server/world is not running."
        })
        @RequiresWorldRestart
        public boolean isAltDiggingAIEnabled = true;

        @Name("Digging AI Must Use Correct Tool")
        @Comment({
            "Mobs that can break blocks will only be able to do so " +
            "if they have the right tool for it." +
            "(They cannot break blocks that require specific tool class/level " +
            "without such tool). " +
            "Disabling this option allows them to break any block by hand.",
            "Requires [ Use Alternative Digging AI ] to be enabled."
        })
        public boolean mustHaveCorrectTool = true;

        @Name("Digging AI Check Both Hands")
        @Comment({
            "Checks digging mob's main and off hand if a tool is required to break the block.",
            "Requires [ Digging AI Must Use Correct Tool ] to be enabled."
        })
        public boolean shouldCheckBothHands = true;

        @Name("Digging AI Use ESM Default Break Check")
        @Comment({
            "Uses Epic Siege Mod original verification method to check if the " +
            "mob is allowed to mine the block or not.",
            "Takes priority over other settings."
        })
        public boolean useESMDefaultHarvestCheck = false;

        @Name("AI Task Update On Death")
        @Comment({
            "When a mob that has an AI dies, it gets one last task update. " +
            "This allows for instance for blocks to be visually reset " + 
            "to their default mined state, but it could cause issues with other AI types." +
            "Takes priority over other settings."
        })
        public boolean updateAITaskOnDeath = true;

        @Name("Digging AI Get Pickaxe On Spawn")
        @Comment({
            "Disable this to not allow this mod to give extra chance for digging mobs to " +
            "receive a pickaxe. They can still get them from minecraft vanilla behavior " +
            "or other mods, regardless of this setting. " +
            "This is simply 'another chance' to be given one."
        })
        public boolean shouldBeGivenTool = true;

        @Name("Digging AI Pickaxe Chance")
        @Comment({
            "The probability for the digging mob to be given a pickaxe when spawning. " +
            "0 = never gets the chance, 100 = always gets it. ",
            "Requires [ Digging AI Get Pickaxe On Spawn ] to be enabled."
        })
        @RangeInt(min = 0, max = 100)
        public int toolChance = 25;

        @Name("Digging AI Override Tool")
        @Comment({
            "If this is enabled and the digging mob gets the chance to receive the pickaxe on spawn, " +
            "then the pickaxe replaces whatever that mob was holding in its main hand, " + 
            "else the pickaxe is not received and the main hand item is kept.",
            "Requires [ Digging AI Get Pickaxe On Spawn ] to be enabled."
        })
        public boolean shouldOverrideTool = false;
    }
    
    public static class MiscCategory {

        @Name("Debug Mode")
        @Comment({
            "Debug mode toogle.",
            "This must be enabled for the other options in this menu to work."
        })
        public boolean enableDebug = false;

        @Name("Show Block Infos")
        @Comment({
            "When left-clicking a block, some of its properties " +
            "and infos related to block breaking will be output to the chat.",
            "Requires [ Debug Mode ] to be enabled."
        })
        public boolean displayBlockInfoInChat = false;

        @Name("Show Block Break Info Message")
        @Comment({
            "When left-clicking a block, a message saying if the block can " +
            "be broken with the current held tool (default to mainhand) will be shown.",
            "Requires [ Debug Mode ] to be enabled."
        })
        public boolean displayCanBreakBlockMessage = false;

        @Name("Use OffHand Item For Checks")
        @Comment({
            "The offhand held item will be used instead " + 
            "of main hand one for debug messages checks. " +
            "This makes it easier for left clicking in creative mode " +
            "with a less destructive item such as a sword."
        })
        public boolean useOffHandItem = false;
    }

    @EventBusSubscriber(modid = ESMTweaks.MOD_ID)
    public static class EventHandler {
        /**
         * Makes sure all the config variables in the config file are saved whenever the user
         * changes them from the game.
         * @param event
         */
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent event) {

            if(event.getModID().equals(ESMTweaks.MOD_ID)) {

                ESMTweaks.logger.debug("Configuration changed");
                ConfigManager.sync(ESMTweaks.MOD_ID, Config.Type.INSTANCE);
            }
        }
    }
}