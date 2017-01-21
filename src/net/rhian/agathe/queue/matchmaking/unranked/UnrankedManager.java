package net.rhian.agathe.queue.matchmaking.unranked;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import net.rhian.agathe.Agathe;
import net.rhian.agathe.exception.PracticeException;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.queue.Queue;
import net.rhian.agathe.queue.QueueType;
import net.rhian.agathe.queue.member.PartyQueueMember;
import net.rhian.agathe.queue.member.QueueMember;
import net.rhian.agathe.queue.type.KiteQueue;
import net.rhian.agathe.queue.type.RankedPartyQueue;
import net.rhian.agathe.queue.type.RankedQueue;
import net.rhian.agathe.queue.type.UnrankedPartyQueue;
import net.rhian.agathe.queue.type.UnrankedPartySizeQueue;
import net.rhian.agathe.queue.type.UnrankedPingQueue;
import net.rhian.agathe.queue.type.UnrankedQueue;

public class UnrankedManager {
    private boolean running = false;
    private final Map<QueueType,Queue> queues = new HashMap<>();
    public UnrankedManager(Agathe instance) {
        registerQueue(QueueType.UNRANKED, new UnrankedQueue());
        registerQueue(QueueType.UNRANKED_PARTY, new UnrankedPartyQueue());
        registerQueue(QueueType.PING, new UnrankedPingQueue());
        registerQueue(QueueType.UNRANKED_PARTY_SIZE, new UnrankedPartySizeQueue());
        registerQueue(QueueType.RANKED, new RankedQueue());
        registerQueue(QueueType.RANKED_PARTY, new RankedPartyQueue());
    }
    public void run(){
        if(running){
            throw new PracticeException("The QueueManager is already running");
        }
        else {
            running = true;
            new BukkitRunnable() {
                @Override
                public void run() {
                    for(Queue queue : queues.values()){
                        queue.run();
                    }
                }
            }.runTaskTimerAsynchronously(Agathe.getPlugin(), 100L, 100L);
        }
    }
    public void registerQueue(QueueType type, Queue queue){
        queues.put(type, queue);
    }
    public Queue getQueue(QueueType type){
        return queues.get(type);
    }
    public boolean inQueue(IPlayer player){
        for(Queue queue : queues.values()){
            if(queue.inQueue(player)){
                return true;
            }
        }
        return false;
    }
    public Queue getQueue(IPlayer player){
        for(Queue queue : queues.values()){
            if(queue.inQueue(player)){
                return queue;
            }
        }
        return null;
    }
    public void removeFromQueue(IPlayer player){
        for(Queue queue : queues.values()){
            if(queue.inQueue(player)){
                QueueMember member = queue.getMember(player);
                if(member instanceof PartyQueueMember){
                    PartyQueueMember pq = (PartyQueueMember) member;
                    if(pq.getParty().getLeader().equals(player.getName())){
                        queue.removeFromQueue(member);
                        for(IPlayer pl : member.getPlayers()){
                            pl.getPlayer().sendMessage(ChatColor.RED+"Your party was disbanded, so you were removed from the queue.");
                            pl.getScoreboard().update();
                        }
                        //If they are the leader, remove the whole party from the queue
                    } else {
                        queue.removeFromQueue(member);
                        for(IPlayer pl : member.getPlayers()){
                            pl.getPlayer().sendMessage(ChatColor.RED+"A player left your party, so you were removed from the queue.");
                            pl.getScoreboard().update();
                        }
                        //If they aren't the leader, remove the whole party from the queue, and
                    }
                } else {
                    //They are in the queue alone, not in a party
                    queue.removeFromQueue(member);
                }
            }
        }
    }
    public boolean isRunning() {
        return running;
    }
    public Map<QueueType, Queue> getQueues() {
        return queues;
    }
}
