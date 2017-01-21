package net.rhian.ipractice.queue.member;

import java.util.Set;

import net.rhian.ipractice.ladder.Ladder;
import net.rhian.ipractice.player.IPlayer;
import net.rhian.ipractice.queue.range.QueueRange;

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
