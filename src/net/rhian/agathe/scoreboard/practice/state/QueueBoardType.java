package net.rhian.agathe.scoreboard.practice.state;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;

import net.rhian.agathe.Agathe;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.player.PlayerState;
import net.rhian.agathe.queue.Queue;
import net.rhian.agathe.queue.QueueType;
import net.rhian.agathe.scoreboard.internal.XLabel;
import net.rhian.agathe.scoreboard.internal.XScoreboard;
import net.rhian.agathe.scoreboard.practice.label.ValueLabel;

public class QueueBoardType implements PracticeBoardType {

    private List<ValueLabel> valueLabels = new ArrayList<>();

    public QueueBoardType(XScoreboard scoreboard, IPlayer player) {

        valueLabels.add(new ValueLabel(scoreboard, player, 6, ChatColor.GOLD + "" + ChatColor.GRAY + "" +
                ChatColor.STRIKETHROUGH + "", new ValueLabel.CallableValue() {
            @Override
            public String call(IPlayer player) {
                return "-------------------" + ChatColor.GREEN + "" + ChatColor.YELLOW;
            }
        }));

        valueLabels.add(new ValueLabel(scoreboard, player, 5, ChatColor.BLUE+"Queue: ", new ValueLabel.CallableValue() {
            @Override
            public String call(IPlayer player) {
                if(Agathe.getQueueManager().inQueue(player)){
                    return ChatColor.GREEN+Agathe.getQueueManager().getQueue(player).getType().getName();
                }
                return "";
            }
        }));

        valueLabels.add(new ValueLabel(scoreboard, player, 4, ChatColor.BLUE+"Ladder: ", new ValueLabel.CallableValue() {
            @Override
            public String call(IPlayer player) {
                if(Agathe.getQueueManager().inQueue(player)){
                    return ChatColor.GREEN+Agathe.getQueueManager().getQueue(player).getMember(player).getLadder().getName();
                }
                return "";
            }
        }));

        valueLabels.add(new ValueLabel(scoreboard, player, 3, ChatColor.BLUE+"Range: ", new ValueLabel.CallableValue() {
            @Override
            public String call(IPlayer player) {
                if(Agathe.getQueueManager().inQueue(player)){
                    Queue queue = Agathe.getQueueManager().getQueue(player);
                    return ChatColor.GREEN+queue.getMember(player).getRange().rangeToString();
                }
                return "";
            }
        }));

        valueLabels.add(new ValueLabel(scoreboard, player, 2, ChatColor.BLUE+"", new ValueLabel.CallableValue() {
            @Override
            public String call(IPlayer player) {
                if(Agathe.getQueueManager().inQueue(player)){
                    Queue queue = Agathe.getQueueManager().getQueue(player);
                    if(queue.getType() == QueueType.RANKED){
                        return ChatColor.BLUE+"ELO: "+ChatColor.GREEN+""+player.getElo(queue.getMember(player).getLadder());
                    }
                    else if (queue.getType() == QueueType.RANKED_PARTY){
                        return ChatColor.BLUE+"Party ELO: "+ChatColor.GREEN+""+player.getParty().getAverageElo(queue.getMember(player).getLadder());
                    }
                    else if (queue.getType() == QueueType.PING){
                        return ChatColor.BLUE+"Ping: "+ChatColor.GREEN+((CraftPlayer)player.getPlayer()).getHandle().ping;
                    }
                }
                return ChatColor.GREEN+"Waiting...";
            }
        }));


        valueLabels.add(new ValueLabel(scoreboard, player, 1, ChatColor.GRAY + "" +
                ChatColor.STRIKETHROUGH, new ValueLabel.CallableValue() {
            @Override
            public String call(IPlayer player) {
                return "-------------------" + ChatColor.RED + "";
            }
        }));

        for (XLabel label : valueLabels) {
            scoreboard.addLabel(label);
        }
    }

    @Override
    public void update(XScoreboard scoreboard) {
        remove(scoreboard);
        for (ValueLabel label : valueLabels) {
            label.setVisible(true);
            label.update();
        }
    }

    @Override
    public void remove(XScoreboard scoreboard) {
        for (ValueLabel label : valueLabels) {
            scoreboard.removeLabel(label);
            label.setVisible(false);
        }
    }

    @Override
    public boolean isApplicable(IPlayer player) {
        return player.getState() == PlayerState.AT_SPAWN && Agathe.getQueueManager().inQueue(player);
    }
}
