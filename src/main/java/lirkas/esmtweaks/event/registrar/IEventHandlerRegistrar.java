package lirkas.esmtweaks.event.registrar;


public interface IEventHandlerRegistrar {

    public void registerAllEventHandlers();
    public void unregisterAllEventHandlers();
    public void registerEventHandler(Object eventHandler);
    public void unregisterEventHanlder(Object eventHandler);
}