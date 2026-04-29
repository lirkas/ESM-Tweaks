package lirkas.esmtweaks.ai.registrar;

import java.util.List;

import funwayguy.epicsiegemod.api.TaskRegistry;
import funwayguy.epicsiegemod.api.ITaskAddition;
import funwayguy.epicsiegemod.api.ITaskModifier;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.ai.addition.DiggingAITaskAddition;
import lirkas.esmtweaks.util.Util;


public class AITaskRegistrar {

    /**
     * Registers an ITaskAddition, such as an specific class that applies AI on mob spawn.
     * @param taskAddition The task to register.
     */
    public static void registerTask(ITaskAddition taskAddition) {
        ESMTweaks.logger.debug("Registering taskAddition : " + taskAddition.getClass().getSimpleName());
        TaskRegistry.INSTANCE.registerTaskAddition(taskAddition);
    }

    /**
     * Registers an ITaskModifier, such as an specific class that applies AI on mob spawn.
     * @param taskModifier The task to register.
     */
    public static void registerTask(ITaskModifier taskModifier) {
        ESMTweaks.logger.debug("Registering taskModifier : " + taskModifier.getClass().getSimpleName());
        TaskRegistry.INSTANCE.registerTaskModifier(taskModifier);
    }

    /**
     * Registers all tasks added by this mod.
     * @see registerTask
     */
    @Deprecated
    public static void registerAll() {
        registerTask(new DiggingAITaskAddition());
    }

    /**
     * Unregisters/Removes existing TaskModifiers or TaskAdditions.
     * 
     * @param taskClass The "ITaskAddition" implemented class that will be removed from the list.
     * @param allMatches Removes all tasks that match 'taskClass' if set to true, else only removes the first match.
     */
    public static void unregisterTasks(Class<?> taskClass, boolean allMatches) {
    
        List<?> tasks;

        if(ITaskModifier.class.isAssignableFrom(taskClass)) {
            tasks = TaskRegistry.INSTANCE.getAllModifiers();
            ESMTweaks.logger.debug("Unregistering TaskModifier : " + taskClass.getSimpleName());
        }
        else if(ITaskAddition.class.isAssignableFrom(taskClass)) {
            tasks = TaskRegistry.INSTANCE.getAllAdditions();
            ESMTweaks.logger.debug("Unregistering TaskAddition : " + taskClass.getSimpleName());
        }
        else {
            ESMTweaks.logger.error("Cannot unregister " + taskClass.getSimpleName() + " : Invalid Task.");
            return;
        }
        Util.removeFromListByClass(tasks, taskClass, allMatches);
    }
    public static void unregisterTasks(Class<?> taskClass) {
        unregisterTasks(taskClass, true);
    }
    /**
     * @see #unregisterTasks
     */
    public static void unregisterTask(Class<?> taskClass) {
        unregisterTasks(taskClass, false);
    }

    /**
     * @see #unregisterTasks
     */
    public static void unregisterTaskAdditions(Class<? extends ITaskAddition> taskClass, boolean allMatches) {
        unregisterTasks(taskClass, allMatches);
    }
    public static void unregisterTaskAdditions(Class<? extends ITaskAddition> taskClass) {
        unregisterTaskAdditions(taskClass, true);
    }
    /**
     * @see #unregisterTaskAdditions
     */
    public static void unregisterTaskAddition(Class<? extends ITaskAddition> taskClass) {
        unregisterTaskAdditions(taskClass, false);
    }
    
    /**
     * @see #unregisterTasks
     */
    public static void unregisterTaskModifiers(Class<? extends ITaskModifier> taskClass, boolean allMatches) {
        unregisterTasks(taskClass, allMatches);
    }
    public static void unregisterTaskModifiers(Class<? extends ITaskModifier> taskClass) {
        unregisterTaskModifiers(taskClass, true);
    }
    /**
     * @see #unregisterTaskModifiers
     */
    public static void unregisterTaskModifier(Class<? extends ITaskModifier> taskClass) {
        unregisterTaskModifiers(taskClass, false);
    }
}