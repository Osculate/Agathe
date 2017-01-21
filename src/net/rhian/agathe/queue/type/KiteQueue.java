package net.rhian.agathe.queue.type;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Material;

import net.rhian.agathe.kite.KiteMatch;
import net.rhian.agathe.kite.KiteRole;
import net.rhian.agathe.ladder.Ladder;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.queue.Queue;
import net.rhian.agathe.queue.QueueMatchSet;
import net.rhian.agathe.queue.QueueType;
import net.rhian.agathe.queue.member.KiteQueueMember;
import net.rhian.agathe.queue.member.QueueMember;

/**
 * Created by 360 on 9/12/2015.
 */
public class KiteQueue extends Queue {

    public KiteQueue() {
        super(QueueType.KITE);
    }

    @Override
    public Set<QueueMatchSet> findMatches(Ladder ladder) {
        Iterator<QueueMember> it = getMembers().iterator();
        Set<QueueMatchSet> results = new HashSet<>();
        while(it.hasNext()){
            QueueMember search = it.next();
            KiteQueueMember alpha = (KiteQueueMember) search;
            if (it.hasNext()) {
                QueueMember found = it.next();
                KiteQueueMember bravo = (KiteQueueMember) found;
                if(alpha.getRole() == KiteRole.CHASER && bravo.getRole() == KiteRole.RUNNER){
                    QueueMatchSet set = new QueueMatchSet(ladder, bravo, alpha);
                    results.add(set);
                }
                else if (alpha.getRole() == KiteRole.RUNNER && bravo.getRole() == KiteRole.CHASER){
                    QueueMatchSet set = new QueueMatchSet(ladder, alpha, bravo);
                    results.add(set);
                }
            }
        }
        return results;
    }

    @Override
    public KiteMatch createMatch(QueueMatchSet set) {
        KiteQueueMember alpha = (KiteQueueMember) set.getAlpha();
        KiteQueueMember bravo = (KiteQueueMember) set.getBravo();
        KiteMatch kiteMatch = new KiteMatch(alpha.getPlayer(), bravo.getPlayer());
        return kiteMatch;
    }

    @Override
    public void addToQueue(IPlayer runner, Ladder ladder) {
        addToQueue(runner, KiteRole.RUNNER);
    }

    public void addToQueue(IPlayer player, KiteRole role) {
        if(role == KiteRole.CHASER){
            KiteQueueMember queueMember = new KiteQueueMember(player, Ladder.getLadder(KiteMatch.KITE_LADDER_CHASER), role);
            getMembers().add(queueMember);
        }
        else{
            KiteQueueMember queueMember = new KiteQueueMember(player, Ladder.getLadder(KiteMatch.KITE_LADDER_RUNNER), role);
            getMembers().add(queueMember);
        }
    }

    @Override
    public Material getIcon() {
        return Material.ENDER_PEARL;
    }

    @Override
    public boolean canJoin(IPlayer player) {
        return false;//so it doesn't show up in queueselect
    }

}
