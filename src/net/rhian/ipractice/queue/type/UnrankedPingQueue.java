package net.rhian.ipractice.queue.type;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;

import net.rhian.ipractice.Practice;
import net.rhian.ipractice.ladder.Ladder;
import net.rhian.ipractice.match.Match;
import net.rhian.ipractice.match.MatchBuilder;
import net.rhian.ipractice.match.team.PracticeTeam;
import net.rhian.ipractice.match.team.Team;
import net.rhian.ipractice.player.IPlayer;
import net.rhian.ipractice.player.PlayerState;
import net.rhian.ipractice.queue.Queue;
import net.rhian.ipractice.queue.QueueMatchSet;
import net.rhian.ipractice.queue.QueueType;
import net.rhian.ipractice.queue.member.RankedQueueMember;
import net.rhian.ipractice.queue.range.PingRange;

/**
 * Created by 360 on 9/12/2015.
 */
public class UnrankedPingQueue extends Queue implements PingQueue{

    public UnrankedPingQueue() {
        super(QueueType.PING);
    }

    @Override
    public Match createMatch(QueueMatchSet set) {
        MatchBuilder builder = Practice.getMatchManager().matchBuilder(set.getLadder());
        builder.setRanked(false);
        builder.registerTeam(new PracticeTeam(set.getAlpha().getName(), Team.ALPHA));
        builder.registerTeam(new PracticeTeam(set.getBravo().getName(), Team.BRAVO));
        for(IPlayer player : set.getAlpha().getPlayers()){
            builder.withPlayer(player.getPlayer(), set.getAlpha().getName());
        }
        for(IPlayer player : set.getBravo().getPlayers()){
            builder.withPlayer(player.getPlayer(), set.getBravo().getName());
        }
        return builder.build();
    }

    @Override
    public void addToQueue(IPlayer player, Ladder ladder) {
        int ping = ((CraftPlayer)player.getPlayer()).getHandle().ping / 2;
        System.out.println("Ping: "+ping);
        RankedQueueMember queueMember = new RankedQueueMember(player, ladder, new PingRange(ping));
        getMembers().add(queueMember);
    }

    @Override
    public Material getIcon() {
        return Material.LEATHER_CHESTPLATE;
    }

    @Override
    public boolean canJoin(IPlayer player) {
        return player.getState() == PlayerState.AT_SPAWN && player.getParty() == null;
    }

}
