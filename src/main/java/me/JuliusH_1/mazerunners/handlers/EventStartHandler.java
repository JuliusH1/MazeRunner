package me.JuliusH_1.mazerunners.handlers;

import me.JuliusH_1.mazerunners.Mazerunners;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class EventStartHandler {

    private final Mazerunners plugin;
    private final TeamHandler teamHandler;
    private final EventHandler eventHandler;

    public EventStartHandler(Mazerunners plugin, TeamHandler teamHandler, EventHandler eventHandler) {
        this.plugin = plugin;
        this.teamHandler = teamHandler;
        this.eventHandler = eventHandler;
    }

    public void startEvent(Set<Player> players, int teamNumbers, int playersPerTeam, String gameMode, int durationInSeconds) {
        teamHandler.createTeams(teamNumbers);

        Set<String> playerNames = new HashSet<>();
        int teamIndex = 1;
        for (Player player : players) {
            playerNames.add(player.getName());
            teamHandler.addPlayerToTeam(player, teamIndex);
            if (teamHandler.getTeam(teamIndex).getEntries().size() >= playersPerTeam) {
                teamIndex++;
            }
        }

        int eventNumber = eventHandler.startEvent(playerNames);
        Bukkit.getServer().broadcastMessage("The Mazerunner event 'Event_" + eventNumber + "' has started with " + teamNumbers + " teams, " + playersPerTeam + " players per team, in " + gameMode + " mode!");

        if (durationInSeconds != -1) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    new EventEndHandler(plugin, teamHandler, eventHandler).endEvent(eventNumber);
                }
            }.runTaskLater(plugin, durationInSeconds * 20L);
        }
    }

    public void endEvent(int eventNumber) {
        eventHandler.endEvent(eventNumber);
        Bukkit.getServer().broadcastMessage("The Mazerunner event 'Event_" + eventNumber + "' has ended!");
    }
}