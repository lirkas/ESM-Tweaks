package lirkas.esmtweaks.ai.modifier;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;

import funwayguy.epicsiegemod.ai.modifiers.ModifierAttackMelee;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.ai.AltEntityAIAttackMelee;
import lirkas.esmtweaks.config.ModConfig;
import lirkas.esmtweaks.util.ReflectUtil;


public class MeleeAttackAITaskModifier extends ModifierAttackMelee {
    
    public static ReflectUtil.WrappedField<EntityAIAttackMelee, Boolean> longMemoryField;
    public static ReflectUtil.WrappedField<EntityAIAttackMelee, Double> moveSpeedField;

    static {
        longMemoryField = new ReflectUtil.WrappedField<>(EntityAIAttackMelee.class, "longMemory", "field_75437_f");
        moveSpeedField = new ReflectUtil.WrappedField<>(ModifierAttackMelee.f_speed);
    }

    public static boolean useLongMemory(EntityLiving host, EntityAIBase task) {
        boolean useLongMemory = true;
        if(task instanceof EntityAIAttackMelee && ModConfig.AI.Attack.Melee.forceLongMemory.getValue() == false) {
            useLongMemory = longMemoryField.getValue((EntityAIAttackMelee)task, useLongMemory);
        }
        ESMTweaks.logger.debug("useLongMemory : " + useLongMemory);
        return useLongMemory;
    }

    public static double getmoveSpeed(EntityLiving host, EntityAIBase task) {
        double moveSpeed = 1.0;
        if(task instanceof EntityAIAttackMelee && true) {
            moveSpeed = moveSpeedField.getValue((EntityAIAttackMelee)task, moveSpeed);
        }
        return moveSpeed;
    }

    @Override
    public boolean isValid(EntityLiving entityLiving, EntityAIBase task) {
        return super.isValid(entityLiving, task);
    }
    
    @Override
    public EntityAIBase getReplacement(EntityLiving host, EntityAIBase entry) {
        return new AltEntityAIAttackMelee(host, 
            MeleeAttackAITaskModifier.getmoveSpeed(host, entry), 
            MeleeAttackAITaskModifier.useLongMemory(host, entry)
        );
    }
}