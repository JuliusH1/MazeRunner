package me.JuliusH_1.mazerunners.listeners;

import me.JuliusH_1.mazerunners.MazerunnerCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerDeathListener implements Listener {

    private final JavaPlugin plugin;
    private final MazerunnerCommandExecutor commandExecutor;

    public PlayerDeathListener(JavaPlugin plugin, MazerunnerCommandExecutor commandExecutor) {
        this.plugin = plugin;
        this.commandExecutor = commandExecutor;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String deathMessage = plugin.getConfig().getString("death-message").replace("{player}", player.getName());
        event.setDeathMessage(deathMessage);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
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
                player.teleport(respawnLocation);
            }

            // Make operators invincible by setting their game mode to creative
            if (player.isOp()) {
                player.setGameMode(GameMode.CREATIVE);
            }
        }, 1L); // Delay of 1 tick to allow the respawn screen to appear
    }

    private int getTeamNumber(Player player) {
        // Implement logic to determine the player's team number
        // This is a placeholder implementation
        return 1; // Example team number
    }
}