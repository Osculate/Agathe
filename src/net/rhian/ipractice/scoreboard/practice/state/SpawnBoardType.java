package net.rhian.ipractice.scoreboard.practice.state;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;

import net.rhian.ipractice.Practice;
import net.rhian.ipractice.player.IPlayer;
import net.rhian.ipractice.player.PlayerState;
import net.rhian.ipractice.scoreboard.internal.XLabel;
import net.rhian.ipractice.scoreboard.internal.XScoreboard;
import net.rhian.ipractice.scoreboard.practice.label.ValueLabel;

public class SpawnBoardType implements PracticeBoardType {

    private Set<ValueLabel> valueLabels = new HashSet<>();

    public SpawnBoardType(XScoreboard scoreboard, IPlayer player) {

        valueLabels.add(new ValueLabel(scoreboard, player, 6, ChatColor.GOLD+""+ ChatColor.GRAY + "" +
                ChatColor.STRIKETHROUGH +"", new ValueLabel.CallableValue() {
            @Override
            public String call(IPlayer player) {
                return "-------------------"+ChatColor.GREEN+""+ChatColor.YELLOW;
            }
        }));

        valueLabels.add(new ValueLabel(scoreboard, player, 5, ChatColor.BLUE + "Average ELO: ", new ValueLabel.CallableValue() {
            @Override
            public String call(IPlayer player) {
                return ChatColor.GREEN+""+player.getAverageElo();
            }
        }));

        valueLabels.add(new ValueLabel(scoreboard, player, 4, ChatColor.BLUE + "Ranked Matches: ", new ValueLabel.CallableValue() {
            @Override
            public String call(IPlayer player) {
                return ChatColor.GREEN+""+player.getTotalMatchesAllLadders();
            }
        }));

        valueLabels.add(new ValueLabel(scoreboard, player, 3, ChatColor.BLUE + "Ranked Kills: ", new ValueLabel.CallableValue() {
            @Override
            public String call(IPlayer player) {
                return ChatColor.GREEN + "" + player.getKillsAllLadders();
            }
        }));

        valueLabels.add(new ValueLabel(scoreboard, player, 2, ChatColor.BLUE + "Ranked Deaths: ", new ValueLabel.CallableValue() {
            @Override
            public String call(IPlayer player) {
                return ChatColor.GREEN + "" + player.getDeathsAllLadders();
            }
        }));

        valueLabels.add(new ValueLabel(scoreboard, player, 1, ChatColor.GRAY + "" +
                ChatColor.STRIKETHROUGH, new ValueLabel.CallableValue() {
            @Override
            public String call(IPlayer player) {
                return "-------------------"+ChatColor.RED+"";
            }
        }));

        for(XLabel label : valueLabels){
            scoreboard.addLabel(label);
        }
    }

    @Override
    public void update(XScoreboard scoreboard) {
        for(ValueLabel label : valueLabels){
            if(!scoreboard.hasLabel(label)){
                scoreboard.addLabel(label);
            }
            label.setVisible(true);
            label.update();
        }
    }

    @Override
    public void remove(XScoreboard scoreboard) {
        for(ValueLabel label : valueLabels){
            scoreboard.removeLabel(label);
        }
    }

    @Override
    public boolean isApplicable(IPlayer player) {
        return player.getState() == PlayerState.AT_SPAWN && !Practice.getQueueManager().inQueue(player) &&
                player.getParty() == null && !player.isStaffMode();
    }
}
