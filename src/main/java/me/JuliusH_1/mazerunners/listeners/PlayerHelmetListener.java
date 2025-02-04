package me.JuliusH_1.mazerunners.listeners;

import me.JuliusH_1.mazerunners.items.MazeRunnerHelmet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerHelmetListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        applySpeedEffect(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        removeSpeedEffect(event.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        applySpeedEffect(event.getPlayer());
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        applySpeedEffect(event.getPlayer());
    }

    private void applySpeedEffect(Player player) {
        ItemStack helmet = player.getInventory().getHelmet();
        if (helmet != null && helmet.isSimilar(MazeRunnerHelmet.getMazeRunnerHelmet())) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
        } else {
            removeSpeedEffect(player);
        }
    }

    private void removeSpeedEffect(Player player) {
        player.removePotionEffect(PotionEffectType.SPEED);
    }
}