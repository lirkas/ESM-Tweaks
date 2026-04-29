package lirkas.esmtweaks.ai.modifier;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIBase;

import java.lang.reflect.Field;

import funwayguy.epicsiegemod.ai.modifiers.ModifierAttackMelee;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.ai.AltEntityAIAttackMelee;
import lirkas.esmtweaks.config.ModConfig;


public class MeleeAttackAITaskModifier extends ModifierAttackMelee {
    
    public static Field longMemory;
    
    static {
        try {
            MeleeAttackAITaskModifier.longMemory = EntityAIAttackMelee.class.getDeclaredField("field_75437_f");
            MeleeAttackAITaskModifier.longMemory.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException exceptionDeobf) {
            try {
                MeleeAttackAITaskModifier.longMemory = EntityAIAttackMelee.class.getDeclaredField("longMemory");
                MeleeAttackAITaskModifier.longMemory.setAccessible(true);
            } catch (NoSuchFieldException | SecurityException exception) {
                ESMTweaks.logger.error("Error while attempting to access 'longMemory' field", exception);
            }
        }
    }

    public static boolean useLongMemory(EntityLiving host, EntityAIBase task) {
        if(ModConfig.AI.Attack.Melee.forceLongMemory.getValue() == false) {
            try {
                ESMTweaks.logger.debug("useLongMemory : " + MeleeAttackAITaskModifier.longMemory.getBoolean(task));
                return MeleeAttackAITaskModifier.longMemory.getBoolean(task);
            } catch (IllegalArgumentException | IllegalAccessException exception) {
                ESMTweaks.logger.warn("Could not change longMemory for " + task.getClass().getSimpleName());
            }
        }
        return true;
    }

    public static double getmoveSpeed(EntityLiving host, EntityAIBase task) {
        if(true) {
            try {
                return ModifierAttackMelee.f_speed.getDouble(task);
            } catch (IllegalArgumentException | IllegalAccessException exception) {
                ESMTweaks.logger.warn("Could not change moveSpeed for " + task.getClass().getSimpleName());
            }
        }
        return 1.0;
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