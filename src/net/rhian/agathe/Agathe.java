package net.rhian.agathe;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import lombok.Getter;
import net.rhian.agathe.arena.ArenaManager;
import net.rhian.agathe.command.CommandHandler;
import net.rhian.agathe.configuration.IConfig;
import net.rhian.agathe.database.ConnectionManager;
import net.rhian.agathe.event.EventManager;
import net.rhian.agathe.ladder.Ladder;
import net.rhian.agathe.listener.ChatListener;
import net.rhian.agathe.listener.KitInvClose;
import net.rhian.agathe.listener.Soup;
import net.rhian.agathe.listener.StaffModeListener;
import net.rhian.agathe.listener.WorldListener;
import net.rhian.agathe.match.handle.MatchManager;
import net.rhian.agathe.party.PartiesInv;
import net.rhian.agathe.party.PartyManager;
import net.rhian.agathe.player.ICache;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.queue.matchmaking.unranked.UnrankedManager;
import net.rhian.agathe.spawn.Spawn;
import net.rhian.agathe.task.TaskAutoSave;
import net.rhian.agathe.task.TaskClearEntities;

public class Agathe extends JavaPlugin {

    @Getter private static Plugin plugin;
    @Getter private static ICache cache;
    @Getter private static ConnectionManager dbManager;
    @Getter private static IConfig iConfig;
    @Getter private static ArenaManager arenaManager;
    @Getter private static CommandHandler commandHandler;
    @Getter private static Spawn spawn;
    @Getter private static PartyManager partyManager;
    @Getter private static MatchManager matchManager;
    @Getter private static ProtocolManager protocolManager;
    @Getter private static EventManager eventManager;
    @Getter private static UnrankedManager queueManager;
    @Getter private static TaskAutoSave taskAutoSave;
    @Getter private static WorldEditPlugin worldEdit;

    @Override
    public void onEnable(){
        plugin = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        dbManager = new ConnectionManager(this);
        cache = new ICache(this);
        iConfig = new IConfig(this);//load & save called within the constructor
        commandHandler = new CommandHandler(this);
        spawn = new Spawn(this);
        partyManager = new PartyManager(this);
        matchManager = new MatchManager(this);
        arenaManager = new ArenaManager(this);
        eventManager = new EventManager(this);
        queueManager = new UnrankedManager(this);
        queueManager.run();
        worldEdit = (WorldEditPlugin) getServer().getPluginManager().getPlugin("WorldEdit");
        taskAutoSave = new TaskAutoSave();
        Ladder.loadLadders(this);
        getServer().getPluginManager().registerEvents(new KitInvClose(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);
        getServer().getPluginManager().registerEvents(new ChatListener(),this);
        getServer().getPluginManager().registerEvents(new PartiesInv(),this);
        getServer().getPluginManager().registerEvents(new StaffModeListener(), this);
        getServer().getPluginManager().registerEvents(new Soup(), this);
        getServer().getScheduler().runTaskTimerAsynchronously(this, taskAutoSave, 15000, 15000);
        getServer().getScheduler().runTaskTimer(this, new TaskClearEntities(), 600, 600);
    }

    @Override
    public void onDisable(){
        taskAutoSave.run();
        if(!eventManager.canStartEvent()){
            eventManager.endEvent();
        }
        
        for(Player pl : Bukkit.getOnlinePlayers()){
            if(queueManager.inQueue(Agathe.getCache().getIPlayer(pl))){
                queueManager.removeFromQueue(Agathe.getCache().getIPlayer(pl));
            }
            
            IPlayer ip = getCache().getIPlayer(pl);
            ip.update();
        }

        cache.clearCache();
        dbManager.shutdown();
        iConfig.save();
        cache = null;
        dbManager = null;
        commandHandler.getCommands().clear();
        commandHandler = null;
        spawn = null;
        partyManager.getParties().clear();
        partyManager = null;
        iConfig = null;
        matchManager = null;
        arenaManager.getArenas().clear();
        arenaManager = null;
        eventManager = null;

        plugin = null;
    }

}
