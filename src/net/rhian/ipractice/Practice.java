package net.rhian.ipractice;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import lombok.Getter;
import net.rhian.ipractice.arena.ArenaManager;
import net.rhian.ipractice.command.CommandHandler;
import net.rhian.ipractice.configuration.IConfig;
import net.rhian.ipractice.database.DBManager;
import net.rhian.ipractice.event.EventManager;
import net.rhian.ipractice.ladder.Ladder;
import net.rhian.ipractice.listener.ChatListener;
import net.rhian.ipractice.listener.KitInvClose;
import net.rhian.ipractice.listener.Soup;
import net.rhian.ipractice.listener.StaffModeListener;
import net.rhian.ipractice.listener.WorldListener;
import net.rhian.ipractice.match.handle.MatchManager;
import net.rhian.ipractice.party.PartiesInv;
import net.rhian.ipractice.party.PartyManager;
import net.rhian.ipractice.player.ICache;
import net.rhian.ipractice.player.IPlayer;
import net.rhian.ipractice.queue.QueueManager;
import net.rhian.ipractice.spawn.Spawn;
import net.rhian.ipractice.task.TaskAutoSave;
import net.rhian.ipractice.task.TaskClearEntities;

public class Practice extends JavaPlugin {

    @Getter private static Plugin plugin;
    @Getter private static ICache cache;
    @Getter private static DBManager dbManager;
    @Getter private static IConfig iConfig;
    @Getter private static ArenaManager arenaManager;
    @Getter private static CommandHandler commandHandler;
    @Getter private static Spawn spawn;
    @Getter private static PartyManager partyManager;
    @Getter private static MatchManager matchManager;
    @Getter private static ProtocolManager protocolManager;
    @Getter private static EventManager eventManager;
    @Getter private static QueueManager queueManager;
    @Getter private static TaskAutoSave taskAutoSave;
    @Getter private static WorldEditPlugin worldEdit;

    @Override
    public void onEnable(){
        plugin = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        dbManager = new DBManager(this);
        cache = new ICache(this);
        iConfig = new IConfig(this);//load & save called within the constructor
        commandHandler = new CommandHandler(this);
        spawn = new Spawn(this);
        partyManager = new PartyManager(this);
        matchManager = new MatchManager(this);
        arenaManager = new ArenaManager(this);
        eventManager = new EventManager(this);
        queueManager = new QueueManager(this);
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
            if(queueManager.inQueue(Practice.getCache().getIPlayer(pl))){
                queueManager.removeFromQueue(Practice.getCache().getIPlayer(pl));
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
