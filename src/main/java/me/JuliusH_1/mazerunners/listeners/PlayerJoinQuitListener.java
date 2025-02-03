package me.JuliusH_1.mazerunners.listeners;

import me.JuliusH_1.mazerunners.Mazerunners;
import me.JuliusH_1.mazerunners.ScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    private final ScoreboardManager scoreboardManager;
    private final Mazerunners plugin;

    public PlayerJoinQuitListener(ScoreboardManager scoreboardManager, Mazerunners plugin) {
        this.scoreboardManager = scoreboardManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String welcomeMessage = plugin.getConfig().getString("welcome-message").replace("{player}", player.getName());
        player.sendMessage(welcomeMessage);
        scoreboardManager.updateScoreboard(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Optionally handle player quit event
    }
}