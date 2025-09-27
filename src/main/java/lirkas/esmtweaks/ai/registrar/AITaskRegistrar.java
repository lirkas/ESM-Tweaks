package lirkas.esmtweaks.ai.registrar;

import java.util.List;
import java.util.ListIterator;

import funwayguy.epicsiegemod.api.TaskRegistry;
import funwayguy.epicsiegemod.api.ITaskAddition;

import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.ai.addition.DiggingAITaskAddition;


public class AITaskRegistrar {

    /**
     * Registers an ITaskAddition, such as an specific class that applies AI on mob spawn.
     * @param task The task to register.
     */
    public static void registerTask(ITaskAddition task) {
        ESMTweaks.logger.debug("Registering task : " + task.getClass().getSimpleName());
        TaskRegistry.INSTANCE.registerTaskAddition(task);
    }

    /**
     * Registers all tasks added by this mod.
     * @see registerTask
     */
    public static void registerAll() {
        registerTask(new DiggingAITaskAddition());
    }

    /**
     * Unregisters/Removes an ESM-like TaskAddition added previously.
     * @param taskClass The "ITaskAddition" implemented class that will be removed from the list.
     * @param allMatches Removes all tasks that match 'taskClass' if set to true, else only removes the first match.
     */
    public static void unregisterTasks(Class<? extends ITaskAddition> taskClass, boolean allMatches) {

        List<ITaskAddition> additions = TaskRegistry.INSTANCE.getAllAdditions();

        for(ListIterator<ITaskAddition> iterator = additions.listIterator(); iterator.hasNext(); ) {
            ITaskAddition taskAddition = iterator.next();
            // if the ai task addition matches the class - remove it
            if(taskClass.isInstance(taskAddition)) {
                iterator.remove();
                ESMTweaks.logger.debug("Unregistered task : " + taskClass.getSimpleName());
                // exit the for loop here if we only remove the first match
                if(!allMatches) {
                    break;
                }
            }
        }
    }

    public static void unregisterTasks(Class<? extends ITaskAddition> taskClass) {
        unregisterTasks(taskClass, true);
    }
    
    /**
     * @see unregisterTasks
     */
    public static void unregisterTask(Class<? extends ITaskAddition> taskClass) {
        unregisterTasks(taskClass, false);
    }
    
}