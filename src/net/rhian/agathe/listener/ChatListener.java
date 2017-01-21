package net.rhian.agathe.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.rhian.agathe.Agathe;
import net.rhian.agathe.player.IPlayer;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onChat(AsyncPlayerChatEvent e){
        Player p = e.getPlayer();
        IPlayer ip = Agathe.getCache().getIPlayer(p);
        if(e.getMessage().startsWith("@") && ip.getParty() != null && e.getMessage().length() >= 2){
            ip.getParty().msg(ChatColor.AQUA+""+ChatColor.BOLD+"(PARTY) "+ChatColor.RESET+""
                    +ChatColor.LIGHT_PURPLE+p.getName()+ChatColor.GRAY+": "+ChatColor.GOLD+e.getMessage().substring(1, e.getMessage().length()));
            e.setCancelled(true);
        }
    }

}
