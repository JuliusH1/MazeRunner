
package me.JuliusH_1.mazerunners.handlers;

import me.JuliusH_1.mazerunners.Mazerunners;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerEventHandler implements Listener {

    private final Mazerunners plugin;

    public PlayerEventHandler(Mazerunners plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String deathMessage = plugin.getDeathMessage();
        if (deathMessage != null && !deathMessage.isEmpty()) {
            event.setDeathMessage(deathMessage.replace("{player}", event.getEntity().getName()));
        }
    }
}