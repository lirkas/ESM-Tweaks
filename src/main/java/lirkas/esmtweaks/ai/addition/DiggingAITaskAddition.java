package lirkas.esmtweaks.ai.addition;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

import funwayguy.epicsiegemod.api.ITaskAddition;
import funwayguy.epicsiegemod.config.props.CfgProps;

import lirkas.esmtweaks.ai.AltEntityAIDigging;


public class DiggingAITaskAddition implements ITaskAddition {

	@Override
	public boolean isTargetTask() {
		return false;
	}
	
	@Override
	public int getTaskPriority(EntityLiving entityLiving) {
		return 4;
	}
	
	@Override
	public boolean isValid(EntityLiving entityLiving) {
        return CfgProps.DIGGING.get(entityLiving);
	}
	
	@Override
	public EntityAIBase getAdditionalAI(EntityLiving entityLiving) {	
		return new AltEntityAIDigging(entityLiving);
	}
}