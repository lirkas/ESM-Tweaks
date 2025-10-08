package lirkas.esmtweaks.util;

import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;


public class EntityUtil {
    
    /**
     * Gets all AI task names assigned to this entity.
     * 
     * @param entityLiving The entity to get AI tasks from.
     * @param fullName If true, the full classpath of the AI class is shown, else only the name.
     * @return A set containing names of the this entity AI Tasks.
     */
    public static Set<String> getAITasks(EntityLiving entityLiving, boolean fullName) {

        Set<String> taskNames = new LinkedHashSet<String>();

        for(EntityAITaskEntry taskEntry : entityLiving.tasks.taskEntries) {
            taskNames.add(fullName ? 
                taskEntry.action.getClass().getName() : 
                taskEntry.action.getClass().getSimpleName()
            );
        }
        return taskNames;
    } 
}
