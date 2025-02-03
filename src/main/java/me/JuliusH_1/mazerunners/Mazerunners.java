package me.JuliusH_1.mazerunners;

import me.JuliusH_1.mazerunners.handlers.EventHandler;
import me.JuliusH_1.mazerunners.handlers.EventStartHandler;
import me.JuliusH_1.mazerunners.handlers.TeamHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mazerunners extends JavaPlugin {

    private TeamHandler teamHandler;
    private EventHandler eventHandler;
    private EventStartHandler eventStartHandler;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        teamHandler = new TeamHandler();
        eventHandler = new EventHandler();
        eventStartHandler = new EventStartHandler(this, teamHandler, eventHandler);
        getCommand("mazerunner").setExecutor(new MazerunnerCommandExecutor(this, eventStartHandler));
        getCommand("event").setExecutor(new MazerunnerCommandExecutor(this, eventStartHandler));
        getLogger().info("Mazerunners plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Mazerunners plugin disabled!");
    }
}