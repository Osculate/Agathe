package net.rhian.agathe.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.rhian.agathe.Agathe;
import net.rhian.agathe.command.CmdArgs;
import net.rhian.agathe.command.Command;
import net.rhian.agathe.command.ICommand;
import net.rhian.agathe.ladder.Ladder;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.player.PlayerState;

/**
 * Created by 360 on 9/18/2015.
 */

@Command(name = "resetelo", usage = "/resetelo <player> <ladder|all>", minArgs = 2, playerOnly = false, permission = "practice.resetelo")
public class CommandResetElo implements ICommand {
    @Override
    public void onCommand(final CmdArgs cmdArgs) {
        cmdArgs.getSender().sendMessage(ChatColor.GRAY+"Searching database asynchronously...");
        new BukkitRunnable(){
            @Override
            public void run() {
                CommandSender s = cmdArgs.getSender();
                String player = cmdArgs.matchPlayer(0);
                IPlayer target = Agathe.getCache().getIPlayer(player);
                if(target != null){
                    if(cmdArgs.getArg(1).equalsIgnoreCase("all")){
                        for(Ladder ladder : Ladder.getLadders()){
                            target.setElo(ladder, IPlayer.DEFAULT_ELO);
                        }
                        target.update();
                        Player t = Bukkit.getPlayer(player);
                        if(t != null){
                            if(target.getState() == PlayerState.AT_SPAWN){
                                target.getScoreboard().update();
                            }
                            t.sendMessage(ChatColor.GREEN+"Your elo has been reset to "+IPlayer.DEFAULT_ELO+" for all ladders.");
                        }
                        s.sendMessage(ChatColor.GREEN + "You reset " + player + "'s elo to " + IPlayer.DEFAULT_ELO + " for all ladders.");

                    }
                    else{
                        Ladder ladder = Ladder.getLadder(cmdArgs.getArg(1));
                        if(ladder != null){
                            target.setElo(ladder, IPlayer.DEFAULT_ELO);
                            target.update();
                            Player t = Bukkit.getPlayer(player);
                            if(t != null){
                                if(target.getState() == PlayerState.AT_SPAWN){
                                    target.getScoreboard().update();
                                }
                                t.sendMessage(ChatColor.GREEN+"Your elo has been reset to "+IPlayer.DEFAULT_ELO+" for ladder: "+ladder.getName()+".");
                            }
                            s.sendMessage(ChatColor.GREEN+"You reset "+player+"'s elo to "+IPlayer.DEFAULT_ELO+" for ladder: "+ladder.getName()+".");
                        }
                        else{
                            s.sendMessage(ChatColor.RED+"Ladder '"+cmdArgs.getArg(1)+"' does not exist.");
                        }
                    }
                }
                else{
                    s.sendMessage(ChatColor.RED+"Player '"+player+"' does not exist in database. (case sensitive)");
                }
            }
        }.runTaskAsynchronously(Agathe.getPlugin());
    }
}
