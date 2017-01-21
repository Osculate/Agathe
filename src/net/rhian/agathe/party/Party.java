package net.rhian.agathe.party;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.fancymessages.fanciful.FancyMessage;
import net.rhian.agathe.Agathe;
import net.rhian.agathe.ladder.Ladder;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.player.PlayerState;

/**
 * Created by 360 on 5/13/2015.
 */
@AllArgsConstructor
@RequiredArgsConstructor
public class Party {

    @NonNull
    @Getter
    @Setter
    private String leader;
    @Getter
    @Setter
    private List<String> members = new ArrayList<>();
    @Getter
    private List<String> invites = new ArrayList<>();
    @Getter
    private List<PartyDuel> duels = new ArrayList<>();

    public List<String> getAllMembers(){
        List<String> members = new ArrayList<>();
        members.addAll(getMembers());
        members.add(leader);
        return members;
    }

    public List<Player> getAllPlayers(){
        List<Player> p = new ArrayList<>();
        for(String s : getAllMembers()){
            Player t = Bukkit.getPlayerExact(s);
            if(t != null){
                p.add(t);
            }
        }
        return p;
    }

    public void msg(String msg){
        for(Player p : getAllPlayers()){
            p.sendMessage(msg);
        }
    }

    public void msg(FancyMessage msg){
        for(Player p : getAllPlayers()){
            msg.send(p);
        }
    }

    public boolean canDuel(){
        for(Player p : getAllPlayers()){
            IPlayer ip = Agathe.getCache().getIPlayer(p);
            if(ip.getState() != PlayerState.AT_SPAWN){
                return false;
            }
        }
        return true;
    }

    public String getMembersToString(){
        String s = "";

        for(Player p : getAllPlayers()){
            s += p.getName()+", ";
        }

        if(s.length() > 2){
            s = s.substring(0,s.length() - 2);
        }
        return s;
    }

    public boolean hasDuel(Party party){
        for(PartyDuel pd : duels) {
            if(pd.getSender().getLeader().equals(party.getLeader())){
                return true;
            }
        }
        return false;
    }

    public PartyDuel getDuel(Party party){
        for(PartyDuel pd : duels) {
            if(pd.getSender().getLeader().equals(party.getLeader())){
                return pd;
            }
        }
        return null;
    }

    public int getAverageElo(Ladder ladder){
        int averageScope = 0;
        for(Player p : getAllPlayers()){
            IPlayer ip = Agathe.getCache().getIPlayer(p);
            averageScope += ip.getElo(ladder);
        }
        averageScope = Math.round(averageScope / getAllMembers().toArray().length);
        return averageScope;
    }

}
