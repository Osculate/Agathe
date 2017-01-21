package net.rhian.ipractice.match;

import java.util.Set;

import org.bukkit.entity.Player;

import net.rhian.ipractice.ladder.Ladder;
import net.rhian.ipractice.match.handle.MatchManager;
import net.rhian.ipractice.player.IPlayer;

public interface PracticeMatch {

    String getId();

    void startMatch(MatchManager matchManager);

    void endMatch();

    boolean isStarted();

    boolean isOver();

    Set<Player> getPlayers();

    MatchType getType();

    Ladder getLadder();

    String getOpponent(IPlayer player);

}
