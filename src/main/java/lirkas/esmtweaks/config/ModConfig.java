package lirkas.esmtweaks.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;

import lirkas.esmtweaks.util.Util;

/**
 * All the properties must be initialized as early as possible, 
 * and before any of them are used.
 * 
 * Config option name and comment values may be set from the 
 * language file 'assets/esmtweaks/lang/xx_yy.lang'
 */
public class ModConfig {
    
    public static final String CONFIG_VERSION = "1.0";
    public static final String CONFIG_FILEPATH = "config/.esmtweaks.cfg";

    public static Configuration configuration;
    static {
        configuration = new Configuration(new File(CONFIG_FILEPATH), CONFIG_VERSION);
        configuration.load();
    }

    // AI Category Options
    public static class AI {

        public static final String CATEGORY_NAME = "AI";
        public static ConfigCategory configCategory = new ConfigCategory(CATEGORY_NAME);

        // General AI Settings SubCategory Options
        public static class General {

            public static final String CATEGORY_NAME = AI.CATEGORY_NAME + ".General";
            public static ConfigCategory configCategory = new ConfigCategory(CATEGORY_NAME, AI.configCategory);

            public static ConfigProperty<Boolean> updateAITaskOnDeath = new ConfigProperty<Boolean>(
                "updateAITaskOnDeath", CATEGORY_NAME, false
            );
            static {
                updateAITaskOnDeath.setComment(
                    "When a mob that has an AI dies, it gets one last task update. " +
                    "If the tweaked digging AI is used, this allows for partially mined blocks to be visually reset " + 
                    "to their unmined state when the mob dies. " +
                    "Enabling this option may or may not lead to unexpected issues for other AI types."
                );
            }
        }

        // Digging AI SubCategory Options
        public static class Digging {

            public static final String CATEGORY_NAME = AI.CATEGORY_NAME + ".Digging";
            public static ConfigCategory configCategory = new ConfigCategory(CATEGORY_NAME, AI.configCategory);

            public static ConfigProperty<Boolean> useTweakedAI = new ConfigProperty<Boolean>(
                "useTweakedAI", CATEGORY_NAME, true
            );
            static {
                useTweakedAI.getProperty()
                    .setRequiresWorldRestart(true);
                useTweakedAI.setComment(
                    "Replaces ESM Digging AI by this mod's one (which is based on it). " +
                    "This is required for other AI options to take effect.\n" +
                    "Can only be changed when the server/world is not running."
                );
            }

            public static ConfigProperty<Boolean> mustHaveCorrectTool = new ConfigProperty<Boolean>(
                "mustHaveCorrectTool", CATEGORY_NAME, true
            );
            static {
                mustHaveCorrectTool.setRequiredOptions(useTweakedAI);
                mustHaveCorrectTool.setComment(
                    "Mobs that can break blocks will only be able to do so " +
                    "if they have the right tool for it. " +
                    "(They cannot break blocks that require specific tool class/level " +
                    "without such tool, as defined by ToolProgression mod). " +
                    "Disabling this option allows them to break any 'breakable' block by hand."
                );
            }

            public static ConfigProperty<Boolean> checkBothHands = new ConfigProperty<Boolean>(
                "checkBothHands", CATEGORY_NAME, true
            );
            static {
                checkBothHands.setRequiredOptions(mustHaveCorrectTool);
                checkBothHands.setComment(
                    "Checks digging mob's main and off hand if a tool is required " +
                    "to break the block."
                );
            }

            public static ConfigProperty<Boolean> legacyHarvestCheck = new ConfigProperty<Boolean>(
                "legacyHarvestCheck", CATEGORY_NAME, false
            );
            static {
                legacyHarvestCheck.setRequiredOptions(useTweakedAI);
                legacyHarvestCheck.setComment(
                    "Uses Epic Siege Mod original verification method to check if the " +
                    "mob is allowed to mine the block or not.\n" +
                    "Takes priority over other settings."
                );
            }

            public static ConfigProperty<Boolean> canGetExtraTool = new ConfigProperty<Boolean>(
                "shouldGetExtraTool", CATEGORY_NAME, false
            );
            static {
                canGetExtraTool.setRequiredOptions(useTweakedAI);
                canGetExtraTool.setComment(
                    "When enabled, diggings mob may get a chance to obtain a pickaxe when spawned. " +
                    "Even if this setting is disabled, they can still get one or other items that way " +
                    "from minecraft vanilla behavior or other mods. " +
                    "This setting only gives them 'another chance' to get one."
                );
            }

            public static ConfigProperty<Boolean> extraToolOverride = new ConfigProperty<Boolean>(
                "overrideTool", CATEGORY_NAME, false
            );
            static {
                extraToolOverride.setRequiredOptions(canGetExtraTool);
                extraToolOverride.setComment(
                    "If this is enabled and digging mobs gets the chance " +
                    "to receive the pickaxe on spawn, " +
                    "then the pickaxe replaces whatever that mob was holding in its main hand, " +
                    "else the pickaxe is not received and the main hand item is kept."
                );
            }

            public static ConfigProperty<Integer> extraToolChance = new ConfigProperty<Integer>(
                "extraToolChance", CATEGORY_NAME, 0
            );
            static {
                extraToolChance.getProperty()
                    .setMinValue(0)
                    .setMaxValue(100);
                extraToolChance.setRequiredOptions(canGetExtraTool);
                extraToolChance.setComment(
                    "The probability for the digging mob to be given a pickaxe when spawning.\n" +
                    "0 = never gets one, 100 = always gets it."
                );
            }
        }
    }

    // Debug Category Options
    public static class Debug {

        public static final String CATEGORY_NAME = "Debug";
        public static ConfigCategory configCategory = new ConfigCategory(CATEGORY_NAME);

        public static ConfigProperty<Boolean> enableDebug = new ConfigProperty<Boolean>(
            "enableDebug", CATEGORY_NAME, false
        );
        static {
            enableDebug.setComment(
                "Debug mode toogle.\n" +
                "Must be enabled for options in this category to work."
            );
        }

        public static ConfigProperty<Boolean> outputBlockInfoInChat = new ConfigProperty<Boolean>(
            "displayBlockInfoInChat", CATEGORY_NAME, false
        );
        static {
            outputBlockInfoInChat.setRequiredOptions(enableDebug);
            outputBlockInfoInChat.setComment(
                "When left-clicking a block, some of its properties " +
                "and infos related to block breaking will be output to the chat."
            );
        }

        public static ConfigProperty<Boolean> showCanBreakBlockMessage = new ConfigProperty<Boolean>(
            "displayCanBreakBlockMessage", CATEGORY_NAME, false
        );
        static {
            showCanBreakBlockMessage.setRequiredOptions(enableDebug);
            showCanBreakBlockMessage.setComment(
                "When left-clicking a block, a message saying if the block can " +
                "be broken with the current held tool (defaults to mainhand) will be shown."
            );
        }
        
        public static ConfigProperty<Boolean> useOffhandItemForChecks = new ConfigProperty<Boolean>(
            "useOffHandItem", CATEGORY_NAME, false
        );
        static {
            useOffhandItemForChecks.setComment(
                "The offhand held item will be used instead " +
                "of main hand one for debug messages checks. " +
                "This makes it easier for left clicking in creative mode " +
                "with less destructive items such as swords."
            );
        }
    }

    /**
     * Config initialization.
     */
    public static void init() {

        setupConfigCategories();
        setupConfigProperties();
    }

    /**
     * Setups config categories. Should only be done once, during game startup.
     */
    public static void setupConfigCategories() {

        configuration.setCategoryLanguageKey(
            AI.CATEGORY_NAME, ConfigProperty.LANG_KEY_PREFIX + ".category." + AI.configCategory.getName());
        configuration.setCategoryLanguageKey(
            AI.General.CATEGORY_NAME, ConfigProperty.LANG_KEY_PREFIX + ".category." + AI.General.configCategory.getName());
        configuration.setCategoryLanguageKey(
            AI.Digging.CATEGORY_NAME, ConfigProperty.LANG_KEY_PREFIX + ".category." + AI.Digging.configCategory.getName());
        configuration.setCategoryLanguageKey(
            Debug.CATEGORY_NAME, ConfigProperty.LANG_KEY_PREFIX + ".category." + Debug.configCategory.getName());
    }

    /**
     * Setups config properties, and their display order in the GUI.
     * Should only be done once, during startup.
     */
    public static void setupConfigProperties() {

        ConfigProperty.setupPropertiesFromClass(AI.class, configuration);
        ConfigProperty.setupPropertiesFromClass(AI.General.class, configuration);
        ConfigProperty.setupPropertiesFromClass(AI.Digging.class, configuration);
        ConfigProperty.setupPropertiesFromClass(Debug.class, configuration);

        // setting up the order in which settings are displayed in the Config GUI
        List<String> propertiesNames = new ArrayList<>();

        // General AI options ordering
        propertiesNames = new ArrayList<>();
        propertiesNames.add(AI.General.updateAITaskOnDeath.getName());

        configuration.setCategoryPropertyOrder(AI.General.CATEGORY_NAME, propertiesNames);
        
        // Digging AI options ordering
        propertiesNames = new ArrayList<>();
        propertiesNames.add(AI.Digging.useTweakedAI.getName());
        propertiesNames.add(AI.Digging.mustHaveCorrectTool.getName());
        propertiesNames.add(AI.Digging.checkBothHands.getName());
        propertiesNames.add(AI.Digging.canGetExtraTool.getName());
        propertiesNames.add(AI.Digging.extraToolOverride.getName());
        propertiesNames.add(AI.Digging.extraToolChance.getName());
        propertiesNames.add(AI.Digging.legacyHarvestCheck.getName());

        configuration.setCategoryPropertyOrder(AI.Digging.CATEGORY_NAME, propertiesNames);
        
        // Debug options ordering
        propertiesNames = new ArrayList<>();
        propertiesNames.add(Debug.enableDebug.getName());
        propertiesNames.add(Debug.showCanBreakBlockMessage.getName());
        propertiesNames.add(Debug.outputBlockInfoInChat.getName());
        propertiesNames.add(Debug.useOffhandItemForChecks.getName());

        configuration.setCategoryPropertyOrder(Debug.CATEGORY_NAME, propertiesNames);

        // saving previous changes
        configuration.save();
    }

    /**
     * Gets elements to be displayed in the Config GUI (in-game).
     * This is normally be called by this mod config screen class each time its Config GUI is opened.
     * 
     * @param parentScreen The screen from which the config is going to be shown (usually mod config menu).
     */
    public static List<IConfigElement> getConfigElements(GuiScreen parentScreen) {
        
        List<IConfigElement> elements = new ArrayList<>();

        // AI settings and any other ones that cant be changed from the client are hidden on online worlds
        if(!Util.isWorldMultiplayerServer(parentScreen.mc)) {
            elements.add(new ConfigElement(configuration.getCategory(AI.CATEGORY_NAME)));
        }
        elements.add(new ConfigElement(configuration.getCategory(Debug.CATEGORY_NAME)));

        return elements;
    }
}