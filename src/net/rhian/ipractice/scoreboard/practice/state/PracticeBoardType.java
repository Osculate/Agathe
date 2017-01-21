package net.rhian.ipractice.scoreboard.practice.state;

import net.rhian.ipractice.player.IPlayer;
import net.rhian.ipractice.scoreboard.internal.XScoreboard;

public interface PracticeBoardType {

    void update(XScoreboard scoreboard);

    void remove(XScoreboard scoreboard);

    boolean isApplicable(IPlayer player);

}
