package me.JuliusH_1.mazerunners.items;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.inventory.ItemStack;

public class MazeRunnerHelmet {

    public static ItemStack getMazeRunnerHelmet() {
        CustomStack customStack = CustomStack.getInstance("mazerunner:helmet");
        if (customStack != null) {
            return customStack.getItemStack();
        }
        return null;
    }
}