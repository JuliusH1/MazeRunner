package me.JuliusH_1.mazerunners.listeners;

import me.JuliusH_1.mazerunners.MazerunnerCommandExecutor;
import me.JuliusH_1.mazerunners.Mazerunners;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    private final Mazerunners plugin;

    public PlayerRespawnListener(Mazerunners plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        MazerunnerCommandExecutor commandExecutor = (MazerunnerCommandExecutor) plugin.getCommand("mazerunner").getExecutor();
        String gameMode = commandExecutor.getGameMode();
        Location respawnLocation;

        if ("hardcore".equalsIgnoreCase(gameMode)) {
            respawnLocation = commandExecutor.getRespawnLocation();
        } else if ("survival".equalsIgnoreCase(gameMode)) {
            int teamNumber = getTeamNumber(player);
            respawnLocation = commandExecutor.getTeamSpawnLocation(teamNumber);
        } else {
            respawnLocation = new Location(plugin.getServer().getWorld("world"), 0, 64, 0); // Default location
        }

        if (respawnLocation != null) {
            event.setRespawnLocation(respawnLocation);
        }

        // Make operators invincible by setting their game mode to creative
        if (player.isOp()) {
            player.setGameMode(GameMode.CREATIVE);
        }
    }

    private int getTeamNumber(Player player) {
        // Implement logic to determine the player's team number
        // This is a placeholder implementation
        return 1; // Example team number
    }
}