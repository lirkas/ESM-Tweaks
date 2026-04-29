package lirkas.esmtweaks.ai.modifier;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.monster.EntityZombie;

import funwayguy.epicsiegemod.api.ITaskModifier;

import lirkas.esmtweaks.ai.AltEntityAIZombieAttack;


public class ZombieAttackAITaskModifier implements ITaskModifier {

    @Override
    public boolean isValid(EntityLiving entityLiving, EntityAIBase task) {
        return task != null && task.getClass() == EntityAIZombieAttack.class && entityLiving instanceof EntityZombie;
    }
    
    @Override
    public EntityAIBase getReplacement(EntityLiving host, EntityAIBase entry) {
        return new AltEntityAIZombieAttack((EntityZombie)host, 
            MeleeAttackAITaskModifier.getmoveSpeed(host, entry), 
            MeleeAttackAITaskModifier.useLongMemory(host, entry)
        );
    }
}