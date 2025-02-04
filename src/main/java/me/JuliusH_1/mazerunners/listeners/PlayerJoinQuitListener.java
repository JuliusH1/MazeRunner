// PlayerJoinQuitListener.java
package me.JuliusH_1.mazerunners.listeners;

import me.JuliusH_1.mazerunners.Mazerunners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    private final Mazerunners plugin;

    public PlayerJoinQuitListener(Mazerunners plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String welcomeMessage = plugin.getWelcomeMessage();
        if (welcomeMessage != null && !welcomeMessage.isEmpty()) {
            event.getPlayer().sendMessage(welcomeMessage);
        }
        plugin.getScoreboardManager().updateScoreboard(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Handle player quit event if needed
    }
}