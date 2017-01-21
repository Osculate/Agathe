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
import net.rhian.agathe.queue.member.RankedQueueMember;
import net.rhian.agathe.queue.range.EloRange;

public class RankedQueue extends Queue {

    public RankedQueue() {
        super(QueueType.RANKED);
    }

    @Override
    public Match createMatch(QueueMatchSet set) {
        MatchBuilder builder = Agathe.getMatchManager().matchBuilder(set.getLadder());
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
