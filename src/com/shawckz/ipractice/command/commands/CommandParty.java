package com.shawckz.ipractice.command.commands;

import com.shawckz.ipractice.Practice;
import com.shawckz.ipractice.command.CmdArgs;
import com.shawckz.ipractice.command.Command;
import com.shawckz.ipractice.command.ICommand;
import com.shawckz.ipractice.party.Party;
import com.shawckz.ipractice.player.IPlayer;
import com.shawckz.ipractice.player.PlayerState;

import net.mkremins.fanciful.FancyMessage;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Command(name = "party", playerOnly = true)
public class CommandParty implements ICommand {

    @Override
    public void onCommand(CmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        IPlayer ip = Practice.getCache().getIPlayer(p);
        String[] args = cmdArgs.getArgs();
        if(args.length == 0){
            p.sendMessage(ChatColor.GOLD+"Party Commands");
            p.sendMessage(ChatColor.AQUA+"/party create");
            p.sendMessage(ChatColor.AQUA+"/party leave");
            p.sendMessage(ChatColor.AQUA+"/party invite <player>");
            p.sendMessage(ChatColor.AQUA+"/party join <player>");
            p.sendMessage(ChatColor.AQUA+"/party kick <player>");
            p.sendMessage(ChatColor.AQUA+"/party chat <message>");
            return;
        }

        if(ip.isStaffMode()){
            p.sendMessage(ChatColor.RED+"You cannot do this while in staff mode.");
            return;
        }

        if(ip.getState() != PlayerState.AT_SPAWN){
            p.sendMessage(ChatColor.RED+"You are not at spawn.");
            return;
        }

        if(Practice.getQueueManager().inQueue(ip)){
            p.sendMessage(ChatColor.RED+"You cannot do this while you are in a queue.");
            return;
        }

        if(args[0].equalsIgnoreCase("create")){
            if(ip.getParty() == null){
                Party party = new Party(p.getName());
                Practice.getPartyManager().register(party);
                ip.sendToSpawnNoTp();
                p.sendMessage(ChatColor.AQUA+""+ChatColor.BOLD+"(PARTY) "+ChatColor.RESET+""+ChatColor.GOLD+"Party created!");
            }
            else{
                p.sendMessage(ChatColor.RED+"You are already in a party.");
            }
        }
        else if (args[0].equalsIgnoreCase("leave")){
            if(ip.getParty() != null){
                Party party = ip.getParty();
                if(!party.canDuel()){
                    p.sendMessage(ChatColor.RED+"You cannot do this while your party member(s) are not at spawn.");
                    return;
                }

                if(party.getLeader().equalsIgnoreCase(p.getName())){
                    party.msg(ChatColor.AQUA + "" + ChatColor.BOLD + "(PARTY) " + ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + p.getName() + ChatColor.GOLD + " deleted the party.");
                    Practice.getPartyManager().unregister(party);
                    ip.sendToSpawnNoTp();
                    for(Player pl : party.getAllPlayers()){
                        IPlayer ipl = Practice.getCache().getIPlayer(pl);
                        if(ipl.getState() == PlayerState.AT_SPAWN){
                            ipl.getPlayer().getInventory().clear();
                            ipl.getPlayer().getInventory().setContents(ipl.getPlayer().getInventory().getContents());
                            ipl.sendToSpawnNoTp();
                        }
                    }
                }
                else{
                    party.msg(ChatColor.AQUA+""+ChatColor.BOLD+"(PARTY) "+ChatColor.RESET+""+ChatColor.LIGHT_PURPLE + p.getName() + ChatColor.GOLD + " left the party.");
                    party.getMembers().remove(p.getName());
                    ip.sendToSpawn();
                }

            }
            else{
                p.sendMessage(ChatColor.RED+"You are not in a party.");
            }
        }
        else if (args[0].equalsIgnoreCase("invite")){
            if(ip.getParty() != null){
                if(args.length < 2){
                    p.sendMessage(ChatColor.RED+"Usage: /party invite <player>");
                    return;
                }
                Party party = ip.getParty();
                Player t = Bukkit.getPlayer(args[1]);
                if(t != null){
                    IPlayer tip = Practice.getCache().getIPlayer(t);
                    if(tip.getParty() == null){
                        if(tip.getState() == PlayerState.AT_SPAWN){
                            if(!party.getInvites().contains(t.getName())){
                                party.getInvites().add(t.getName());
                                FancyMessage fm = new FancyMessage("");
                                fm.then(ChatColor.AQUA + "" + ChatColor.BOLD + "(PARTY) " + ChatColor.RESET + "" + ChatColor.AQUA + p.getName() + ChatColor.GOLD + " has invited you to their party.");
                                fm.send(t);
                                new FancyMessage(ChatColor.GREEN+""+ChatColor.BOLD+"[CLICK HERE]")
                                        .command("/party join "+p.getName())
                                        .tooltip(ChatColor.GOLD + "Click to join " + ChatColor.AQUA + p.getName()
                                                + ChatColor.GOLD + "'s Party")
                                        .then(ChatColor.GOLD+" to accept.")
                                        .send(t);
                                party.msg(ChatColor.AQUA+""+ChatColor.BOLD+"(PARTY) "+ChatColor.RESET+""+ChatColor.LIGHT_PURPLE+p.getName()+ChatColor.GOLD+" invited "+ChatColor.AQUA
                                            +t.getName()+ChatColor.GOLD+" to the party.");
                            }
                            else{
                                p.sendMessage(ChatColor.RED+t.getName()+" has already been invited to the party.");
                            }
                        }
                        else{
                            p.sendMessage(ChatColor.RED+t.getName()+" is not at spawn.");
                        }
                    }
                    else{
                        p.sendMessage(ChatColor.RED+t.getName()+" is already in a party.");
                    }
                }
                else{
                    p.sendMessage(ChatColor.RED+"Could not find player '"+args[1]+"'.");
                }
            }
            else{
                p.sendMessage(ChatColor.RED+"You are not in a party.");
            }
        }
        else if (args[0].equalsIgnoreCase("join")){
            if(ip.getParty() == null){
                if(ip.getState() == PlayerState.AT_SPAWN){
                    if(args.length < 2){
                        p.sendMessage(ChatColor.RED+"Usage: /party join <player>");
                        return;
                    }
                    Player t = Bukkit.getPlayer(args[1]);
                    if(t != null){
                        IPlayer tip = Practice.getCache().getIPlayer(t);
                        if(tip.getParty() != null){
                            Party party = tip.getParty();
                            if(party.getInvites().contains(p.getName())){
                                party.getInvites().remove(p.getName());
                                party.getMembers().add(p.getName());
                                party.msg(ChatColor.AQUA+""+ChatColor.BOLD+"(PARTY) "+ChatColor.RESET+""+ChatColor.AQUA
                                        +p.getName()+ChatColor.GOLD+" has joined the party.");
                                ip.sendToSpawn();
                            }
                            else{
                                p.sendMessage(ChatColor.RED+"You have not been invited to this party.");
                            }
                        }
                        else{
                            p.sendMessage(ChatColor.RED+t.getName()+" is not in a party.");
                        }
                    }
                    else{
                        p.sendMessage(ChatColor.RED+"Could not find player '"+args[1]+"'.");
                    }

                }
                else{
                    p.sendMessage(ChatColor.RED+"You are not at spawn.");
                }
            }
            else{
                p.sendMessage(ChatColor.RED+"You are already in a party.");
            }
        }
        else if (args[0].equalsIgnoreCase("chat")){
            if(ip.getParty() != null){
                if(args.length < 2){
                    p.sendMessage(ChatColor.RED+"Usage: /party chat <message>");
                    return;
                }

                String msg = "";
                for(int i = 1; i < args.length; i++){
                    msg += args[i] + " ";
                }

                ip.getParty().msg(ChatColor.AQUA+""+ChatColor.BOLD+"(PARTY) "+ChatColor.RESET+""
                        +ChatColor.LIGHT_PURPLE+p.getName()+ChatColor.GRAY+": "+ChatColor.GOLD+msg);
            }
            else{
                p.sendMessage(ChatColor.RED+"You are not in a party.");
            }
        }
        else if (args[0].equalsIgnoreCase("kick")){
            if(ip.getParty() != null){
                if(args.length < 2){
                    p.sendMessage(ChatColor.RED+"Usage: /party kick <player>");
                    return;
                }
                Party party = ip.getParty();
                if(!party.getLeader().equalsIgnoreCase(p.getName())){
                    p.sendMessage(ChatColor.RED+"You are not the party leader.");
                    return;
                }
                Player t = Bukkit.getPlayer(args[1]);
                if(t != null){
                    IPlayer tip = Practice.getCache().getIPlayer(t);
                    if(tip.getParty() != null){
                        if(tip.getParty().getLeader().equalsIgnoreCase(party.getLeader())) {
                            tip.getPlayer().sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "(PARTY) "
                                    + ChatColor.RESET + "" + ChatColor.GOLD+" You were kicked from the party.");
                            party.msg(ChatColor.AQUA + "" + ChatColor.BOLD + "(PARTY) " + ChatColor.RESET + "" + ChatColor.AQUA +
                                    t.getName() + ChatColor.GOLD + " was kicked from the party.");
                            if (tip.getParty().getLeader().equalsIgnoreCase(party.getLeader())) {
                                party.getMembers().remove(t.getName());
                            }
                            if (tip.getState() == PlayerState.AT_SPAWN) {
                                tip.sendToSpawn();
                            }
                        }
                        else{
                            p.sendMessage(ChatColor.RED+t.getName()+" is not in your party.");
                        }
                    }
                    else{
                        p.sendMessage(ChatColor.RED+t.getName()+" is not in a party.");
                    }
                }
                else{
                    p.sendMessage(ChatColor.RED+"Could not find player '"+args[1]+"'.");
                }
            }
            else{
                p.sendMessage(ChatColor.RED+"You are not in a party.");
            }
        }
    }

    private void resetPlayers(Party party){
        for(Player p : party.getAllPlayers()){
            IPlayer ip = Practice.getCache().getIPlayer(p);
            if(ip.getState() == PlayerState.AT_SPAWN){
                ip.sendToSpawn();
            }
        }
    }

}
