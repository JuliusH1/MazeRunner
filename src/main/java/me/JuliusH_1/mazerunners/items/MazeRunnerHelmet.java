package me.JuliusH_1.mazerunners.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MazeRunnerHelmet {
    public static ItemStack getMazeRunnerHelmet() {
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemMeta meta = helmet.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(1); // Set custom model data to match the model JSON
            helmet.setItemMeta(meta);
        }
        return helmet;
    }
}