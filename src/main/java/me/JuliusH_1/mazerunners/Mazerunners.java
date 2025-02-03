package me.JuliusH_1.mazerunners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Mazerunners extends JavaPlugin {

    private final Map<Integer, String> ongoingEvents = new HashMap<>();
    private final Map<Integer, Set<String>> eventPlayers = new HashMap<>();
    private int nextEventNumber = 1;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("mazerunner").setExecutor(new MazerunnerCommandExecutor(this));
        getCommand("event").setExecutor(new MazerunnerCommandExecutor(this));
        getLogger().info("Mazerunners plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Mazerunners plugin disabled!");
    }

    public void startEvent(Set<Player> players, int teamNumbers, int playersPerTeam, String gameMode, int durationInSeconds) {
        String eventName = "Event_" + nextEventNumber;
        ongoingEvents.put(nextEventNumber, eventName);
        Set<String> playerNames = new HashSet<>();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        for (int i = 1; i <= teamNumbers; i++) {
            Team team = scoreboard.registerNewTeam("team" + i);
            team.setDisplayName("Team " + i);
        }

        int teamIndex = 1;
        for (Player player : players) {
            playerNames.add(player.getName());
            Team team = scoreboard.getTeam("team" + teamIndex);
            if (team != null) {
                team.addEntry(player.getName());
            }
            if (team.getEntries().size() >= playersPerTeam) {
                teamIndex++;
            }
        }

        eventPlayers.put(nextEventNumber, playerNames);
        nextEventNumber++;
        getServer().broadcastMessage("The Mazerunner event '" + eventName + "' has started with " + teamNumbers + " teams, " + playersPerTeam + " players per team, in " + gameMode + " mode!");

        new BukkitRunnable() {
            @Override
            public void run() {
                endEvent(nextEventNumber - 1);
            }
        }.runTaskLater(this, durationInSeconds * 20L);
    }

    public void endEvent(int eventNumber) {
        String eventName = ongoingEvents.remove(eventNumber);
        Set<String> players = eventPlayers.remove(eventNumber);
        if (eventName != null) {
            getServer().broadcastMessage("The Mazerunner event '" + eventName + "' has ended!");
            Location respawnLocation = new Location(Bukkit.getWorld("world"), 0, 64, 0);
            for (String playerName : players) {
                Player player = Bukkit.getPlayer(playerName);
                if (player != null) {
                    player.teleport(respawnLocation);
                }
            }
        } else {
            getServer().broadcastMessage("No Mazerunner event found with number " + eventNumber);
        }
    }

    public Map<Integer, String> getOngoingEvents() {
        return ongoingEvents;
    }

    public Set<String> getEventPlayers(int eventNumber) {
        return eventPlayers.getOrDefault(eventNumber, new HashSet<>());
    }
}