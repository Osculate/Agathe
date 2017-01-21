package net.rhian.ipractice.match.participant;

import lombok.Data;
import net.rhian.ipractice.player.IPlayer;

/**
 * Created by 360 on 9/7/2015.
 */
@Data
public class MatchPlayer {

    private final IPlayer player;
    private boolean alive = true;
    private boolean spectating = false;

}
