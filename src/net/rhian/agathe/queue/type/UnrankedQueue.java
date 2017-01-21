package net.rhian.agathe.queue.type;

import org.bukkit.Material;

import net.rhian.agathe.Agathe;
import net.rhian.agathe.ladder.Ladder;
import net.rhian.agathe.match.Match;
import net.rhian.agathe.match.MatchBuilder;
import net.rhian.agathe.match.team.PracticeTeam;
import net.rhian.agathe.match.team.Team;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.player.PlayerState;
import net.rhian.agathe.queue.Queue;
import net.rhian.agathe.queue.QueueMatchSet;
import net.rhian.agathe.queue.QueueType;
import net.rhian.agathe.queue.member.UnrankedQueueMember;

/**
 * Created by 360 on 9/12/2015.
 */
public class UnrankedQueue extends Queue {

    public UnrankedQueue() {
        super(QueueType.UNRANKED);
    }

    @Override
    public Match createMatch(QueueMatchSet set) {
        MatchBuilder builder = Agathe.getMatchManager().matchBuilder(set.getLadder());
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
        UnrankedQueueMember queueMember = new UnrankedQueueMember(player, ladder);
        getMembers().add(queueMember);
    }

    @Override
    public Material getIcon() {
        return Material.IRON_HELMET;
    }

    @Override
    public boolean canJoin(IPlayer player) {
        return player.getState() == PlayerState.AT_SPAWN && player.getParty() == null;
    }

}
