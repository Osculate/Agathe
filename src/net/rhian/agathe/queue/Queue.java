package net.rhian.agathe.queue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import lombok.Getter;
import net.rhian.agathe.Agathe;
import net.rhian.agathe.ladder.Ladder;
import net.rhian.agathe.match.PracticeMatch;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.queue.member.QueueMember;

public abstract class Queue {
    private final QueueType type;
    public Queue(QueueType type) {
        this.type = type;
    }

    public QueueType getType() {
        return type;
    }

    @Getter private Set<QueueMember> members = new HashSet<>();
    public void run(){
        for(Ladder ladder : Ladder.getLadders()){
            Set<QueueMatchSet> found = findMatches(ladder);
            for(final QueueMatchSet set : found){
                for(QueueMember mem : set.getAll()){
                    if(members.contains(mem)){
                        members.remove(mem);
                    }
                }
                Bukkit.getScheduler().scheduleSyncDelayedTask(Agathe.getPlugin(), new Runnable() {
                    @Override
                    public void run() {
                        createMatch(set).startMatch(Agathe.getMatchManager());
                    }
                });
            }
        }
    }

    @SuppressWarnings("unused")
	public Set<QueueMatchSet> findMatches(Ladder ladder) {
        Iterator<QueueMember> it = getMembers().iterator();
        Set<QueueMatchSet> results = new HashSet<>();
        main:
        while(it.hasNext()){
            QueueMember search = it.next();
            if(search.getLadder().getName().equals(ladder.getName())) {
                if (it.hasNext()) {
                    QueueMember found = it.next();
                    if (found.getLadder().getName().equals(search.getLadder().getName())) {
                        QueueMatchSet set = new QueueMatchSet(ladder, search, found);
                        if (inRange(set)) {
                            results.add(set);
                        } else {
                            incrementRange(search);
                            incrementRange(found);
                        }
                    }
                } else {
                    if (search.getLadder().getName().equals(ladder.getName())) {
                        incrementRange(search);
                    }
                }
            }
        }
        return results;
    }

    public void incrementRange(QueueMember member){
        member.getRange().incrementRange();
        for(IPlayer ip : member.getPlayers()){
            ip.getPlayer().sendMessage(ChatColor.BLUE+"Searching in range "+ChatColor.GOLD+member.getRange().rangeToString());
            ip.getScoreboard().update();
        }
    }

    public boolean inQueue(IPlayer player){
        for(QueueMember member : members){
            if(member.getPlayers().contains(player)){
                return true;
            }
        }
        return false;
    }

    public abstract void addToQueue(IPlayer player, Ladder ladder);
    public void removeFromQueue(QueueMember member) {
        if(getMembers().contains(member)){
            getMembers().remove(member);
        }
    }

    public abstract PracticeMatch createMatch(QueueMatchSet set);
    public abstract Material getIcon();
    public abstract boolean canJoin(IPlayer player);
    public QueueMember getMember(IPlayer player) {
        for(QueueMember member : getMembers()){
            if(member.getPlayers().contains(player)){
                return member;
            }
        }
        return null;
    }

    public boolean inRange(QueueMatchSet set){
        return set.getAlpha().getRange().inRange(set.getBravo().getRange());
    }
}
