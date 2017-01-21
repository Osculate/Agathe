package net.rhian.agathe.event;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import net.rhian.agathe.Agathe;
import net.rhian.agathe.event.events.EventLMS;
import net.rhian.agathe.exception.PracticeEventException;

public class EventManager {
    private final Map<EventType, PracticeEvent> events = new HashMap<>();
    @Getter private PracticeEvent activeEvent = null;

    public EventManager(Agathe instance) {
        events.put(EventType.LMS, new EventLMS(instance));
    }

    public PracticeEvent getEvent(EventType type){
        return events.get(type);
    }

    public boolean hasEvent(EventType type){
        return events.containsKey(type);
    }

    public boolean canStartEvent(){
        return activeEvent == null;
    }

    public void startEvent(PracticeEvent event){
        if(activeEvent != null){
            throw new PracticeEventException("Can not start an event when one is already running");
        }
        this.activeEvent = event;
        event.registerListener();
        event.startEvent();
    }
    
    public void endEvent(){
        if(activeEvent != null){
            activeEvent.endEvent();
            activeEvent.unregisterListener();
        } else {
            throw new PracticeEventException("Can not end an event when none is running");
        }
    }
}
