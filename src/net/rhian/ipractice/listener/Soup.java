package net.rhian.ipractice.listener;

import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class Soup implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Damageable d = player;
        int hp = 6;
        double health = d.getHealth();
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getItem() != null && event.getItem().getType() == Material.MUSHROOM_SOUP) {			
				if (d.getHealth() >= d.getMaxHealth()) return;
				player.setHealth((health + hp > d.getMaxHealth()) ? d.getMaxHealth() : health + hp);
				player.getItemInHand().setType(Material.BOWL);
				player.updateInventory();
			}
		}
	}
}