package lirkas.esmtweaks.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;


public class AltEntityAIZombieAttack extends AltEntityAIAttackMelee {
    
    protected final EntityZombie zombie;
    protected int raiseArmTicks;


    public AltEntityAIZombieAttack(EntityZombie zombie, double moveSpeed, boolean useLongMemory) {
        super((EntityLiving)zombie, moveSpeed, useLongMemory);
        this.zombie = zombie;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        this.raiseArmTicks = 0;
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.zombie.setArmsRaised(false);
    }

    @Override
    public void updateTask() {
        super.updateTask();
        ++this.raiseArmTicks;
        if (this.raiseArmTicks >= 5 && this.getAttackTick() < 10) {
            this.zombie.setArmsRaised(true);
        } 
        else {
            this.zombie.setArmsRaised(false);
        }
    }
}