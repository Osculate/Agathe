package net.rhian.agathe.scoreboard.practice.state;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;

import net.rhian.agathe.Agathe;
import net.rhian.agathe.match.MatchType;
import net.rhian.agathe.match.PracticeMatch;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.player.PlayerState;
import net.rhian.agathe.scoreboard.internal.XLabel;
import net.rhian.agathe.scoreboard.internal.XScoreboard;
import net.rhian.agathe.scoreboard.internal.label.BasicLabel;
import net.rhian.agathe.scoreboard.practice.label.EnderpearlCooldownLabel;
import net.rhian.agathe.scoreboard.practice.label.MatchTimerLabel;

public class MatchBoardType implements PracticeBoardType {

    private Set<XLabel> labels = new HashSet<>();

    private MatchTimerLabel matchTimerLabel = null;
    private EnderpearlCooldownLabel enderpearlCooldownLabel = null;
    private final IPlayer player;

    public MatchBoardType(XScoreboard scoreboard, IPlayer player) {
        this.player = player;
    }

    @Override
    public void update(XScoreboard scoreboard) {
        remove(scoreboard);
        final PracticeMatch practiceMatch = Agathe.getMatchManager().getMatch(player);

        if(practiceMatch != null) {

            if (practiceMatch.getType() == MatchType.NORMAL || practiceMatch.getType() == MatchType.KITE) {

                if (this.matchTimerLabel == null) {
                    this.matchTimerLabel = new MatchTimerLabel(scoreboard, 3, practiceMatch);
                    labels.add(matchTimerLabel);
                }
                if (this.enderpearlCooldownLabel == null) {
                    this.enderpearlCooldownLabel = new EnderpearlCooldownLabel(scoreboard, 2, player);
                    labels.add(enderpearlCooldownLabel);
                }

           //     if((this.enderpearlCooldownLabel.isVisible() && this.enderpearlCooldownLabel.isRunning())
          //              || (this.matchTimerLabel.isVisible() && this.matchTimerLabel.isRunning() && !this.matchTimerLabel.isComplete())) {
                    labels.add(new BasicLabel(scoreboard, 4, ChatColor.GRAY + "" +
                            ChatColor.STRIKETHROUGH + "-------------------" + ChatColor.GREEN + "" + ChatColor.YELLOW));

                    labels.add(new BasicLabel(scoreboard, 1, ChatColor.GRAY + "" +
                            ChatColor.STRIKETHROUGH + "-------------------" + ChatColor.RED));
           //     }

                for (XLabel label : labels) {
                    label.setVisible(true);
                    label.update();
                }

                if (practiceMatch.isStarted() && !practiceMatch.isOver()) {
                    if (!matchTimerLabel.isRunning()) {
                        matchTimerLabel.start();
                    }
                }
                if (!this.enderpearlCooldownLabel.isRunning()) {
                    this.enderpearlCooldownLabel.start();
                }
            }
        }
    }

    @Override
    public void remove(XScoreboard scoreboard) {
        for(XLabel label : labels){
            scoreboard.removeLabel(label);
        }
        PracticeMatch practiceMatch = Agathe.getMatchManager().getMatch(player);
        //labels.clear(); Going to remove this so that they will be removed (hopefully) properly
        if(practiceMatch != null && (practiceMatch.getType() == MatchType.NORMAL || practiceMatch.getType() == MatchType.KITE)) {
            if (this.matchTimerLabel != null) {
                if (this.matchTimerLabel.isRunning()) {
                    this.matchTimerLabel.stop();
                }
                matchTimerLabel.complete();
            }
            if (this.enderpearlCooldownLabel != null) {
                if (this.enderpearlCooldownLabel.isRunning()) {
                    this.enderpearlCooldownLabel.stop();
                }
                enderpearlCooldownLabel.complete();
            }
            labels.clear();
            this.matchTimerLabel = null;
            this.enderpearlCooldownLabel = null;
        }
    }

    @Override
    public boolean isApplicable(IPlayer player) {
        return player.getState() == PlayerState.IN_MATCH && Agathe.getMatchManager().inMatch(player)
                && Agathe.getMatchManager().getMatch(player).isStarted();
    }
}
