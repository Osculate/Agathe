package com.shawckz.ipractice.queue.type;

import com.shawckz.ipractice.Practice;
import com.shawckz.ipractice.ladder.Ladder;
import com.shawckz.ipractice.match.*;
import com.shawckz.ipractice.match.team.PracticeTeam;
import com.shawckz.ipractice.match.team.Team;
import com.shawckz.ipractice.player.IPlayer;
import com.shawckz.ipractice.player.PlayerState;
import com.shawckz.ipractice.queue.Queue;
import com.shawckz.ipractice.queue.QueueMatchSet;
import com.shawckz.ipractice.queue.QueueType;
import com.shawckz.ipractice.queue.member.RankedQueueMember;
import com.shawckz.ipractice.queue.range.EloRange;
import org.bukkit.Material;

/**
 * Created by 360 on 9/12/2015.
 */
public class RankedQueue extends Queue {

    public RankedQueue() {
        super(QueueType.RANKED);
    }

    @Override
    public Match createMatch(QueueMatchSet set) {
        MatchBuilder builder = Practice.getMatchManager().matchBuilder(set.getLadder());
        builder.setRanked(true);
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
        RankedQueueMember queueMember = new RankedQueueMember(player, ladder, new EloRange(player.getElo(ladder)));
        getMembers().add(queueMember);
    }

    @Override
    public Material getIcon() {
        return Material.DIAMOND_HELMET;
    }

    @Override
    public boolean canJoin(IPlayer player) {
        return player.getState() == PlayerState.AT_SPAWN && player.getParty() == null;
    }

}
