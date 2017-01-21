package net.rhian.agathe.scoreboard.practice;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import net.rhian.agathe.Agathe;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.scoreboard.internal.XScoreboard;
import net.rhian.agathe.scoreboard.practice.state.KitBuilderBoardType;
import net.rhian.agathe.scoreboard.practice.state.MatchBoardType;
import net.rhian.agathe.scoreboard.practice.state.PartyBoardType;
import net.rhian.agathe.scoreboard.practice.state.PracticeBoardType;
import net.rhian.agathe.scoreboard.practice.state.QueueBoardType;
import net.rhian.agathe.scoreboard.practice.state.SpawnBoardType;
import net.rhian.agathe.scoreboard.practice.state.StaffModeBoardType;

public class PracticeScoreboard {

    private XScoreboard scoreboard;
    private IPlayer ip;

    private Set<PracticeBoardType> boards = new HashSet<>();

    public Set<PracticeBoardType> getBoards() {
        return boards;
    }

    public IPlayer getIp() {
        return ip;
    }

    public XScoreboard getScoreboard() {
        return scoreboard;
    }

    public PracticeScoreboard(IPlayer ip) {
        this.ip = ip;
        this.scoreboard = new XScoreboard(ChatColor.translateAlternateColorCodes('&', Agathe.getIConfig().getScoreboardTitle()));

        boards.add(new SpawnBoardType(scoreboard, ip));
        boards.add(new KitBuilderBoardType(scoreboard, ip));
        boards.add(new QueueBoardType(scoreboard, ip));
        boards.add(new PartyBoardType(scoreboard, ip));
        boards.add(new MatchBoardType(scoreboard, ip));
        boards.add(new StaffModeBoardType(scoreboard, ip));

        scoreboard.send(ip.getPlayer());
        scoreboard.update();
        update();
    }

    private String getTitle(String title){
        int split = Math.round(title.length() / 2);
        final String key = title.substring(0, split);
        final String value = title.substring(split, title.length());
        Score score = scoreboard.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(key);
        if (scoreboard.getScoreboard().getEntries().toArray().length != 0) {
            Team team = this.scoreboard.getScoreboard().getTeam(key);
            if (team == null) {
                team = this.scoreboard.getScoreboard().registerNewTeam(key);
                team.addPlayer(Bukkit.getOfflinePlayer(key));
            }
            team.setSuffix(value);
        }
        return score.getEntry();
    }

    public void update(){
        for (PracticeBoardType board : boards) {
            if (!board.isApplicable(ip)) {
                board.remove(scoreboard);
            }
        }
        for (PracticeBoardType board : boards) {
            if (board.isApplicable(ip)) {
                board.update(scoreboard);
            }
        }
    }

}
