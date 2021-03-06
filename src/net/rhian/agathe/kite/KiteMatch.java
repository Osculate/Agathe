package net.rhian.agathe.kite;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.rhian.agathe.Agathe;
import net.rhian.agathe.arena.Arena;
import net.rhian.agathe.arena.ArenaType;
import net.rhian.agathe.arena.KiteArena;
import net.rhian.agathe.exception.PracticeException;
import net.rhian.agathe.ladder.Ladder;
import net.rhian.agathe.match.MatchType;
import net.rhian.agathe.match.PracticeMatch;
import net.rhian.agathe.match.handle.MatchManager;
import net.rhian.agathe.player.IPlayer;
import net.rhian.agathe.player.PlayerState;

@RequiredArgsConstructor
@Getter
public class KiteMatch implements PracticeMatch{

    public static final String KITE_LADDER_RUNNER = "KiteRunner";
    public static final String KITE_LADDER_CHASER = "KiteChaser";

    private final String id = UUID.randomUUID().toString();
    private final IPlayer runner;
    private final IPlayer chaser;
    private boolean started = false;
    private boolean over = false;
    private int runnerCountdown = 5;
    private int chaserCountdown = 6;
    private KiteMatchHandler matchHandler;
    @Setter private KiteArena arena;

    @Override
    public void startMatch(MatchManager matchManager){
        if(started){
            throw new PracticeException("Attempted to start KiteMatch when already running");
        }

        if(arena == null){
            arena = (KiteArena) Agathe.getArenaManager().getNextArena(ArenaType.KITE);
        }
        else{
            if(arena.getType() != ArenaType.KITE){
                throw new PracticeException("KiteMatch: Arena is not Kite Arena");
            }
        }
        if(arena == null){
            msg(ChatColor.RED+"There are currently no available arenas.  Please wait while we attempt to auto-generate one for you.");
            try{
                final Arena newest = Agathe.getArenaManager().getNewestArena(ArenaType.KITE);
                if(newest != null) {
                    arena = (KiteArena) newest;
                }
                else{
                    msg(ChatColor.RED + "Unable to start match(1): There are no available arenas.  Please contact an administrator and notify them of this error.");
                    return;
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
                msg(ChatColor.RED + "Unable to start match(2): There are no available arenas.  Please contact an administrator and notify them of this error.");
                return;
            }
        }

        matchManager.registerMatch(this);

        runner.getPlayer().teleport(arena.getSpawnAlpha());
        chaser.getPlayer().teleport(arena.getSpawnBravo());

        runner.setState(PlayerState.IN_MATCH);
        chaser.setState(PlayerState.IN_MATCH);

        runner.getScoreboard().update();
        chaser.getScoreboard().update();

        runner.equipKit(Ladder.getLadder(KITE_LADDER_RUNNER));
        chaser.equipKit(Ladder.getLadder(KITE_LADDER_CHASER));

        runner.handlePlayerVisibility();
        chaser.handlePlayerVisibility();

        matchHandler = new KiteMatchHandler(this);
        matchHandler.register();

        runner.getPlayer().sendMessage(ChatColor.GOLD + "You are the " + ChatColor.GREEN + "runner" + ChatColor.GOLD + ".");
        runner.getPlayer().sendMessage(ChatColor.GOLD + "To Win: " + ChatColor.YELLOW + "Reach the beacon and don't let the chaser kill you.");

        chaser.getPlayer().sendMessage(ChatColor.GOLD + "You are the " + ChatColor.GREEN + "chaser" + ChatColor.GOLD + ".");
        chaser.getPlayer().sendMessage(ChatColor.GOLD + "To Win: " + ChatColor.YELLOW + "Kill the runner before they reach the beacon.");

        runner.getPlayer().sendMessage(ChatColor.GOLD+"Beacon location: "+ChatColor.GREEN+arena.getEnd().getBlockX()
                +", "+arena.getEnd().getBlockY()+", "+arena.getEnd().getBlockZ());
        chaser.getPlayer().sendMessage(ChatColor.GOLD+"Beacon location: "+ChatColor.GREEN+arena.getEnd().getBlockX()
                +", "+arena.getEnd().getBlockY()+", "+arena.getEnd().getBlockZ());

        runner.getPlayer().showPlayer(chaser.getPlayer());
        chaser.getPlayer().showPlayer(runner.getPlayer());
        runner.getPlayer().showPlayer(chaser.getPlayer());
        chaser.getPlayer().showPlayer(runner.getPlayer());

        new BukkitRunnable(){
            @Override
            public void run() {
                if(runnerCountdown > 0){
                    runner.getPlayer().sendMessage(ChatColor.GOLD + "Start Kiting in " +
                            ChatColor.LIGHT_PURPLE + runnerCountdown + ChatColor.GOLD + "...");
                    runnerCountdown--;
                }
                else if (runnerCountdown == 0){
                    runner.getPlayer().sendMessage(ChatColor.GREEN + "Go!");
                    runnerCountdown = -1;
                }

                if(chaserCountdown > 0){
                    chaser.getPlayer().sendMessage(ChatColor.GOLD + "You can start chasing in " +
                            ChatColor.LIGHT_PURPLE + chaserCountdown + ChatColor.GOLD + "...");
                    chaserCountdown--;
                }
                else if (chaserCountdown == 0){
                    chaser.getPlayer().sendMessage(ChatColor.GREEN + "Go!");
                    started = true;
                    cancel();
                }

            }
        }.runTaskTimer(Agathe.getPlugin(),20L,20L);
    }

    public void msg(String msg){
        runner.getPlayer().sendMessage(msg);
        chaser.getPlayer().sendMessage(msg);
    }

    @Override
    public void endMatch() {
        if(!started || over){
            throw new PracticeException("Attempted to end KiteMatch when already over or not started (started:"+started+",over:"+over+")");
        }
        matchHandler.unregister();
        started = false;
        over = true;

        if(runner != null){
            runner.sendToSpawn();
        }
        if(chaser != null){
            chaser.sendToSpawn();
        }
    }

    public void eliminatePlayer(Player player){
        if(runner.getName().equals(player.getName())){
            msg(ChatColor.LIGHT_PURPLE+chaser.getName()+ChatColor.GOLD+" has won!");
            endMatch();
        }
        else if (chaser.getName().equals(player.getName())){
            msg(ChatColor.LIGHT_PURPLE+runner.getName()+ChatColor.GOLD+" has won!");
            endMatch();
        }
        else{
            throw new PracticeException("Not in KiteMatch?: "+player.getName());
        }
    }

    public boolean contains(Player player){
        return chaser.getPlayer().getName().equals(player.getName()) ||
                runner.getPlayer().getName().equals(player.getName());
    }

    public KiteRole getRole(Player player){
        if(contains(player)){
            if(chaser.getPlayer().getName().equals(player.getName())){
                return KiteRole.CHASER;
            }
            else{
                return KiteRole.RUNNER;
            }
        }
        return null;
    }

    @Override
    public Set<Player> getPlayers() {
        Set<Player> players = new HashSet<>();
        players.add(runner.getPlayer());
        players.add(chaser.getPlayer());
        return players;
    }

    @Override
    public MatchType getType() {
        return MatchType.KITE;
    }

    @Override
    public Ladder getLadder() {
        return Ladder.getLadder(KITE_LADDER_RUNNER);
    }

    @Override
    public String getOpponent(IPlayer player) {
        if(player.getName().equals(runner.getName())){
            return chaser.getName();
        }
        else{
            return runner.getName();
        }
    }
}
