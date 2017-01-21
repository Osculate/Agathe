package net.rhian.ipractice.scoreboard.practice.label;

import org.bukkit.ChatColor;

import net.rhian.ipractice.Practice;
import net.rhian.ipractice.match.PracticeMatch;
import net.rhian.ipractice.scoreboard.internal.XScoreboard;
import net.rhian.ipractice.scoreboard.internal.XScoreboardTimer;

public class MatchTimerLabel extends XScoreboardTimer {
    private final PracticeMatch match;
    private final String valueBase = ChatColor.translateAlternateColorCodes('&', Practice.getIConfig().getScoreboardMatchDuration());
    public MatchTimerLabel(XScoreboard scoreboard, int score, PracticeMatch match) {
        super(scoreboard, score, "", 0, 20);
        this.match = match;
    }

    @Override
    public void onUpdate() {
        if(match.isStarted()){
            setValue(valueBase+getTimeString());
        }
    }

    private String getTimeString(){
        int minutes = (int)(Math.round(getTime()) % 3600) / 60;
        int seconds = (int) Math.round(getTime()) % 60;
        String m = ""+minutes;
        if(minutes < 10){
            m = "0"+minutes;
        }
        String s = ""+seconds;
        if(seconds < 10){
            s = "0"+seconds;
        }
        return m+":"+s;
    }

    @Override
    public void complete() {
        getScoreboard().removeLabel(this);
    }

    @Override
    public void updateTime() {
        setTime(getTime() + 1);
    }

    @Override
    public boolean isComplete() {
        return match.isOver();
    }
}
