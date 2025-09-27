package lirkas.esmtweaks.event.registrar;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import tyra314.toolprogression.handlers.HarvestEventHandler;
import lirkas.esmtweaks.ESMTweaks;
import lirkas.esmtweaks.event.handler.ConfigEventHandler;
import lirkas.esmtweaks.event.handler.EntityEventHandler;

/**
 * Central place for EventHandlers un/registration management.
 */
public class CommonEventHandlerRegistrar implements IEventHandlerRegistrar {

    public static final CommonEventHandlerRegistrar INSTANCE = new CommonEventHandlerRegistrar();

    @Override
    public void registerAllEventHandlers() {
        ESMTweaks.logger.debug("CommonEventHandler registerAllEventHandlers");
        MinecraftForge.EVENT_BUS.register(new ConfigEventHandler());
        MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
        MinecraftForge.EVENT_BUS.register(new HarvestEventHandler());
    }

    @Override
    public void unregisterAllEventHandlers() {
        ESMTweaks.logger.debug("CommonEventHandler unregisterAllEventHandlers");
    }

    @Override
    public void registerEventHandler(Object eventHandler) {
        ESMTweaks.logger.debug("CommonEventHandler registerEventHandler");
        MinecraftForge.EVENT_BUS.register(eventHandler);
    }

    @Override
    public void unregisterEventHanlder(Object eventHandler) {
        ESMTweaks.logger.debug("CommonEventHandler unregisterEventHanlder");
        MinecraftForge.EVENT_BUS.unregister(eventHandler);
    }

    /**
     * Hacky way to unregister an event when we dont have access to the eventHandler object reference.
     * Should be avoid if a cleaner and safer solution is available.
     *  
     * @param classpath The name of the class path which contains event handlers (i.e. "dev.badmod.handler.BMEventHandler")
     * @param methodName The name of the method handling the event (i.e. "onEntityConstruct")
     * @param eventClass The class of the event being handled by the handler (i.e. PlayerEvent.class)
     */
    public void unregisterEventHanlder(String classpath, String methodName, Class<? extends Event> eventClass) {
        
        Event tmpEvent;

        try {
            tmpEvent = eventClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            ESMTweaks.logger.error("unregisterEventHanlder : cannot instanciate " + eventClass.getSimpleName());
            e.printStackTrace();
            return;
        }

        if(tmpEvent == null) {
            ESMTweaks.logger.error("unregisterEventHanlder : tmpEvent == null");
            return;
        }

        IEventListener[] listeners = tmpEvent.getListenerList().getListeners(0);
        for(IEventListener listener : listeners) {
            
            if(listener.toString().contains(classpath) && listener.toString().contains(methodName)) {

                // only need one of these two, ideally the first one.
                //MinecraftForge.EVENT_BUS.unregister(listener);
                tmpEvent.getListenerList().unregister(0, listener);
                
                ESMTweaks.logger.debug("unregisterEventHanlder : unregistered " + classpath + ":" + methodName);
                return;
            }
        }

        ESMTweaks.logger.info("unregisterEventHanlder : did not find anything to unregister");
    }
}