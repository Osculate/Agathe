package net.rhian.ipractice.scoreboard.practice.label;

import org.bukkit.ChatColor;

import net.rhian.ipractice.Practice;
import net.rhian.ipractice.match.PracticeMatch;
import net.rhian.ipractice.player.IPlayer;
import net.rhian.ipractice.scoreboard.internal.XScoreboard;
import net.rhian.ipractice.scoreboard.internal.XScoreboardTimer;

public class EnderpearlCooldownLabel extends XScoreboardTimer {
    private final IPlayer player;
    public EnderpearlCooldownLabel(XScoreboard scoreboard, int score, IPlayer player) {
        super(scoreboard, score, "", 0, 10);
        this.player = player;
    }

    @Override
    public void onUpdate() {
        if(player.getEnderpearl() > System.currentTimeMillis()){
            setVisible(true);
            setValue(ChatColor.translateAlternateColorCodes('&', Practice.getIConfig().getScoreboardMatchDuration().replaceAll("{COOLDOWN}", String.valueOf((player.getEnderpearl() - System.currentTimeMillis())/1000 + "s"))));
        } else {
            setVisible(false);
            getScoreboard().removeLabel(this);
        }
    }

    @Override
    public void complete() {
        getScoreboard().removeLabel(this);
    }

    @Override
    public void updateTime() {

    }

    @Override
    public boolean isComplete() {
        PracticeMatch match = Practice.getMatchManager().getMatch(player);
        return match == null || match.isOver();
    }
}
