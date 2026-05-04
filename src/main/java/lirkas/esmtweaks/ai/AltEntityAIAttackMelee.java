package lirkas.esmtweaks.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import funwayguy.epicsiegemod.ai.ESM_EntityAIAttackMelee;

import lirkas.esmtweaks.config.ModConfig;
import lirkas.esmtweaks.util.ReflectUtil;


public class AltEntityAIAttackMelee extends ESM_EntityAIAttackMelee {

    public static final int defaultAttackDelay = 20;
    public static boolean useCustomAttackDelay = true;
    public static int minAttackDelay = 10;
    public static int maxAttackDelay = 20;
    
    protected EntityLiving entity;
    public static ReflectUtil.WrappedField<ESM_EntityAIAttackMelee, Integer> attackTickField;
    protected int previousAttackTick = 0;
    protected boolean useLongMemory;

    static {
        attackTickField = new ReflectUtil.WrappedField<>(ESM_EntityAIAttackMelee.class, "attackTick");
    }

    // moveSpeed only affects the speed at which the mob runs toward its target
    public AltEntityAIAttackMelee(EntityLiving entity, double moveSpeed, boolean useLongMemory) {
        super(entity, moveSpeed, useLongMemory);
        this.entity = entity;
        this.useLongMemory = useLongMemory;
    }

    public boolean shouldContinueExecuting() {

        if(!ModConfig.AI.General.disableXRay.getValue() && ModConfig.AI.Attack.Melee.forceLongMemory.getValue()) {
            return super.shouldContinueExecuting();
        }

        EntityLivingBase target = this.entity.getAttackTarget();
        if (target == null) {
            return false;
        }
        else if (!target.isEntityAlive()) {
            return false;
        }
        else if (!this.useLongMemory) {
            return !this.entity.getNavigator().noPath();
        }
        else if (this.entity instanceof EntityCreature && 
                !((EntityCreature)this.entity).isWithinHomeDistanceFromPosition(new BlockPos(target))) {
            return false;
        }
        else {
            return !(target instanceof EntityPlayer) || 
                    !((EntityPlayer)target).isSpectator() && 
                    !((EntityPlayer)target).isCreative();
        }
    }

    @Override
    public void updateTask() {
        this.previousAttackTick = this.getAttackTick();
        super.updateTask();
  
        // If the current tick has just been reset from the parent method
        if(this.previousAttackTick <= 1 && this.getAttackTick() > 1) {
            // If the config allows to set custom delays and force all mobs to have long memory
            if(ModConfig.AI.Attack.Melee.useCustomAttackDelay.getValue() == true
                    && ModConfig.AI.Attack.Melee.forceLongMemory.getValue() == true) {
                this.resetAttackTick(AltEntityAIAttackMelee.minAttackDelay, AltEntityAIAttackMelee.maxAttackDelay);
            }
            else{
                this.setAttackTick(AltEntityAIAttackMelee.defaultAttackDelay);
            }
        }
    }

    /**
     * Sets a random time in ticks for the next attack to happen.
     * 
     * @param minDelay Minimum Amount of ticks before next attack (inclusive).
     * @param maxDelay Maximum Amount of ticks before next attack (inclusive).
     */
    protected void resetAttackTick(int minDelay, int maxDelay) {
        if(maxDelay > minDelay) {
            this.setAttackTick(minDelay + this.entity.getRNG().nextInt(maxDelay - minDelay));
        } 
        else {
            this.setAttackTick(minDelay);
        }
    }

    /**
     * Sets the time in ticks for the next attack to happen.
     * 
     * @param attackDelay Amount of ticks before next attack.
     */
    protected void setAttackTick(int attackDelay) {
        attackTickField.setValue(this, attackDelay);
    }

    /**
     * Retreives the value indicating how many ticks are left until the next attack can
     * be performed.
     */
    protected int getAttackTick() {
        return attackTickField.getValue(this, AltEntityAIAttackMelee.defaultAttackDelay);
    }
}