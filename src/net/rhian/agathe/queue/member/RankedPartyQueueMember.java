package net.rhian.agathe.queue.member;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import net.rhian.agathe.Agathe;
import net.rhian.agathe.ladder.Ladder;
import net.rhian.agathe.party.Party;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.queue.range.EloRange;

/**
 * Created by 360 on 5/13/2015.
 */

/**
 * The PartyQueueMember class
 * Used for representing a Party in the queue
 * Basically combines a party with range and ladder into one class.
 */
@AllArgsConstructor
public class RankedPartyQueueMember implements QueueMember, PartyQueueMember {

    @Getter @NonNull Party party;
    @Getter @NonNull EloRange range;
    @Getter @NonNull Ladder ladder;

    public double getAverageElo(){
        double averageScope = 0.0;
        for(Player p : party.getAllPlayers()){
            IPlayer ip = Agathe.getCache().getIPlayer(p);
            averageScope += ip.getElo(ladder);
        }
        averageScope = Math.round((double)averageScope / (double)party.getAllMembers().toArray().length);
        return (double)averageScope;
    }

    @Override
    public Set<IPlayer> getPlayers() {
        Set<IPlayer> players = new HashSet<>();
        for(Player pl : party.getAllPlayers()){
            players.add(Agathe.getCache().getIPlayer(pl));
        }
        return players;
    }

    @Override
    public String getName() {
        return party.getLeader()+"'s Party";
    }
}
