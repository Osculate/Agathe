package net.rhian.ipractice.scoreboard.practice.state;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;

import net.rhian.ipractice.player.IPlayer;
import net.rhian.ipractice.player.PlayerState;
import net.rhian.ipractice.scoreboard.internal.XLabel;
import net.rhian.ipractice.scoreboard.internal.XScoreboard;
import net.rhian.ipractice.scoreboard.practice.label.ValueLabel;

public class KitBuilderBoardType implements PracticeBoardType {

    private Set<ValueLabel> valueLabels = new HashSet<>();

    public KitBuilderBoardType(XScoreboard scoreboard, IPlayer player) {

        valueLabels.add(new ValueLabel(scoreboard, player, 3, ChatColor.GOLD+""+ ChatColor.GRAY + "" +
                ChatColor.STRIKETHROUGH +"", new ValueLabel.CallableValue() {
            @Override
            public String call(IPlayer player) {
                return "-------------------"+ChatColor.GREEN+""+ChatColor.YELLOW;
            }
        }));

        valueLabels.add(new ValueLabel(scoreboard, player, 2, ChatColor.BLUE + "Editing Kit: ", new ValueLabel.CallableValue() {
            @Override
            public String call(IPlayer player) {
                if(player.getKitBuilder() != null && player.getKitBuilder().isActive()){
                    return ChatColor.GREEN+player.getKitBuilder().getLadder().getName();
                }
                return ChatColor.AQUA+"None";
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
        remove(scoreboard);
        for(ValueLabel label : valueLabels){
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
        return player.getState() == PlayerState.BUILDING_KIT;
    }
}
