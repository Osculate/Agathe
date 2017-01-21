package net.rhian.ipractice.queue.member;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.rhian.ipractice.ladder.Ladder;
import net.rhian.ipractice.player.IPlayer;
import net.rhian.ipractice.queue.range.QueueRange;

/**
 * Created by 360 on 9/12/2015.
 */
@RequiredArgsConstructor
public class RankedQueueMember implements QueueMember {

    @Getter private final IPlayer player;
    private final Ladder ladder;
    @Getter private final QueueRange range;

    @Override
    public Set<IPlayer> getPlayers() {
        Set<IPlayer> players = new HashSet<>();
        players.add(player);
        return players;
    }

    @Override
    public Ladder getLadder() {
        return ladder;
    }

    @Override
    public String getName() {
        return player.getName();
    }
}
