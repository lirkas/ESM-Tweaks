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
     * @param showPriority If true, the task priority will be included.
     * @param areTargetTasks If true, the returned Tasks will be TargetTasks instead.
     * @return A set containing names of the this entity AI Tasks.
     */
    public static Set<String> getAITasks(EntityLiving entityLiving, boolean fullName, boolean showPriority, boolean areTargetTasks) {

        Set<String> taskNames = new LinkedHashSet<String>();
        String formattedTaskText = "?";
        Set<EntityAITaskEntry> taskEntries = areTargetTasks ? entityLiving.targetTasks.taskEntries 
                : entityLiving.tasks.taskEntries;
 
        for(EntityAITaskEntry taskEntry : taskEntries) {
            formattedTaskText = showPriority ? "(" + taskEntry.priority + ") " : "";
            formattedTaskText += fullName ? taskEntry.action.getClass().getName() 
                    : taskEntry.action.getClass().getSimpleName();
            taskNames.add(formattedTaskText);
        }
        return taskNames;
    }
    public static Set<String> getAITasks(EntityLiving entityLiving, boolean fullName, boolean showPriority) {
        return EntityUtil.getAITasks(entityLiving, fullName, true, false);
    }
    public static Set<String> getAITasks(EntityLiving entityLiving, boolean fullName) {
        return EntityUtil.getAITasks(entityLiving, fullName, true);
    }
    public static Set<String> getAITasks(EntityLiving entityLiving) {
        return EntityUtil.getAITasks(entityLiving, true);
    }

    /**
     * Gets all AI TargetTask names assigned to this entity.
     * @see getAITasks
     */
    public static Set<String> getAITargetTasks(EntityLiving entityLiving, boolean fullName, boolean showPriority) {
        return EntityUtil.getAITasks(entityLiving, fullName, true, true);
    }
    public static Set<String> getAITargetTasks(EntityLiving entityLiving) {
        return EntityUtil.getAITargetTasks(entityLiving, true, true);
    }
}