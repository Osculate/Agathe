package net.rhian.agathe.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.rhian.agathe.Agathe;
import net.rhian.agathe.command.CmdArgs;
import net.rhian.agathe.command.Command;
import net.rhian.agathe.command.ICommand;
import net.rhian.agathe.kite.KiteMatch;
import net.rhian.agathe.kite.KiteRequest;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.player.PlayerState;

@Command(name = "kiteaccept", usage = "/kiteaccept <player>", minArgs = 1, playerOnly = true)
public class CommandKiteAccept implements ICommand {

    @Override
    public void onCommand(CmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        IPlayer ip = Agathe.getCache().getIPlayer(p);
        if(ip.isStaffMode()){
            p.sendMessage(ChatColor.RED+"You cannot do this while in staff mode.");
            return;
        }
        if(ip.getState() == PlayerState.AT_SPAWN){
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
                IPlayer tip = Agathe.getCache().getIPlayer(t);
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

                KiteRequest req = null;

                for(KiteRequest request : ip.getKiteRequests()){
                    if(request.getSender().getName().equalsIgnoreCase(t.getName())){
                        if(request.getExpiry() >= System.currentTimeMillis()){
                            req = request;
                            break;
                        }
                    }
                }

                if(req != null){
                    ip.getKiteRequests().remove(req);

                    KiteMatch match = new KiteMatch(Agathe.getCache().getIPlayer(req.getRunner())
                                                    ,Agathe.getCache().getIPlayer(req.getChaser()));
                    match.startMatch(Agathe.getMatchManager());
                }
                else{
                    p.sendMessage(ChatColor.RED+"You do not have a pending duel request from that player.");
                }

            }
            else{
                p.sendMessage(ChatColor.RED+"Could not find player '"+cmdArgs.getArg(0)+"'.");
            }
        }
        else{
            p.sendMessage(ChatColor.RED+"You are not at spawn.");
        }
    }
}
