package net.rhian.agathe.match.participant;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;

import lombok.Getter;
import net.rhian.agathe.Agathe;
import net.rhian.agathe.ladder.Ladder;
import net.rhian.agathe.match.team.PracticeTeam;
import net.rhian.agathe.party.Party;
import net.rhian.agathe.player.IPlayer;

/**
 * Created by 360 on 9/7/2015.
 */
@Getter
public class MatchParticipant {

    private final Set<MatchPlayer> players = new HashSet<>();
    private MatchPlayer singlePlayer = null;
    private boolean single = false;
    private final PracticeTeam team;

    public MatchParticipant(IPlayer iPlayer, PracticeTeam team) {
        MatchPlayer pmp = new MatchPlayer(iPlayer);
        this.players.add(pmp);
        this.singlePlayer = pmp;
        this.team = team;
        this.single = true;
    }

    public MatchParticipant(Party party, PracticeTeam team) {
        this.team = team;
        for(Player pl : party.getAllPlayers()){
            IPlayer ip = Agathe.getCache().getIPlayer(pl);
            players.add(new MatchPlayer(ip));
        }
        this.single = false;
    }

    public int getAverageElo(Ladder ladder){
        int elo = 0;
        int x = 0;
        for(MatchPlayer pl : players){
            elo += pl.getPlayer().getElo(ladder);
            x++;
        }
        return Math.round(elo / x);
    }

}
