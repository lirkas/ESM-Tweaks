package lirkas.esmtweaks.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;

import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import funwayguy.epicsiegemod.core.ESM_Settings;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.config.ModConfig;
import lirkas.esmtweaks.util.HarvestUtil;

/**
 * The AI for digging entities (Zombies, etc...).
 * Modified version of funwayguy.epicsiegemod.ai.ESM_EntityAIDigging (v13.169).
 */
public class AltEntityAIDigging extends EntityAIBase {

	private EntityLivingBase target;
    private EntityLiving digger;
    private BlockPos curBlock;
    private int scanTick = 0;
    private int digTick = 0;
    private BlockPos obsPos = null;
    private int obsTick = 0;
    private EnumHand diggingHand;

    private boolean canHarvest = false;
    private IBlockState previousBlockState = null; //? is this really needed

    // ticks between block searching attempts
    public static int searchBlockInterval = 1;
    // ticks between harvest checks during mining
    public static int harvestCheckInterval = 20;


	public AltEntityAIDigging(EntityLiving digger) {
        this.digger = digger;
        this.diggingHand = EnumHand.MAIN_HAND;
	}
	
	@Override
	public boolean shouldExecute() {

        ESMTweaks.logger.trace("shouldExecute " + this.digger.getName() + " (" + 
            this.digger.getHeldItemMainhand().getDisplayName() + " - " +
            this.digger.getHeldItemOffhand().getDisplayName() + ")");

		this.target = this.digger.getAttackTarget();
        if (this.target == null || !this.target.isEntityAlive() || !this.digger.getNavigator().noPath()) {
            return false;
        }
        double dist = this.digger.getDistanceSq((Entity)this.target);
        double navDist = this.digger.getNavigator().getPathSearchRange();

        if (dist < 1.0 || dist > navDist * navDist) {
            return false;
        }
        if (this.obsPos == null) {
            this.obsPos = this.digger.getPosition();
        }
        if (!this.obsPos.equals((Object)this.digger.getPosition())) {
            this.obsTick = 0;
            this.obsPos = null;
            return false;
        }

        this.obsTick++;

        // limits how often the mob looks for a block to break
        if (this.obsTick % searchBlockInterval != 0) {
            return false;
        }
        this.curBlock = 
            this.curBlock != null && this.digger.getDistanceSq(this.curBlock) <= 16.0 && this.canHarvest(this.curBlock) ? 
            this.curBlock : this.getNextBlock(this.target, 2.0);

        return this.curBlock != null;
	}
	
	@Override
	public void startExecuting() {

        ESMTweaks.logger.trace("startExecuting : " + this.digger.getName());

		super.startExecuting();
		this.digger.getNavigator().clearPath();
		this.obsTick = 0;
        this.obsPos = null;
	}

	@Override
	public void resetTask() {

        ESMTweaks.logger.trace("resetTask : " + this.digger.getName());

        if(this.curBlock != null) {
            // this remove the breaking texture if the entity stops breaking for any reason
            // also gets the block back to its original unbroken state
            this.digger.world.sendBlockBreakProgress(this.digger.getEntityId(), this.curBlock, -1);
        }
        
		this.curBlock = null;
        this.digTick = 0;
        this.obsTick = 0;
        this.obsPos = null;
        this.canHarvest = false;
        this.previousBlockState = null;
        this.diggingHand = EnumHand.MAIN_HAND;
	}
	
	@Override
	public boolean shouldContinueExecuting() {

        ESMTweaks.logger.trace("shouldContinueExecuting : " + this.digger.getName() + " with (" + 
            this.digger.getHeldItemMainhand().getDisplayName() + " - " +
            this.digger.getHeldItemOffhand().getDisplayName() + ") on " +
            (this.curBlock == null ? "null" : this.digger.world.getBlockState(this.curBlock).getBlock().getLocalizedName()));

        if(this.target == null || this.curBlock == null || 
                this.digger.getDistanceSq(this.curBlock) > 16.0 || !this.canHarvest || 
                this.digger.world.getBlockState(this.curBlock) != this.previousBlockState ||
                !this.digger.isEntityAlive()) {
            return false;
        }

        return true;
	}
	
	@Override
	public void updateTask() {

        ESMTweaks.logger.trace("updateTask : " + this.digger.getName());

		this.digger.getLookHelper().setLookPosition(
            this.target.posX, this.target.posY + (double)this.target.getEyeHeight(), 
            this.target.posZ, (float)this.digger.getHorizontalFaceSpeed(), 
            (float)this.digger.getVerticalFaceSpeed());
		this.digger.getNavigator().clearPath();
		
		this.digTick++;

        float str = HarvestUtil.getBlockStrength(this.digger, this.diggingHand, this.curBlock) * ((float)this.digTick + 1.0f);
		ItemStack heldItem = this.digger.getHeldItem(this.diggingHand);
		IBlockState state = this.digger.world.getBlockState(this.curBlock);
		
        this.previousBlockState = state;

        // periodically checks if the block can still be broken
        // this could probably go into shouldContinueExecuting() for clarity
        if(this.digTick % harvestCheckInterval == 0) {
            this.canHarvest = canHarvest(this.curBlock);
        }

        // this could probably go into shouldContinueExecuting() for clarity
		if(this.digger.world.isAirBlock(this.curBlock)) {
			this.resetTask();
            return; // just for safety
		}
        else if(str >= 1F) { // Block has been broken.

            // when param2 == true, the block is dropped as an item (harvest)
			this.digger.world.destroyBlock(this.curBlock, false);

            if (this.canHarvest && this.digger.world instanceof WorldServer) {
                // this could be replaced with :
                //      this.digger.world.destroyBlock(this.curBlock, true);
                // but it will not check for silk touch enchant if present on the tool
                // as the code below current does.
                //
                // should ideally get rid of FakePlayer stuffs, but need to figure out what all this does
                // and if another way of achieving this is possible (Forge docs says FakePlayer may cause world leaking issue)
                FakePlayer player = FakePlayerFactory.getMinecraft((WorldServer)((WorldServer)this.digger.world));
                player.setHeldItem(EnumHand.MAIN_HAND, this.digger.getHeldItem(EnumHand.MAIN_HAND));
                player.setHeldItem(EnumHand.OFF_HAND, this.digger.getHeldItem(EnumHand.OFF_HAND));
                player.setPosition((double)this.digger.getPosition().getX(), (double)this.digger.getPosition().getY(), (double)this.digger.getPosition().getZ());
                TileEntity tile = this.digger.world.getTileEntity(this.curBlock);
                state.getBlock().harvestBlock(this.digger.world, (EntityPlayer)player, this.curBlock, state, tile, heldItem);
            }

            this.digger.getNavigator().setPath(this.digger.getNavigator().getPathToEntityLiving(target), this.digger.getMoveHelper().getSpeed()); // This is fine. We only run it after a block breaks
			this.resetTask();
		} 
        else if(digTick % 5 == 0) { // Just keeping digging...

			this.digger.world.playSound(null, this.curBlock, state.getBlock().getSoundType(state, this.digger.world, this.curBlock, this.digger).getHitSound(), SoundCategory.BLOCKS, 1F, 1F);
			this.digger.swingArm(this.diggingHand);
			this.digger.world.sendBlockBreakProgress(this.digger.getEntityId(), this.curBlock, (int)(str * 10F));
		}
	}
	
    /**
     * Looking for the next block to break to get to reach its target.
     */
	public BlockPos getNextBlock(EntityLivingBase target, double dist) {

        ESMTweaks.logger.trace("getNextBlock : " + this.digger.getName());

        int digWidth = MathHelper.ceil(this.digger.width);
        int digHeight = MathHelper.ceil(this.digger.height);

        int passMax = digWidth * digWidth * digHeight;
        if (passMax <= 0) {
            return null;
        }
        int y = this.scanTick % digHeight;
        int x = this.scanTick % (digWidth * digHeight) / digHeight;
        int z = this.scanTick / (digWidth * digHeight);

        double rayX = (double)x + Math.floor(this.digger.posX) + 0.5 - (double)digWidth / 2.0;
        double rayY = (double)y + Math.floor(this.digger.posY) + 0.5;
        double rayZ = (double)z + Math.floor(this.digger.posZ) + 0.5 - (double)digWidth / 2.0;

        Vec3d rayOrigin = new Vec3d(rayX, rayY, rayZ);
        // could this (+0.5) be what cause this mob to dig the block on its right instead of in front of it
        // when the player is in front ? (in a 2x1 tunnel situation)
        Vec3d rayOffset = new Vec3d(
			Math.floor(target.posX) + 0.5, 
			Math.floor(target.posY) + 0.5, 
			Math.floor(target.posZ) + 0.5
		);
        rayOffset.add((double)x - (double)digWidth / 2.0, (double)y, (double)z - (double)digWidth / 2.0);
        Vec3d norm = rayOffset.subtract(rayOrigin).normalize(); // unsure about subtract (could be add)

        if (Math.abs(norm.x) == Math.abs(norm.z) && norm.x != 0.0) {
            norm = new Vec3d(norm.x, norm.y, 0.0).normalize();
        }
        rayOffset = rayOrigin.add(norm.scale(dist)); // unsure about add (could be subtract)
        
		BlockPos diggerPos = this.digger.getPosition();
		BlockPos targetPos = target.getPosition();

        if (diggerPos.getDistance(targetPos.getX(), diggerPos.getY(), targetPos.getZ()) < 4.0) {
            
            if ((double)(targetPos.getY() - diggerPos.getY()) > 2.0) {
                rayOffset = rayOrigin.add(0.0, dist, 0.0);
            } 
            else if ((double)(targetPos.getY() - diggerPos.getY()) < -2.0) {
                rayOffset = rayOrigin.add(0.0, -dist, 0.0);
            }
        }
        RayTraceResult ray = this.digger.world.rayTraceBlocks(rayOrigin, rayOffset, false, true, false);
        this.scanTick = (this.scanTick + 1) % passMax;
		
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = ray.getBlockPos();
            IBlockState state = this.digger.world.getBlockState(pos);
            // could remove the right side of this AND condition after some verification and testing,
            // since the same check is done inside canHarvest()
            if (this.canHarvest(pos) && 
                    ESM_Settings.ZombieDigBlacklist.contains(state.getBlock().getRegistryName().toString()) == ESM_Settings.ZombieSwapList) {
                return pos;
            }
        }
        return null;
	}

    /**
     * Orginal canHarvest method from ESM, besides the removal of EntityLiving argument.
     */
    public boolean originalCanHarvest(BlockPos pos) {

        IBlockState state = this.digger.world.getBlockState(pos);
        if (!state.getMaterial().isSolid() || state.getBlockHardness(this.digger.world, pos) < 0.0f) {
            return false;
        }
        if (state.getMaterial().isToolNotRequired() || !ESM_Settings.ZombieDiggerTools) {
            return true;
        }
        ItemStack held = this.digger.getHeldItem(EnumHand.MAIN_HAND);

        return !held.isEmpty() && held.getItem().canHarvestBlock(state, held);
    }

	public boolean canHarvestBlock(BlockPos pos) {

        IBlockState blockState = this.digger.world.getBlockState(pos);

        ItemStack mainHandItem = this.digger.getHeldItem(EnumHand.MAIN_HAND);
        if(HarvestUtil.canBreakWithItem(mainHandItem, blockState)) {
            this.diggingHand = EnumHand.MAIN_HAND;
            return true;
        }

        if(ModConfig.AI.Digging.checkBothHands.getValue()) {
            ItemStack offHandItem = this.digger.getHeldItem(EnumHand.OFF_HAND);
            if(HarvestUtil.canBreakWithItem(offHandItem, blockState)) {
                this.diggingHand = EnumHand.OFF_HAND;
                return true;
            }
        }
        return false;
    }

    /**
     * Main method to check if this entity can harvest the block.
     */
	public boolean canHarvest(BlockPos pos) {

        // Original check handler
        if(ModConfig.AI.Digging.legacyHarvestCheck.getValue()) {

            this.canHarvest = this.originalCanHarvest(pos);
            return this.canHarvest;
        }

        ESMTweaks.logger.trace("canHarvest : " + this.digger.getName());

        IBlockState state = this.digger.world.getBlockState(pos);
        
        // If the block should not be broken due to its properties
        if(HarvestUtil.isBlockUnbreakable(state)) {
            
            this.canHarvest = false;
            return this.canHarvest;
        }

        // The block is blacklisted or not whitelisted for digging 
        // (only works if the list is defined under the entity tag in the config)
        if(ESM_Settings.ZombieDigBlacklist.contains(state.getBlock().getRegistryName().toString()) == !ESM_Settings.ZombieSwapList) {

            this.canHarvest = false;
            return this.canHarvest;
        }

        if(!state.getMaterial().isSolid()) {

            this.canHarvest = false;
            return this.canHarvest;
        }

        // If "Requires Tools" == true in esm ai config, 
        // then the mob ignore tool and level harvest restrictions
        if(!ESM_Settings.ZombieDiggerTools) {

            this.canHarvest = true;
            return this.canHarvest;
        }

        // If the mob will respect block breaking properties 
        // (tool and harvest level restriction) else they will be able to harvest
        // almost every block by hand
        if(ModConfig.AI.Digging.mustHaveCorrectTool.getValue()) {
            this.canHarvest = canHarvestBlock(pos);
        }
        else {
            this.canHarvest = true;
        }
        return this.canHarvest;
	}
}