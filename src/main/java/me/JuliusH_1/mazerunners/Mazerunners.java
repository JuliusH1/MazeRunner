package me.JuliusH_1.mazerunners;

import me.JuliusH_1.mazerunners.listeners.PlayerDeathListener;
import me.JuliusH_1.mazerunners.listeners.PlayerRespawnListener;
import me.JuliusH_1.mazerunners.listeners.PlayerJoinQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Mazerunners extends JavaPlugin {

    private MazerunnerCommandExecutor commandExecutor;
    private final Map<Integer, String> ongoingEvents = new HashMap<>();
    private final Map<Integer, Set<String>> eventPlayers = new HashMap<>();
    private int nextEventNumber = 1;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        commandExecutor = new MazerunnerCommandExecutor(this);
        getCommand("mazerunner").setExecutor(commandExecutor);
        getCommand("mazerunner").setTabCompleter(new MazerunnerTabCompleter());
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this, commandExecutor), this);
        getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), this);

        ScoreboardManager scoreboardManager = new ScoreboardManager(this, commandExecutor);
        getServer().getPluginManager().registerEvents(new PlayerJoinQuitListener(scoreboardManager), this);

        if (getConfig().getBoolean("scoreboard.enabled")) {
            getServer().getScheduler().runTaskTimer(this, () -> {
                getServer().getOnlinePlayers().forEach(scoreboardManager::updateScoreboard);
            }, 0L, 20L * 5);
        }

        getLogger().info("Mazerunners plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Mazerunners plugin disabled!");
    }

    public void startEvent(Set<Player> players) {
        String eventName = "Event_" + nextEventNumber;
        ongoingEvents.put(nextEventNumber, eventName);
        Set<String> playerNames = new HashSet<>();
        for (Player player : players) {
            playerNames.add(player.getName());
        }
        eventPlayers.put(nextEventNumber, playerNames);
        nextEventNumber++;
        getServer().broadcastMessage("The Mazerunner event '" + eventName + "' has started!");
    }

    public void endEvent(int eventNumber) {
        String eventName = ongoingEvents.remove(eventNumber);
        Set<String> players = eventPlayers.remove(eventNumber);
        if (eventName != null) {
            getServer().broadcastMessage("The Mazerunner event '" + eventName + "' has ended!");
            Location respawnLocation = commandExecutor.getRespawnLocation();
            if (respawnLocation != null) {
                for (String playerName : players) {
                    Player player = Bukkit.getPlayer(playerName);
                    if (player != null) {
                        player.teleport(respawnLocation);
                    }
                }
            }
        } else {
            getServer().broadcastMessage("No Mazerunner event found with number " + eventNumber);
        }
    }

    public Map<Integer, String> getOngoingEvents() {
        return ongoingEvents;
    }

    public Set<String> getEventPlayers(int eventNumber) {
        return eventPlayers.getOrDefault(eventNumber, new HashSet<>());
    }

    public MazerunnerCommandExecutor getCommandExecutor() {
        return commandExecutor;
    }
}