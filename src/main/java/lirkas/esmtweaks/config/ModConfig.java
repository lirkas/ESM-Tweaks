package lirkas.esmtweaks.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.ai.AltEntityAIAttackMelee;
import lirkas.esmtweaks.ai.AltEntityAIDigging;
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
    public static final String CONFIG_FILEPATH = "config/esmtweaks.cfg";
    public static final String MF_LOGLEVEL = "LogLevel";

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
                "updateAITaskOnDeath", CATEGORY_NAME, true
            );
            static {
                updateAITaskOnDeath.setComment(
                    "When a mob that has an AI dies, it gets one last task update. " +
                    "If the tweaked digging AI is used, this allows for partially mined blocks to be visually reset " + 
                    "to their unmined state when the mob dies. " +
                    "Enabling this option may or may not lead to unexpected issues for other AI types."
                );
            }

            public static ConfigProperty<Boolean> disableXRay = new ConfigProperty<Boolean>(
                "disableXRay", CATEGORY_NAME, false
            );
            static {
                disableXRay.setComment(
                    "Disables Xray Vision feature for mobs and ignores ESM 'Xray Distance' config values.\n" +
                    "Takes effect for newly spawned/created entities or on server/world restart."
                );
            }

            public static ConfigProperty<Boolean> copyNavigatorProperties = new ConfigProperty<Boolean>(
                "copyNavigatorProperties", CATEGORY_NAME, false
            );
            static {
                copyNavigatorProperties.setComment(
                    "Ensures that certain mobs original properties (ability to swim or interact with doors) " +
                    "will be carried over during the entity creation process.\n" +
                    "Takes effect for newly spawned/created entities or on server/world restart."
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
                    "When enabled, digging mobs may get a chance to obtain a pickaxe when spawned. " +
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
                    "If this is enabled and digging mobs get the chance " +
                    "to receive the pickaxe on spawn, " +
                    "then the pickaxe replaces whatever that mob was holding in its main hand, " +
                    "else the pickaxe is not received and the main hand item is kept."
                );
            }

            public static ConfigProperty<Integer> extraToolChance = new ConfigProperty<Integer>(
                "extraToolChance", CATEGORY_NAME, 25
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

            public static ConfigProperty<Integer> searchBlockInterval = new ConfigProperty<Integer>(
                "searchBlockInterval", CATEGORY_NAME, 1
            );
            static {
                searchBlockInterval.getProperty()
                    .setMinValue(1)
                    .setMaxValue(1200);
                searchBlockInterval.setRequiredOptions(useTweakedAI);
                searchBlockInterval.setComment(
                    "Defines how many ticks the mob waits between each attempt to find a block to break. " +
                    "Even if the block cannot be broken, each check counts as an attempt, which means " +
                    "the mob waits again, and repeats this process until he finds a valid block or dies."
                );
            }

            public static ConfigProperty<Integer> harvestCheckInterval = new ConfigProperty<Integer>(
                "harvestCheckInterval", CATEGORY_NAME, 20
            );
            static {
                harvestCheckInterval.getProperty()
                    .setMinValue(1)
                    .setMaxValue(600);
                harvestCheckInterval.setRequiredOptions(useTweakedAI);
                harvestCheckInterval.setComment(
                    "Defines how often (in ticks) the periodical check to determine if the mob " +
                    "is still capable of harvesting the currently mined block is run. " +
                    "As an example, if a mob were to break its tool while mining a block that requires it, " +
                    "he is going to continue mining it (at reduced speed) for the set time."
                );
            }

            public static ConfigProperty<Boolean> diggingSounds = new ConfigProperty<Boolean>(
                "diggingSounds", CATEGORY_NAME, true
            );
            static {
                diggingSounds.setRequiredOptions(useTweakedAI);
                diggingSounds.setComment(
                    "Enables or disables digging sounds made by mobs (while they are mining a block). " +
                    "This does not affect sounds made when the block breaks."
                );
            }

            public static ConfigProperty<Double> digSpeedMultiplier = new ConfigProperty<Double>(
                "digSpeedMultiplier", CATEGORY_NAME, 1.00
            );
            static {
                digSpeedMultiplier.getProperty()
                    .setMinValue(0.0)
                    .setMaxValue(256.0);
                digSpeedMultiplier.setRequiredOptions(useTweakedAI);
                digSpeedMultiplier.setComment(
                    "Digging speed multipler for mobs. " +
                    "It directly multiplies the speed at which mobs break a block by this value. " +
                    "A block that would normally take 12.5 seconds to break by a specific mob will " +
                    "take 25 seconds instead if this value is set to 0.5. " +
                    "Blocks with 0 hardness are unaffected by this setting.\n" +
                    "0 = instant mining, 1.0 = normal speed, 2.0 = 2x faster"
                );
            }
        }

        // Attack AI SubCategory Options
        public static class Attack {

            public static final String CATEGORY_NAME = AI.CATEGORY_NAME + ".Attack";
            public static ConfigCategory configCategory = new ConfigCategory(CATEGORY_NAME, AI.configCategory);

            // Melee Attack AI SubCategory Options
            public static class Melee {
    
                public static final String CATEGORY_NAME = Attack.CATEGORY_NAME + ".Melee";
                public static ConfigCategory configCategory = new ConfigCategory(CATEGORY_NAME, AI.Attack.configCategory);

                public static ConfigProperty<Boolean> useTweakedAI = new ConfigProperty<Boolean>(
                    "useTweakedAI", CATEGORY_NAME, true
                );
                static {
                    useTweakedAI.getProperty()
                        .setRequiresWorldRestart(true);
                    useTweakedAI.setComment(
                        "Replaces ESM AttackMelee AI by this mod's one (which is based on). " +
                        "Only affects mobs that had this AI changed by ESM. " +
                        "This is required for other AI options to take effect.\n" +
                        "Can only be changed when the server/world is not running."
                    );
                }

                public static ConfigProperty<Boolean> forceLongMemory = new ConfigProperty<Boolean>(
                    "forceLongMemory", CATEGORY_NAME, true
                );
                static {
                    forceLongMemory.setRequiredOptions(useTweakedAI);
                    forceLongMemory.setComment(
                        "If enabled, the type of memory will be forced to 'long' for all mobs with this AI. " +
                        "If disabled, uses the mob's original memory type defined by minecraft " +
                        "but might negatively impacts ESM 'Xray Distance' feature. " +
                        "This affects attack rate, AI updates frequency, and potentially other things.\n" +
                        "Fully takes effect on newly spawned/created entities or on server/world restart."
                    );
                }

                public static ConfigProperty<Boolean> useCustomAttackDelay = new ConfigProperty<Boolean>(
                    "useCustomAttackDelay", CATEGORY_NAME, true
                );
                static {
                    useCustomAttackDelay.setRequiredOptions(useTweakedAI, forceLongMemory);
                    useCustomAttackDelay.setComment(
                        "If enabled, allows for custom attack delays to be defined. " +
                        "A random delay in ticks between min and max is set after each attack. " +
                        "If disabled, it uses Minecraft's default delay of 20 ticks per attack. " +
                        "Delays dont always exactly match with game ticks.\n" +
                        "Min and max delays are set from the next 2 options. "
                    );
                }

                public static ConfigProperty<Integer> minAttackDelay = new ConfigProperty<Integer>(
                    "minAttackDelay", CATEGORY_NAME, 10
                );
                static {
                    minAttackDelay.getProperty()
                        .setMinValue(5)
                        .setMaxValue(100);
                    minAttackDelay.setRequiredOptions(useTweakedAI, useCustomAttackDelay);
                    minAttackDelay.setComment(
                        "Defines the minimum amount of ticks a mob waits between each attack.\n" +
                        "20 ticks = 1 second"
                    );
                }

                public static ConfigProperty<Integer> maxAttackDelay = new ConfigProperty<Integer>(
                    "maxAttackDelay", CATEGORY_NAME, 20
                );
                static {
                    maxAttackDelay.getProperty()
                        .setMinValue(5)
                        .setMaxValue(100);
                    maxAttackDelay.setRequiredOptions(useTweakedAI, useCustomAttackDelay);
                    maxAttackDelay.setComment(
                        "Defines the maximum amount of ticks a mob waits between each attack.\n" +
                        "20 ticks = 1 second"
                    );
                }
            }
        }
    }

    // Advanced Category Options
    public static class Advanced {

        public static final String CATEGORY_NAME = "Advanced";
        public static ConfigCategory configCategory = new ConfigCategory(CATEGORY_NAME);

        // ESM Core Features SubCategory Options
        public static class ESMCore {

            public static final String CATEGORY_NAME = Advanced.CATEGORY_NAME + ".ESMCore";
            public static ConfigCategory configCategory = new ConfigCategory(CATEGORY_NAME, Advanced.configCategory);

            // ESM Addition SubCategory Options
            public static class Additions {

                public static final String CATEGORY_NAME = ESMCore.CATEGORY_NAME + ".Additions";
                public static ConfigCategory configCategory = new ConfigCategory(CATEGORY_NAME, ESMCore.configCategory);

                public static ConfigProperty<Boolean> useAnimalAttack = new ConfigProperty<Boolean>(
                    "useAnimalAttack", CATEGORY_NAME, true
                );
                static {
                    useAnimalAttack.getProperty().setRequiresWorldRestart(true);
                    useAnimalAttack.setComment(
                        "No description."
                    );
                }

                public static ConfigProperty<Boolean> useAnimalRetaliate = new ConfigProperty<Boolean>(
                    "useAnimalRetaliate", CATEGORY_NAME, true
                );
                static {
                    useAnimalRetaliate.getProperty().setRequiresWorldRestart(true);
                    useAnimalRetaliate.setComment(
                        "No description."
                    );
                }

                public static ConfigProperty<Boolean> useAvoidExplosives = new ConfigProperty<Boolean>(
                    "useAvoidExplosives", CATEGORY_NAME, true
                );
                static {
                    useAvoidExplosives.getProperty().setRequiresWorldRestart(true);
                    useAvoidExplosives.setComment(
                        "No description."
                    );
                }

                public static ConfigProperty<Boolean> useDemolition = new ConfigProperty<Boolean>(
                    "useDemolition", CATEGORY_NAME, true
                );
                static {
                    useDemolition.getProperty().setRequiresWorldRestart(true);
                    useDemolition.setComment(
                        "No description."
                    );
                }

                public static ConfigProperty<Boolean> useDigger = new ConfigProperty<Boolean>(
                    "useDigger", CATEGORY_NAME, true
                );
                static {
                    useDigger.getProperty().setRequiresWorldRestart(true);
                    useDigger.setComment(
                        "No description."
                    );
                }

                public static ConfigProperty<Boolean> useGriefing = new ConfigProperty<Boolean>(
                    "useGriefing", CATEGORY_NAME, true
                );
                static {
                    useGriefing.getProperty().setRequiresWorldRestart(true);
                    useGriefing.setComment(
                        "No description."
                    );
                }

                public static ConfigProperty<Boolean> usePillaring = new ConfigProperty<Boolean>(
                    "usePillaring", CATEGORY_NAME, true
                );
                static {
                    usePillaring.getProperty().setRequiresWorldRestart(true);
                    usePillaring.setComment(
                        "No description."
                    );
                }
            }
            
            // ESM Modifiers SubCategory Options
            public static class Modifiers {

                public static final String CATEGORY_NAME = ESMCore.CATEGORY_NAME + ".Modifiers";
                public static ConfigCategory configCategory = new ConfigCategory(CATEGORY_NAME, ESMCore.configCategory);

                public static ConfigProperty<Boolean> useAttackMelee = new ConfigProperty<Boolean>(
                    "useAttackMelee", CATEGORY_NAME, true
                );
                static {
                    useAttackMelee.getProperty().setRequiresWorldRestart(true);
                    useAttackMelee.setComment(
                        "No description."
                    );
                }

                public static ConfigProperty<Boolean> useRangedAttack = new ConfigProperty<Boolean>(
                    "useRangedAttack", CATEGORY_NAME, true
                );
                static {
                    useRangedAttack.getProperty().setRequiresWorldRestart(true);
                    useRangedAttack.setComment(
                        "No description."
                    );
                }

                public static ConfigProperty<Boolean> useBowAttack = new ConfigProperty<Boolean>(
                    "useBowAttack", CATEGORY_NAME, true
                );
                static {
                    useBowAttack.getProperty().setRequiresWorldRestart(true);
                    useBowAttack.setComment(
                        "No description."
                    );
                }

                public static ConfigProperty<Boolean> useNearestAttackable = new ConfigProperty<Boolean>(
                    "useNearestAttackable", CATEGORY_NAME, true
                );
                static {
                    useNearestAttackable.getProperty().setRequiresWorldRestart(true);
                    useNearestAttackable.setComment(
                        "No description."
                    );
                }

                public static ConfigProperty<Boolean> useZombieAttack = new ConfigProperty<Boolean>(
                    "useZombieAttack", CATEGORY_NAME, true
                );
                static {
                    useZombieAttack.getProperty().setRequiresWorldRestart(true);
                    useZombieAttack.setComment(
                        "No description."
                    );
                }

                public static ConfigProperty<Boolean> useCreeperSwell = new ConfigProperty<Boolean>(
                    "useCreeperSwell", CATEGORY_NAME, true
                );
                static {
                    useCreeperSwell.getProperty().setRequiresWorldRestart(true);
                    useCreeperSwell.setComment(
                        "No description."
                    );
                }

                public static ConfigProperty<Boolean> useNoPanic = new ConfigProperty<Boolean>(
                    "useNoPanic", CATEGORY_NAME, true
                );
                static {
                    useNoPanic.getProperty().setRequiresWorldRestart(true);
                    useNoPanic.setComment(
                        "No description."
                    );
                }

                public static ConfigProperty<Boolean> useSwimming = new ConfigProperty<Boolean>(
                    "useSwimming", CATEGORY_NAME, true
                );
                static {
                    useSwimming.getProperty().setRequiresWorldRestart(true);
                    useSwimming.setComment(
                        "No description."
                    );
                }

                public static ConfigProperty<Boolean> useAvoidVillager = new ConfigProperty<Boolean>(
                    "useAvoidVillager", CATEGORY_NAME, true
                );
                static {
                    useAvoidVillager.getProperty().setRequiresWorldRestart(true);
                    useAvoidVillager.setComment(
                        "No description."
                    );
                }

                public static ConfigProperty<Boolean> useWander = new ConfigProperty<Boolean>(
                    "useWander", CATEGORY_NAME, true
                );
                static {
                    useWander.getProperty().setRequiresWorldRestart(true);
                    useWander.setComment(
                        "No description."
                    );
                }
            }
            // ESM Handlers SubCategory Options
            public static class Handlers {

                public static final String CATEGORY_NAME = ESMCore.CATEGORY_NAME + ".Handlers";
                public static ConfigCategory configCategory = new ConfigCategory(CATEGORY_NAME, ESMCore.configCategory);

                public static ConfigProperty<Boolean> useMainHandler = new ConfigProperty<Boolean>(
                    "useMainHandler", CATEGORY_NAME, true
                );
                static {
                    useMainHandler.getProperty().setRequiresMcRestart(true);
                    useMainHandler.setComment(
                        "Affects various important ESM features.\n" +
                        "Disabling this may disable a lot of features."
                    );
                }

                public static ConfigProperty<Boolean> useEntityHandler = new ConfigProperty<Boolean>(
                    "useEntityHandler", CATEGORY_NAME, true
                );
                static {
                    useEntityHandler.getProperty().setRequiresMcRestart(true);
                    useEntityHandler.setComment(
                        "No description."
                    );
                }

                public static ConfigProperty<Boolean> usePlayerHandler = new ConfigProperty<Boolean>(
                    "usePlayerHandler", CATEGORY_NAME, true
                );
                static {
                    usePlayerHandler.getProperty().setRequiresMcRestart(true);
                    usePlayerHandler.setComment(
                        "No description."
                    );
                }

                public static ConfigProperty<Boolean> useCreeperHandler = new ConfigProperty<Boolean>(
                    "useCreeperHandler", CATEGORY_NAME, true
                );
                static {
                    useCreeperHandler.getProperty().setRequiresMcRestart(true);
                    useCreeperHandler.setComment(
                        "Allows changes for Creepers such as the chance to spawn charged and causing fire on explosion."
                    );
                }

                public static ConfigProperty<Boolean> useEndermanHandler = new ConfigProperty<Boolean>(
                    "useEndermanHandler", CATEGORY_NAME, true
                );
                static {
                    useEndermanHandler.getProperty().setRequiresMcRestart(true);
                    useEndermanHandler.setComment(
                        "Allows changes for Endermen such as teleporting their target instead of themselves."
                    );
                }

                public static ConfigProperty<Boolean> useSkeletonHandler = new ConfigProperty<Boolean>(
                    "useSkeletonHandler", CATEGORY_NAME, true
                );
                static {
                    useSkeletonHandler.getProperty().setRequiresMcRestart(true);
                    useSkeletonHandler.setComment(
                        "Allows changes for Skeletons such as the chance to spawn as Wither Skeleton and configurable arrow tips."
                    );
                }

                public static ConfigProperty<Boolean> useSpiderHandler = new ConfigProperty<Boolean>(
                    "useSpiderHandler", CATEGORY_NAME, true
                );
                static {
                    useSpiderHandler.getProperty().setRequiresMcRestart(true);
                    useSpiderHandler.setComment(
                        "Allows changes for Spiders such as placing webs on hit."
                    );
                }

                public static ConfigProperty<Boolean> useWitchHandler = new ConfigProperty<Boolean>(
                    "useWitchHandler", CATEGORY_NAME, true
                );
                static {
                    useWitchHandler.getProperty().setRequiresMcRestart(true);
                    useWitchHandler.setComment(
                        "Allows changes for Witches such as configurable thrown potions."
                    );
                }

                public static ConfigProperty<Boolean> useZombieHandler = new ConfigProperty<Boolean>(
                    "useZombieHandler", CATEGORY_NAME, true
                );
                static {
                    useZombieHandler.getProperty().setRequiresMcRestart(true);
                    useZombieHandler.setComment(
                        "Allows 'infected' players to get turned into zombies on death."
                    );
                }
            }
            // ESM Other SubCategory Options
            public static class Other {

                public static final String CATEGORY_NAME = ESMCore.CATEGORY_NAME + ".Other";
                public static ConfigCategory configCategory = new ConfigCategory(CATEGORY_NAME, ESMCore.configCategory);

                public static ConfigProperty<Boolean> useSenses = new ConfigProperty<Boolean>(
                    "useSenses", CATEGORY_NAME, true
                );
                static {
                    useSenses.getProperty().setRequiresWorldRestart(true);
                    useSenses.setComment(
                        "Overrides mobs 'Senses' and enables 'XRay Distance' feature."
                    );
                }

                public static ConfigProperty<Boolean> useNavigator = new ConfigProperty<Boolean>(
                    "useNavigator", CATEGORY_NAME, true
                );
                static {
                    useNavigator.getProperty().setRequiresWorldRestart(true);
                    useNavigator.setComment(
                        "Overrides mobs 'Navigator', which affects mobs pathing in general."
                    );
                }
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

        public static ConfigProperty<String> loggingLevel = new ConfigProperty<String>(
            "loggingLevel", CATEGORY_NAME, Util.getManifestValue(MF_LOGLEVEL, "INFO")
        );
        static {
            loggingLevel.getProperty()
                .setValidValues(
                    new String[]{"OFF", "FATAL", "ERROR", "WARN", "INFO", "DEBUG", "TRACE", "ALL"});
            loggingLevel.setComment(
                "Sets the global log level for this mod's logging messages."
            );
        }
    }

    /**
     * Config initialization.
     */
    public static void init() {

        setupConfigCategories();
        setupConfigProperties();
        updateValues();
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
            AI.Attack.CATEGORY_NAME, ConfigProperty.LANG_KEY_PREFIX + ".category." + AI.Attack.configCategory.getName());
        configuration.setCategoryLanguageKey(
            AI.Attack.Melee.CATEGORY_NAME, ConfigProperty.LANG_KEY_PREFIX + ".category." + AI.Attack.Melee.configCategory.getName());
        configuration.setCategoryLanguageKey(
            Advanced.CATEGORY_NAME, ConfigProperty.LANG_KEY_PREFIX + ".category." + Advanced.configCategory.getName());
        configuration.setCategoryLanguageKey(
            Advanced.ESMCore.CATEGORY_NAME, ConfigProperty.LANG_KEY_PREFIX + ".category." + Advanced.ESMCore.configCategory.getName());
        configuration.setCategoryLanguageKey(
            Advanced.ESMCore.Additions.CATEGORY_NAME, ConfigProperty.LANG_KEY_PREFIX + ".category." + Advanced.ESMCore.Additions.configCategory.getName());
        configuration.setCategoryLanguageKey(
            Advanced.ESMCore.Modifiers.CATEGORY_NAME, ConfigProperty.LANG_KEY_PREFIX + ".category." + Advanced.ESMCore.Modifiers.configCategory.getName());
        configuration.setCategoryLanguageKey(
            Advanced.ESMCore.Handlers.CATEGORY_NAME, ConfigProperty.LANG_KEY_PREFIX + ".category." + Advanced.ESMCore.Handlers.configCategory.getName());
        configuration.setCategoryLanguageKey(
            Advanced.ESMCore.Other.CATEGORY_NAME, ConfigProperty.LANG_KEY_PREFIX + ".category." + Advanced.ESMCore.Other.configCategory.getName());
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
        ConfigProperty.setupPropertiesFromClass(AI.Attack.class, configuration);
        ConfigProperty.setupPropertiesFromClass(AI.Attack.Melee.class, configuration);

        ConfigProperty.setupPropertiesFromClass(Advanced.class, configuration);
        ConfigProperty.setupPropertiesFromClass(Advanced.ESMCore.class, configuration);
        ConfigProperty.setupPropertiesFromClass(Advanced.ESMCore.Additions.class, configuration);
        ConfigProperty.setupPropertiesFromClass(Advanced.ESMCore.Modifiers.class, configuration);
        ConfigProperty.setupPropertiesFromClass(Advanced.ESMCore.Handlers.class, configuration);
        ConfigProperty.setupPropertiesFromClass(Advanced.ESMCore.Other.class, configuration);

        ConfigProperty.setupPropertiesFromClass(Debug.class, configuration);

        // setting up the order in which settings are displayed in the Config GUI
        List<String> propertiesNames = new ArrayList<>();

        // General AI options ordering
        propertiesNames = new ArrayList<>();
        propertiesNames.add(AI.General.updateAITaskOnDeath.getName());
        propertiesNames.add(AI.General.disableXRay.getName());
        propertiesNames.add(AI.General.copyNavigatorProperties.getName());

        configuration.setCategoryPropertyOrder(AI.General.CATEGORY_NAME, propertiesNames);
        
        // Digging AI options ordering
        propertiesNames = new ArrayList<>();
        propertiesNames.add(AI.Digging.useTweakedAI.getName());
        propertiesNames.add(AI.Digging.mustHaveCorrectTool.getName());
        propertiesNames.add(AI.Digging.checkBothHands.getName());
        propertiesNames.add(AI.Digging.searchBlockInterval.getName());
        propertiesNames.add(AI.Digging.harvestCheckInterval.getName());
        propertiesNames.add(AI.Digging.canGetExtraTool.getName());
        propertiesNames.add(AI.Digging.extraToolOverride.getName());
        propertiesNames.add(AI.Digging.extraToolChance.getName());
        propertiesNames.add(AI.Digging.legacyHarvestCheck.getName());
        propertiesNames.add(AI.Digging.diggingSounds.getName());
        propertiesNames.add(AI.Digging.digSpeedMultiplier.getName());

        configuration.setCategoryPropertyOrder(AI.Digging.CATEGORY_NAME, propertiesNames);
        
        // MeleeAttack AI options ordering
        propertiesNames = new ArrayList<>();
        propertiesNames.add(AI.Attack.Melee.useTweakedAI.getName());
        propertiesNames.add(AI.Attack.Melee.forceLongMemory.getName());
        propertiesNames.add(AI.Attack.Melee.useCustomAttackDelay.getName());
        propertiesNames.add(AI.Attack.Melee.minAttackDelay.getName());
        propertiesNames.add(AI.Attack.Melee.maxAttackDelay.getName());

        configuration.setCategoryPropertyOrder(AI.Attack.Melee.CATEGORY_NAME, propertiesNames);

        // Advanced ESMCore Additions options ordering
        propertiesNames = new ArrayList<>();
        propertiesNames.add(Advanced.ESMCore.Additions.useAnimalAttack.getName());
        propertiesNames.add(Advanced.ESMCore.Additions.useAnimalRetaliate.getName());
        propertiesNames.add(Advanced.ESMCore.Additions.useAvoidExplosives.getName());
        propertiesNames.add(Advanced.ESMCore.Additions.useDemolition.getName());
        propertiesNames.add(Advanced.ESMCore.Additions.useDigger.getName());
        propertiesNames.add(Advanced.ESMCore.Additions.useGriefing.getName());
        propertiesNames.add(Advanced.ESMCore.Additions.usePillaring.getName());

        configuration.setCategoryPropertyOrder(Advanced.ESMCore.Additions.CATEGORY_NAME, propertiesNames);

        // Advanced ESMCore Modifiers options ordering
        propertiesNames = new ArrayList<>();
        propertiesNames.add(Advanced.ESMCore.Modifiers.useAttackMelee.getName());
        propertiesNames.add(Advanced.ESMCore.Modifiers.useRangedAttack.getName());
        propertiesNames.add(Advanced.ESMCore.Modifiers.useBowAttack.getName());
        propertiesNames.add(Advanced.ESMCore.Modifiers.useZombieAttack.getName());
        propertiesNames.add(Advanced.ESMCore.Modifiers.useCreeperSwell.getName());
        propertiesNames.add(Advanced.ESMCore.Modifiers.useNearestAttackable.getName());
        propertiesNames.add(Advanced.ESMCore.Modifiers.useNoPanic.getName());
        propertiesNames.add(Advanced.ESMCore.Modifiers.useSwimming.getName());
        propertiesNames.add(Advanced.ESMCore.Modifiers.useAvoidVillager.getName());
        propertiesNames.add(Advanced.ESMCore.Modifiers.useWander.getName());
        
        configuration.setCategoryPropertyOrder(Advanced.ESMCore.Modifiers.CATEGORY_NAME, propertiesNames);

        // Advanced ESMCore Handlers options ordering
        propertiesNames = new ArrayList<>();
        propertiesNames.add(Advanced.ESMCore.Handlers.useMainHandler.getName());
        propertiesNames.add(Advanced.ESMCore.Handlers.useEntityHandler.getName());
        propertiesNames.add(Advanced.ESMCore.Handlers.usePlayerHandler.getName());
        propertiesNames.add(Advanced.ESMCore.Handlers.useCreeperHandler.getName());
        propertiesNames.add(Advanced.ESMCore.Handlers.useEndermanHandler.getName());
        propertiesNames.add(Advanced.ESMCore.Handlers.useSpiderHandler.getName());
        propertiesNames.add(Advanced.ESMCore.Handlers.useSkeletonHandler.getName());
        propertiesNames.add(Advanced.ESMCore.Handlers.useWitchHandler.getName());
        propertiesNames.add(Advanced.ESMCore.Handlers.useZombieHandler.getName());
        
        configuration.setCategoryPropertyOrder(Advanced.ESMCore.Handlers.CATEGORY_NAME, propertiesNames);

        // Advanced ESMCore Other options ordering
        propertiesNames = new ArrayList<>();
        propertiesNames.add(Advanced.ESMCore.Other.useSenses.getName());
        propertiesNames.add(Advanced.ESMCore.Other.useNavigator.getName());
        
        configuration.setCategoryPropertyOrder(Advanced.ESMCore.Other.CATEGORY_NAME, propertiesNames);

        // Debug options ordering
        propertiesNames = new ArrayList<>();
        propertiesNames.add(Debug.enableDebug.getName());
        propertiesNames.add(Debug.showCanBreakBlockMessage.getName());
        propertiesNames.add(Debug.outputBlockInfoInChat.getName());
        propertiesNames.add(Debug.useOffhandItemForChecks.getName());
        propertiesNames.add(Debug.loggingLevel.getName());

        configuration.setCategoryPropertyOrder(Debug.CATEGORY_NAME, propertiesNames);

        // saving previous changes
        configuration.save();
    }

    /**
     * Updates values that need to be set from the config when it changes.
     */
    public static void updateValues() {
        // updates the logging level if it has changed
        if(!ModConfig.Debug.loggingLevel.getValue().equals(ESMTweaks.logger.getLevel().name())) {
            ESMTweaks.logger.setLevel(Level.forName(ModConfig.Debug.loggingLevel.getValue(), 400));
            ESMTweaks.logger.info("logging level set to " + ESMTweaks.logger.getLevel().name());
        }
        
        // update digging ai values
        AltEntityAIDigging.harvestCheckInterval = ModConfig.AI.Digging.harvestCheckInterval.getValue();
        AltEntityAIDigging.searchBlockInterval = ModConfig.AI.Digging.searchBlockInterval.getValue();
        AltEntityAIDigging.diggingSounds = ModConfig.AI.Digging.diggingSounds.getValue();
        AltEntityAIDigging.digSpeedMultiplier = ModConfig.AI.Digging.digSpeedMultiplier.getValue();
        
        // update melee attack ai values
        AltEntityAIAttackMelee.useCustomAttackDelay = ModConfig.AI.Attack.Melee.useCustomAttackDelay.getValue();
        AltEntityAIAttackMelee.minAttackDelay = ModConfig.AI.Attack.Melee.minAttackDelay.getValue();
        AltEntityAIAttackMelee.maxAttackDelay = ModConfig.AI.Attack.Melee.maxAttackDelay.getValue();
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
            elements.add(new ConfigElement(configuration.getCategory(Advanced.CATEGORY_NAME)));
        }
        elements.add(new ConfigElement(configuration.getCategory(Debug.CATEGORY_NAME)));

        return elements;
    }
}