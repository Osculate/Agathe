package net.rhian.agathe.scoreboard.practice.state;

import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.scoreboard.internal.XScoreboard;

public interface PracticeBoardType {

    void update(XScoreboard scoreboard);

    void remove(XScoreboard scoreboard);

    boolean isApplicable(IPlayer player);

}
