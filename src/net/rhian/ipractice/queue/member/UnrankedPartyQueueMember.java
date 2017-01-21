package net.rhian.ipractice.queue.member;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.rhian.ipractice.Practice;
import net.rhian.ipractice.ladder.Ladder;
import net.rhian.ipractice.party.Party;
import net.rhian.ipractice.player.IPlayer;
import net.rhian.ipractice.queue.range.UnrankedQueueRange;

/**
 * Created by 360 on 9/12/2015.
 */
@RequiredArgsConstructor
public class UnrankedPartyQueueMember implements QueueMember, PartyQueueMember {

    @NonNull @Getter private final Party party;
    @NonNull @Getter private final Ladder ladder;
    @Getter private final UnrankedQueueRange range;

    @Override
    public Set<IPlayer> getPlayers() {
        Set<IPlayer> players = new HashSet<>();
        for(Player pl : party.getAllPlayers()){
            players.add(Practice.getCache().getIPlayer(pl));
        }
        return players;
    }

    @Override
    public String getName() {
        return party.getLeader()+"'s Party";
    }
}
