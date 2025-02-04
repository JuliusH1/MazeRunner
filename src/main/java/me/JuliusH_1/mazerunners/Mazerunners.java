package me.JuliusH_1.mazerunners;

import me.JuliusH_1.mazerunners.handlers.*;
import me.JuliusH_1.mazerunners.listeners.PlayerDeathListener;
import me.JuliusH_1.mazerunners.listeners.PlayerHelmetListener;
import me.JuliusH_1.mazerunners.listeners.PlayerJoinQuitListener;
import me.JuliusH_1.mazerunners.listeners.PlayerRespawnListener;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.List;

public final class Mazerunners extends JavaPlugin {

    private TeamHandler teamHandler;
    private EventHandler eventHandler;
    private EventStartHandler eventStartHandler;
    private EventEndHandler eventEndHandler;
    private ScoreboardManager scoreboardManager;
    private String welcomeMessage;
    private String deathMessage;
    private boolean scoreboardEnabled;
    private String scoreboardTitle;
    private List<String> scoreboardLines;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        teamHandler = new TeamHandler();
        eventHandler = new EventHandler();
        eventStartHandler = new EventStartHandler(this, teamHandler, eventHandler);
        eventEndHandler = new EventEndHandler(this, teamHandler, eventHandler);
        loadMessages();
        loadScoreboard();
        scoreboardManager = new ScoreboardManager(this, new MazerunnerCommandExecutor(this, eventStartHandler, eventEndHandler, eventHandler));
        getCommand("mazerunner").setExecutor(new MazerunnerCommandExecutor(this, eventStartHandler, eventEndHandler, eventHandler));
        getCommand("mazerunner").setTabCompleter(new MazerunnerTabCompleter());
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerHelmetListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this, new MazerunnerCommandExecutor(this, eventStartHandler, eventEndHandler, eventHandler), teamHandler), this);
        getLogger().info("Mazerunners plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Mazerunners plugin disabled!");
    }

    public void loadMessages() {
        welcomeMessage = getConfig().getString("welcome-message");
        deathMessage = getConfig().getString("death-message");
    }

    public void loadScoreboard() {
        ScoreboardManager.loadScoreboard();
        scoreboardEnabled = getConfig().getBoolean("scoreboard.enabled");
        scoreboardTitle = getConfig().getString("scoreboard.title");
        scoreboardLines = getConfig().getStringList("scoreboard.lines");
        if (scoreboardEnabled) {
            // Initialize and update the scoreboard here
            // Example: createScoreboard();
        }
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public String getDeathMessage() {
        return deathMessage;
    }

    public boolean isScoreboardEnabled() {
        return scoreboardEnabled;
    }

    public String getScoreboardTitle() {
        return scoreboardTitle;
    }

    public List<String> getScoreboardLines() {
        return scoreboardLines;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
}