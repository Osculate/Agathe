package net.rhian.agathe.queue.member;

import java.util.HashSet;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import net.rhian.agathe.ladder.Ladder;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.queue.range.QueueRange;
import net.rhian.agathe.queue.range.UnrankedQueueRange;

/**
 * Created by 360 on 9/12/2015.
 */
@RequiredArgsConstructor
public class UnrankedQueueMember implements QueueMember {

    private final IPlayer player;
    private final Ladder ladder;
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
