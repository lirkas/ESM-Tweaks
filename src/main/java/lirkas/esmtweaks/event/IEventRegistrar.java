package lirkas.esmtweaks.event;


public interface IEventRegistrar {

    public void registerAllEventHandlers();
    public void unregisterAllEventHandlers();
    public void registerEventHandler(Object eventHandler);
    public void unregisterEventHanlder(Object eventHandler);
}
