package lirkas.esmtweaks.event;


public interface IEventHandler {

    public void registerAllEventHandlers();
    public void unregisterAllEventHandlers();
    public void registerEventHandler(Object eventHandler);
    public void unregisterEventHanlder(Object eventHandler);
}
