package net.rhian.agathe.queue.member;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.rhian.agathe.Agathe;
import net.rhian.agathe.ladder.Ladder;
import net.rhian.agathe.party.Party;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.queue.range.UnrankedQueueRange;

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
            players.add(Agathe.getCache().getIPlayer(pl));
        }
        return players;
    }

    @Override
    public String getName() {
        return party.getLeader()+"'s Party";
    }
}
