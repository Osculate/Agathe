package net.rhian.ipractice.task;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.rhian.ipractice.Practice;
import net.rhian.ipractice.player.IPlayer;

public class TaskAutoSave implements Runnable {

    @Override
    public void run() {
        int saved = 0;
        for(Player pl : Bukkit.getOnlinePlayers()){
            if(Practice.getCache().contains(pl.getName())){
                IPlayer iPlayer = Practice.getCache().getIPlayer(pl);
                iPlayer.update();
                saved++;
            }
        }
        for(Player pl : Bukkit.getOnlinePlayers()){
            if(pl.isOp() || pl.hasPermission("practice.verbose")){
                pl.sendMessage(ChatColor.DARK_PURPLE+"Saved "+saved+" players to the database.");
            }
        }
        Bukkit.getLogger().info(ChatColor.DARK_PURPLE+"Saved "+saved+" players to the database.");
    }
}
