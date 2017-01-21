package net.rhian.ipractice.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.rhian.ipractice.Practice;
import net.rhian.ipractice.command.CmdArgs;
import net.rhian.ipractice.command.Command;
import net.rhian.ipractice.command.ICommand;
import net.rhian.ipractice.match.Match;
import net.rhian.ipractice.match.team.PracticeTeam;
import net.rhian.ipractice.match.team.Team;
import net.rhian.ipractice.party.Party;
import net.rhian.ipractice.party.PartyDuel;
import net.rhian.ipractice.player.IPlayer;
import net.rhian.ipractice.player.PlayerState;

@Command(name = "paccept", playerOnly = true, minArgs = 1, usage = "/paccept <player>")
public class CommandPAccept implements ICommand {

    @Override
    public void onCommand(CmdArgs cmdArgs) {
        Player p = (Player) cmdArgs.getSender();
        IPlayer ip = Practice.getCache().getIPlayer(p);

        if(ip.getState() != PlayerState.AT_SPAWN){
            p.sendMessage(ChatColor.RED+"You are not at spawn.");
            return;
        }

        if(ip.getParty() != null){
            if(ip.getParty().canDuel()){
                Party party = ip.getParty();
                final Player t = cmdArgs.getPlayer(0);
                if(t != null){
                    IPlayer tip = Practice.getCache().getIPlayer(t);
                    if(tip.getParty() != null){
                        Party tParty = tip.getParty();
                        if(tParty.canDuel()){
                            if(party.hasDuel(tParty)){
                                PartyDuel duel = party.getDuel(tParty);

                                Match match = Practice.getMatchManager().matchBuilder(duel.getLadder())
                                        .registerTeam(new PracticeTeam(duel.getSender().getLeader()+"'s Party", Team.ALPHA))
                                        .registerTeam(new PracticeTeam(duel.getRecipient().getLeader()+"'s Party", Team.BRAVO))
                                        .withParty(duel.getSender(), duel.getSender().getLeader()+"'s Party")
                                        .withParty(duel.getRecipient(), duel.getRecipient().getLeader()+"'s Party")
                                        .setRanked(false)
                                        .build();

                                match.startMatch(Practice.getMatchManager());

                            }
                            else{
                                p.sendMessage(ChatColor.RED+"That party has not challenged you to a duel.");
                            }
                        }
                        else{
                            p.sendMessage(ChatColor.RED+"One of more of that party's members are not at spawn.");
                        }
                    }
                    else{
                        p.sendMessage(ChatColor.RED+"That player is not in a party.");
                    }
                }
                else{
                    p.sendMessage(ChatColor.RED+"Could not find player '"+cmdArgs.getArg(0)+"'.");
                }
            }
            else{
                p.sendMessage(ChatColor.RED+"One or more of your party members is not at spawn.");
            }
        }
        else{
            p.sendMessage(ChatColor.RED+"You are not in a party.");
        }


    }
}
