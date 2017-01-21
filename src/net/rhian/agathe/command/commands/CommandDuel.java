package net.rhian.agathe.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.rhian.agathe.Agathe;
import net.rhian.agathe.command.CmdArgs;
import net.rhian.agathe.command.Command;
import net.rhian.agathe.command.ICommand;
import net.rhian.agathe.ladder.Ladder;
import net.rhian.agathe.ladder.LadderSelect;
import net.rhian.agathe.match.DuelRequest;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.player.PlayerState;

@Command(name = "duel", playerOnly = true, minArgs = 1, usage = "/duel <player>")
public class CommandDuel implements ICommand {

    @Override
    public void onCommand(CmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        final IPlayer ip = Agathe.getCache().getIPlayer(p);

        if(ip.isStaffMode()){
            p.sendMessage(ChatColor.RED+"You cannot do this while in staff mode.");
            return;
        }

        if(ip.getState() == PlayerState.AT_SPAWN){
            if(ip.getDuelRequestCooldown() < System.currentTimeMillis()){

                if(Agathe.getQueueManager().inQueue(ip)){
                    p.sendMessage(ChatColor.RED+"You cannot do this while you are in a queue.");
                    return;
                }

                Player t = cmdArgs.getPlayer(0);
                if(t != null){
                    if(t.getName().equalsIgnoreCase(p.getName())){
                        p.sendMessage(ChatColor.RED+"You cannot duel yourself.");
                        return;
                    }
                    if(ip.getParty() != null){
                        p.sendMessage(ChatColor.RED+"You cannot do this while you are in a party.");
                        return;
                    }
                    final IPlayer tip = Agathe.getCache().getIPlayer(t);
                    if(tip.isStaffMode()){
                        p.sendMessage(ChatColor.RED+"Could not find player '"+cmdArgs.getArg(0)+"'.");
                        return;
                    }
                    if(tip.getState() != PlayerState.AT_SPAWN){
                        p.sendMessage(ChatColor.RED+"That player is not at spawn.");
                        return;
                    }
                    if(tip.getParty() != null){
                        p.sendMessage(ChatColor.RED+"That player is in a party.");
                        return;
                    }
                    if(Agathe.getQueueManager().inQueue(tip)){
                        p.sendMessage(ChatColor.RED+"That player is in a queue.");
                        return;
                    }

                    new LadderSelect(ip){
                        @Override
                        public void onSelect(Ladder ladder) {
                            DuelRequest duelRequest = new DuelRequest(ladder, ip, tip, System.currentTimeMillis());
                            duelRequest.send();
                        }
                    };

                }
                else{
                    p.sendMessage(ChatColor.RED+"Could not find player '"+cmdArgs.getArg(0)+"'.");
                }
            }
            else{
                p.sendMessage(ChatColor.RED+"Slow down.  You can send another duel request in "+
                        ((ip.getDuelRequestCooldown()-System.currentTimeMillis())/1000)+" seconds.");
            }
        }
        else{
            p.sendMessage(ChatColor.RED+"You are not at spawn.");
        }

    }

}
