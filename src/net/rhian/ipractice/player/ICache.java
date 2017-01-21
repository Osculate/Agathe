package net.rhian.ipractice.player;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.rhian.ipractice.Practice;
import net.rhian.ipractice.exception.PracticeException;
import net.rhian.ipractice.player.cache.AbstractCache;
import net.rhian.ipractice.player.cache.CachePlayer;

public class ICache extends AbstractCache {

    protected static boolean instantiated = false;

    public ICache(Plugin plugin){
        super(plugin, IPlayer.class);
        if(!instantiated){
            instantiated = true;
        }
        else{
            throw new PracticeException("ICache instance already exists");
        }
    }

    public IPlayer getIPlayer(String name){
        CachePlayer cachePlayer = getBasePlayer(name);
        if(cachePlayer != null){
            return (IPlayer) cachePlayer;
        }
        return null;
    }

    public IPlayer getIPlayer(Player p){
        return getIPlayer(p.getName());
    }

    public void clearCache(){
        super.getPlayersMap().clear();
    }

    @Override
    public CachePlayer create(String name, String uuid) {
        return new IPlayer(name, uuid);
    }

    @Override
    public void init(final Player player, CachePlayer cachePlayer) {
        if(cachePlayer instanceof IPlayer){
            final IPlayer iPlayer = (IPlayer) cachePlayer;
            iPlayer.setPlayer(player);
            iPlayer.setup();
            //todo

            new BukkitRunnable(){
                @Override
                public void run() {
                    iPlayer.sendToSpawn();
                }
            }.runTaskLater(Practice.getPlugin(), 5L);



        }
    }
}
