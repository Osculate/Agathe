package net.rhian.agathe.event;

import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import lombok.Getter;
import lombok.Setter;
import net.rhian.agathe.Agathe;
import net.rhian.agathe.exception.PracticeEventException;
import net.rhian.agathe.player.IPlayer;

public abstract class PracticeEvent implements Listener {
    private final Agathe practice;
    private final EventType eventType;
    private boolean registeredListener = false;
    @Getter @Setter private boolean running = false;
    
    public PracticeEvent(Agathe practice, EventType eventType) {
        this.practice = practice;
        this.eventType = eventType;
    }

    public final void registerListener(){
        if(!registeredListener) {
            practice.getServer().getPluginManager().registerEvents(this, practice);
            registeredListener = true;
        } else {
            throw new PracticeEventException("Attempted to register event listener while already registered");
        }
    }

    public final void unregisterListener(){
        if(registeredListener){
            HandlerList.unregisterAll(this);
            registeredListener = false;
        } else {
            throw new PracticeEventException("Attempted to unregister event listener when not already registered");
        }
    }

    public abstract void startEvent();
    public abstract void endEvent();
    public abstract void addPlayer(IPlayer player);
    public abstract boolean hasPlayer(IPlayer player);
    public String getName(){
        return eventType.getName();
    }
}
