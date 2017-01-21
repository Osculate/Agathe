package net.rhian.ipractice.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import net.rhian.ipractice.ladder.Ladder;

public class KitInvClose implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        Ladder ladder = Ladder.getLadder(e.getInventory().getName());
        if(ladder != null){
            ladder.getInventory().setContents(e.getInventory().getContents());
            ladder.save();
            if(e.getPlayer() instanceof Player){
                ((Player)e.getPlayer()).sendMessage(ChatColor.GREEN+"Kit Items Saved.");
            }
        }
    }

}
