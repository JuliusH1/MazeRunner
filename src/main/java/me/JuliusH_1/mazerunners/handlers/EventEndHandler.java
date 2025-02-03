package me.JuliusH_1.mazerunners.handlers;

import me.JuliusH_1.mazerunners.Mazerunners;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Set;

public class EventEndHandler {

    private final Mazerunners plugin;
    private final TeamHandler teamHandler;
    private final EventHandler eventHandler;

    public EventEndHandler(Mazerunners plugin, TeamHandler teamHandler, EventHandler eventHandler) {
        this.plugin = plugin;
        this.teamHandler = teamHandler;
        this.eventHandler = eventHandler;
    }

    public void endEvent(int eventNumber) {
        String eventName = eventHandler.getEventName(eventNumber);
        Set<String> players = eventHandler.getEventPlayers(eventNumber);
        eventHandler.endEvent(eventNumber);

        if (eventName != null) {
            Bukkit.getServer().broadcastMessage("The Mazerunner event '" + eventName + "' has ended!");
            Location respawnLocation = new Location(Bukkit.getWorld("world"), 0, 64, 0);
            for (String playerName : players) {
                Player player = Bukkit.getPlayer(playerName);
                if (player != null) {
                    player.teleport(respawnLocation);
                }
            }
        } else {
            Bukkit.getServer().broadcastMessage("No Mazerunner event found with number " + eventNumber);
        }
    }
}