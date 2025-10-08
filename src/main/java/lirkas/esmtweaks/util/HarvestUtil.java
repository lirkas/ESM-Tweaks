package lirkas.esmtweaks.util;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import tyra314.toolprogression.api.OverwrittenContent;
import tyra314.toolprogression.config.ConfigHandler;
import tyra314.toolprogression.harvest.BlockHelper;
import tyra314.toolprogression.harvest.BlockOverwrite;
import tyra314.toolprogression.harvest.HarvestLevelName;

import lirkas.esmtweaks.mods.toolprogression.ToolProgression;

/**
 * A Collection of methods to obtain various details related to block harvesting/breaking.
 */
public class HarvestUtil {
    
    /**
     * Checks if a specific block can be harvested/destroyed without any tool.
     * 
     * @param blockState The block to check.
     * @return true if the block can be broken.
     */
    public static boolean canBreakBlockByHand(IBlockState blockState) {
        
        Block block = blockState.getBlock();
        
        // Unbreakable blocks such as bedrock have a negative hardness
        // Toolprogession overwrites never have negative hardness
        if(blockState.getBlockHardness(null, null) < 0) {
            return false;
        }
        
        if(ToolProgression.isLoaded()) {

            // ToolProgression API doesnt register keys the same way as its config does, 
            // so not all overwrites are properly added in the API (tooprogression 1.12.2-1.6.2)
            // Getting them via ConfigHandler class for now.
            BlockOverwrite blockOverwrite = ConfigHandler.blockOverwrites.get(BlockHelper.getKeyString(blockState));
            // The prefered way, but not unable to get overwrites for blocks like specific stained glass color            
            //BlockOverwrite blockOverwrite = OverwrittenContent.blocks.get(block.getTranslationKey());

            if(blockOverwrite != null && !blockOverwrite.toolRequired) {
                return true;
            }
            if(blockOverwrite == null && blockState.getMaterial().isToolNotRequired()) {
                return true;
            }
        }
        else if(blockState.getMaterial().isToolNotRequired()) {
            return true;
        }
        
        if(block.getHarvestTool(blockState) == null) {
            return true;
        }

        if(block.getHarvestTool(blockState).equals("null")) {
            return true;
        }

        return false;
    }

    /**
     * Checks if a specific block can be harvested/destroyed with the specified item.
     * 
     * @param itemStack The item used to break the block.
     * @param blockState The block to check.
     * @return true if the block can be broken with that item. 
     */
    public static boolean canBreakWithItem(ItemStack itemStack, IBlockState blockState) {
        
        // No need to check further if the block can be handbroken
        if(canBreakBlockByHand(blockState)) {
            return true;
        }

        String blockHarvestClass = blockState.getBlock().getHarvestTool(blockState);

        if(blockHarvestClass == null) {
            return false;
        }

        int blockHarvestLevel = blockState.getBlock().getHarvestLevel(blockState);
        // Going through each of the item/tool categories (required for ones such as Paxel)
        // and check if one of them is valid for breaking this block
        for(String itemHarvestClass : itemStack.getItem().getToolClasses(itemStack)) {

            int itemHarvestLevel = itemStack.getItem().getHarvestLevel(itemStack, itemHarvestClass, null, null);

            if(itemHarvestClass.equals(blockHarvestClass) && itemHarvestLevel >= blockHarvestLevel) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * A combination of canBreakBlockByHand and canBreakWithItem for checking if the block
     * can be broken.
     * 
     * @param entity The entity (monster, player or anything else) attempting to break the block.
     * @param blockState The block to check.
     * @param checkBothHands Do Checks for both held items if true.
     * @return true if the block can be broken by this entity.
     */
    public static boolean canEntityBreakBlock(EntityLivingBase entity, IBlockState blockState, boolean checkBothHands) {

        if(canBreakBlockByHand(blockState) || canBreakWithItem(entity.getHeldItemMainhand(), blockState)) {
            return true;
        }

        if(checkBothHands && canBreakWithItem(entity.getHeldItemOffhand(), blockState)) {
            return true;
        }

        return false;
    }

    /**
     * Checks if the tool is effective against this block. 
     * Doesnt care if the block requires a tool to be broken or not.
     * 
     * @param itemStack The tool to check with.
     * @param blockState The block to check on.
     * @return true is the tool if effective.
     */
    public static boolean isToolEffective(ItemStack itemStack, IBlockState blockState) {

        String blockHarvestClass = blockState.getBlock().getHarvestTool(blockState);

        if(blockHarvestClass == null) {
            return false;
        }

        // Going through each of the item/tool categories (required for ones such as Paxel)
        // and check if one of them matches for this block.
        for(String itemHarvestClass : itemStack.getItem().getToolClasses(itemStack)) {

            if(itemHarvestClass.equals(blockHarvestClass)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the block is unbreakable (bedrock-like blocks).
     * 
     * @param blockState The block to check.
     * @return true if the block is unbreakable.
     */
    public static boolean isBlockUnbreakable(IBlockState blockState) {

        // Unbreakable blocks such as bedrock have a negative hardness
        // Toolprogession overwrites never have negative hardness
        if(blockState.getBlockHardness(null, null) < 0) {
            return true;
        }

        return false;
    }

    /**
     * Checks if the block destroyability is enforced by ToolProgression or another
     * mod's setting.
     * 
     * @param blockState The block to check.
     * @return true if the block is not meant to be breakable.
     */
    public static boolean shouldBlockNotBeBroken(IBlockState blockState) {

        if(ToolProgression.isLoaded()) {

            BlockOverwrite overwrite = ConfigHandler.blockOverwrites.get(blockState);
            if (ConfigHandler.prevent_block_destruction && (overwrite == null || !overwrite.destroyable)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Non performance-optimal way to calculate the theorical time it would take to break
     * a block, using a specific tool/item.
     * Does not account for all factors, such as being in water, under haste effect, etc.
     * 
     * @param itemStack The item to test with.
     * @param blockPos The block to check.
     * @param world The world where the check takes place.
     * @return The time it would take for the block to be mined with that item, in seconds.
     * 
     * @see https://minecraft.wiki/w/Breaking#Calculation
     */
    public static double getBlockBreakTime(ItemStack itemStack, BlockPos blockPos, World world) {

        IBlockState blockState = world.getBlockState(blockPos);

        boolean canToolBreakBlock = false;
        if(canBreakWithItem(itemStack, blockState)) {
            canToolBreakBlock = true;
        }

        float toolSpeedMultiplier = itemStack.getItem().getDestroySpeed(itemStack, blockState);
        boolean isToolEffective = isToolEffective(itemStack, blockState);

        float speedMultiplier = 1;
        if(isToolEffective) {
            speedMultiplier = toolSpeedMultiplier;
        }
        
        float damage = speedMultiplier / blockState.getBlockHardness(world, blockPos);
        if(canToolBreakBlock) {
            damage /= 30;
        }
        else{
            damage /= 100;
        }

        if(damage >= 1) {
            return 0;
        }

        double ticks = Math.ceil(1 / damage);
        double timeToBreak = ticks / 20;

        return timeToBreak;
    }

    /**
     * Vanilla-ish method to calculate the digging speed for this entity held item.
     * 
     * @param entity The digging entity.
     * @param enumHand The hand used to mine this block.
     * @param blockPos The block to dig.
     * @return The digging speed for this block.
     */
    public static float getDiggingSpeed(EntityLivingBase entity, EnumHand enumHand, BlockPos blockPos) {
        
        World world = entity.world;
        IBlockState state = world.getBlockState(blockPos);
        ItemStack heldItem = entity.getHeldItem(enumHand);
        
        float diggingSpeed = heldItem.isEmpty()? 1F : heldItem.getDestroySpeed(state);
        
        if (diggingSpeed > 1.0F) {
            int efficiencyModifier = EnchantmentHelper.getEfficiencyModifier(entity);

            if (efficiencyModifier > 0 && !heldItem.isEmpty()) {
                diggingSpeed += (float)(efficiencyModifier * efficiencyModifier + 1);
            }
        }

        if (entity.isPotionActive(MobEffects.HASTE)) {
            diggingSpeed *= 1.0F + (float)(entity.getActivePotionEffect(MobEffects.HASTE).getAmplifier() + 1) * 0.2F;
        }

        if (entity.isPotionActive(MobEffects.MINING_FATIGUE)) {
            float fatigueModifier;

            switch (entity.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier())
            {
                case 0:
                    fatigueModifier = 0.3F;
                    break;
                case 1:
                    fatigueModifier = 0.09F;
                    break;
                case 2:
                    fatigueModifier = 0.0027F;
                    break;
                case 3:
                default:
                    fatigueModifier = 8.1E-4F;
            }

            diggingSpeed *= fatigueModifier;
        }

        if (entity.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(entity)) {
            diggingSpeed /= 5.0F;
        }

        if (!entity.onGround) {
            diggingSpeed /= 5.0F;
        }
        
        return (diggingSpeed < 0 ? 0 : diggingSpeed);
    }

    /**
     * Gets this block strength based on the entity mining it.
     * 
     * @param entity The digging entity.
     * @param enumHand The hand used to mine this block.
     * @param blockPos The block to dig.
     * @return This block strength.
     */
    public static float getBlockStrength(EntityLivingBase entity, EnumHand enumHand, BlockPos pos) {

        World world = entity.world;
        IBlockState blockState = world.getBlockState(pos);
        float hardness = blockState.getBlockHardness(world, pos);
        
        if(hardness <= 0F) {
            return 0F;
        }
        
        ItemStack heldItem = entity.getHeldItem(enumHand);
        boolean canHarvest = blockState.getMaterial().isToolNotRequired() || (!heldItem.isEmpty() && heldItem.canHarvestBlock(blockState));
        
        return getDiggingSpeed(entity, enumHand, pos) / hardness / (canHarvest? 30F : 100F);
    }

    /**
     * Returns various informations about a block, and its destroyability/breakability details.
     * This is returned in a format to be displayed by the game.
     * 
     * @param blockPos The block to get infos from.
     * @param entityLiving The entity to get details from, related to this block info.
     * @param useOffHandItem Whether it should should use off hand held item instead of main hand for certain infos.
     * @return The message containing all the informations, formatted and ready to be displayed in the chat or another GUI.
     */
    public static TextComponentString getBlockInfosTextMessage(BlockPos blockPos, EntityLivingBase entityLiving, boolean useOffHandItem) {
        /* The info message looks similar to this:
            [BLOCK INFOS] 
            Name        : Stone
            ID          : minecraft:grass:0
            Breakable   : Yes
            CanIBreak   : No
            Hardness    : 1.2
            Eff. Tools  : Pickaxe - Crude [0]
            HandBreak   : No
            BreakTime   : 1.5s
        */
        TextComponentString infosText = new TextComponentString("");

        Style goldColor = new Style().setColor(TextFormatting.GOLD);
        Style redColor = new Style().setColor(TextFormatting.RED);
        Style greenColor = new Style().setColor(TextFormatting.GREEN);
        Style whiteColor = new Style().setColor(TextFormatting.WHITE);
        Style greyColor = new Style().setColor(TextFormatting.GRAY);

        IBlockState blockState = entityLiving.world.getBlockState(blockPos);

        ItemStack heldItem = useOffHandItem ? entityLiving.getHeldItemOffhand() : entityLiving.getHeldItemMainhand();
        boolean requiresTool = !canBreakBlockByHand(blockState);
        boolean canIBreakBlock = canBreakWithItem(heldItem, blockState);
        double blockBreakTime = getBlockBreakTime(heldItem, blockPos, entityLiving.world);
        String effectiveToolName = blockState.getBlock().getHarvestTool(blockState);
        int harvestLevel = blockState.getBlock().getHarvestLevel(blockState);
        String harvestLevelName = null;
        String heldItemName = heldItem.getDisplayName();
        
        if(effectiveToolName == null || effectiveToolName.equals("null")) {
            effectiveToolName = null;
        }

        if(effectiveToolName != null && ToolProgression.isLoaded()) {
            harvestLevelName = OverwrittenContent.mining_level.getOrDefault(
                harvestLevel, new HarvestLevelName(harvestLevel, "?")).getFormatted();
        }
        
        Style requiresToolTextStyle = requiresTool ? greenColor : redColor;
        Style canIBreakBlockTextStyle = canIBreakBlock ? greenColor : redColor;
        Style blockBreakTimeTextStyle = blockBreakTime > 0 ? whiteColor : blockBreakTime < 0 ? redColor : greenColor;
        Style harvestLevelTextStyle = harvestLevel < 0 ? greyColor : whiteColor;
        Style effectiveToolNameTextStyle = effectiveToolName == null ? greyColor : whiteColor;
        Style harvestLevelNameTexStyle = whiteColor;

        ITextComponent requiresToolText = 
            new TextComponentString(requiresTool ? "Yes" : "No").setStyle(requiresToolTextStyle);
        ITextComponent canBreakBlockText = 
            new TextComponentString(canIBreakBlock ? "Yes" : "No").setStyle(canIBreakBlockTextStyle);
        ITextComponent effectiveToolNameText = 
            new TextComponentString(effectiveToolName != null ? effectiveToolName : "").setStyle(effectiveToolNameTextStyle);
        ITextComponent harvestLevelText = 
            new TextComponentString(harvestLevel < 0 ? "None" : "[" + harvestLevel + "]").setStyle(harvestLevelTextStyle);
        ITextComponent harvestLevelNameText = 
            new TextComponentString(harvestLevelName != null ? ""+harvestLevelName : "").setStyle(harvestLevelNameTexStyle);
        ITextComponent blockBreakTimeText = 
            new TextComponentString(blockBreakTime < 0 ? "Unbreakable" : blockBreakTime + "s").setStyle(blockBreakTimeTextStyle);


        infosText.appendSibling(new TextComponentString(
            "\n[BLOCK INFOS]" + "\n").setStyle(goldColor)
        );
        infosText.appendSibling(new TextComponentString(
            "Name             :  " + 
            blockState.getBlock().getLocalizedName() + "\n"
        ));
        infosText.appendSibling(new TextComponentString(
            "ID                :  " + 
            blockState.getBlock().getRegistryName() + "\n"
        ));
        infosText.appendSibling(new TextComponentString(
            "Hardness       :  " + 
            blockState.getBlockHardness(entityLiving.world, blockPos) + "\n"
        ));
        infosText.appendSibling(new TextComponentString(
            "Needs Tool     :  " + 
            requiresToolText.getFormattedText() + "\n"
        ));
        infosText.appendSibling(new TextComponentString(
            "Effective Tool :  " + 
            effectiveToolNameText.getFormattedText() + " - " +
            harvestLevelNameText.getFormattedText() + " " +
            harvestLevelText.getFormattedText() + "\n"
        ));
        infosText.appendSibling(new TextComponentString(
            "Can I Break    :  " + 
            canBreakBlockText.getFormattedText() + " " +
            "(" + heldItemName + ")\n"
        ));
        infosText.appendSibling(new TextComponentString(
            "Break Time     :  " + 
            blockBreakTimeText.getFormattedText() + "\n"
        ));

        return infosText;
    }
    
    /**
     * Returns a small text message about whether the a block can be broken by a specific entity or not.
     * 
     * @param blockPos The block to check.
     * @param entityLiving The entity that would attempt to break the block.
     * @param useOffHandItem Whether it should should use off hand held item instead of main hand for checking.
     * @return The message saying if this block can be broken or not.
     */
    public static TextComponentString getBlockBreakableTextMessage(BlockPos blockPos, EntityLivingBase entityLiving, boolean useOffHandItem) {

        TextComponentString breakText = new TextComponentString("");

        Style redColor = new Style().setColor(TextFormatting.RED);
        Style greenColor = new Style().setColor(TextFormatting.GREEN);

        ItemStack heldItem = useOffHandItem ? entityLiving.getHeldItemOffhand() : entityLiving.getHeldItemMainhand();
        IBlockState interactedBlockState = entityLiving.world.getBlockState(blockPos);
        Block interactedBlock = interactedBlockState.getBlock();

        String blockName = interactedBlock.getLocalizedName();
        String itemName = heldItem.getDisplayName();
        boolean canbreak = canBreakWithItem(heldItem, interactedBlockState);

        Style canbreakTextStyle = canbreak ? greenColor : redColor;

        ITextComponent canBreakText = 
            new TextComponentString(canbreak ? "can" : "cannot").setStyle(canbreakTextStyle);

        breakText.appendSibling(new TextComponentString(
            itemName + " " + canBreakText.getFormattedText() + " break " + blockName
        ));

        return breakText;
    }
}