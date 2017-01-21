package net.rhian.agathe.queue.member;

import java.util.Set;

import net.rhian.agathe.ladder.Ladder;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.queue.range.QueueRange;

/**
 * Created by 360 on 09/05/2015.
 */

/**
 * The QueueMember class, just a simple class to combine a CPlayer with a KDRange
 * in order to make the queue system a lot easier.
 */
public interface QueueMember {

    Set<IPlayer> getPlayers();

    Ladder getLadder();

    String getName();

    QueueRange getRange();

}
