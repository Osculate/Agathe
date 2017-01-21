package net.rhian.agathe.match;

import java.util.Set;
import org.bukkit.entity.Player;

import net.rhian.agathe.ladder.Ladder;
import net.rhian.agathe.match.handle.MatchManager;
import net.rhian.agathe.player.IPlayer;

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
