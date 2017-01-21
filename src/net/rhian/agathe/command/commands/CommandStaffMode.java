package net.rhian.agathe.command.commands;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import net.rhian.agathe.Agathe;
import net.rhian.agathe.command.CmdArgs;
import net.rhian.agathe.command.Command;
import net.rhian.agathe.command.ICommand;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.player.PlayerState;
import net.rhian.agathe.util.ItemBuilder;

/**
 * Created by 360 on 9/7/2015.
 */
@Command(name = "staffmode", playerOnly = true, permission = "practice.staffmode")
public class CommandStaffMode implements ICommand {

    private static final Random RANDOM = new Random();

    public CommandStaffMode() {
        new BukkitRunnable(){
            @Override
            public void run() {
                Player[] players = new Player[Bukkit.getOnlinePlayers().size()];
                int i = 0;
                for(Player pl : Bukkit.getOnlinePlayers()){
                    IPlayer ip = Agathe.getCache().getIPlayer(pl);
                    if(!ip.isStaffMode()){
                        players[i] = pl;
                        i++;
                    }
                }
                for(Player pl : Bukkit.getOnlinePlayers()){
                    IPlayer ip = Agathe.getCache().getIPlayer(pl);
                    if(ip.isStaffMode() && ip.isStaffTeleportShuffle()){
                        int next = RANDOM.nextInt(players.length);
                        if(players.length >= next){
                            if(players[next] != null) {
                                pl.teleport(players[next]);
                                pl.sendMessage(ChatColor.YELLOW + "Teleported to: " + ChatColor.GRAY + players[next].getName());
                            }
                            else{
                                pl.sendMessage(ChatColor.YELLOW+"No players to teleport to, disabling teleport shuffle.");
                                ip.setStaffTeleportShuffle(false);
                            }
                        }
                        else{
                            pl.sendMessage(ChatColor.YELLOW+"No players to teleport to, disabling teleport shuffle.");
                            ip.setStaffTeleportShuffle(false);
                        }
                    }
                }
            }
        }.runTaskTimer(Agathe.getPlugin(), 100L, 100L);
    }

    @Override
    public void onCommand(CmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        IPlayer ip = Agathe.getCache().getIPlayer(p);

        if(Agathe.getQueueManager().inQueue(ip)){
            p.sendMessage(ChatColor.RED+"You cannot do this while you are in a queue.");
            return;
        }

        if(ip.getParty() != null){
            p.sendMessage(ChatColor.RED+"You cannot do this while you are in a party.");
            return;
        }

        if(!ip.isStaffMode() && ip.getState() != PlayerState.AT_SPAWN){
            p.sendMessage(ChatColor.RED+"You must be at spawn to enable staff mode.");
            return;
        }

        ip.setStaffMode(!ip.isStaffMode());


        p.sendMessage(ChatColor.YELLOW + "Staff Mode " + ChatColor.GRAY + "- " +
                (ip.isStaffMode() ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled"));
        ip.handlePlayerVisibility();
        if(!ip.isStaffMode()){
            ip.setStaffTeleportShuffle(false);
            ip.sendToSpawn();
        }
        else{
            giveItems(p);
        }
        ip.getScoreboard().update();
    }

    private void giveItems(Player p){
        p.setGameMode(GameMode.SURVIVAL);
        p.setAllowFlight(true);
        p.setFlying(true);
        p.teleport(Agathe.getIConfig().getSpawn());
        p.setHealth(20);
        p.setFoodLevel(20);
        for(PotionEffect po : p.getActivePotionEffects())
            p.removePotionEffect(po.getType());

        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        //Give items
        p.getInventory().setItem(0, new ItemBuilder(Material.BOOK).name(ChatColor.GRAY+"Inspect Player").build());
        p.getInventory().setItem(8, new ItemBuilder(Material.EYE_OF_ENDER).name(ChatColor.GRAY+"Random Teleport").build());

        p.updateInventory();
    }


}
