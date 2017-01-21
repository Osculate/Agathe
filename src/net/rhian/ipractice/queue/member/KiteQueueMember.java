package net.rhian.ipractice.queue.member;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.rhian.ipractice.kite.KiteRole;
import net.rhian.ipractice.ladder.Ladder;
import net.rhian.ipractice.player.IPlayer;
import net.rhian.ipractice.queue.range.QueueRange;
import net.rhian.ipractice.queue.range.UnrankedQueueRange;

/**
 * Created by 360 on 9/12/2015.
 */
@RequiredArgsConstructor
@Getter
public class KiteQueueMember implements QueueMember {

    private final IPlayer player;
    private final Ladder ladder;
    private final KiteRole role;
    private final UnrankedQueueRange range = new UnrankedQueueRange();

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

    @Override
    public QueueRange getRange() {
        return range;
    }
}
