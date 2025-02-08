package me.JuliusH_1.mazerunners;

import me.JuliusH_1.mazerunners.handlers.EventEndHandler;
import me.JuliusH_1.mazerunners.handlers.EventHandler;
import me.JuliusH_1.mazerunners.handlers.EventStartHandler;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class MazerunnerCommandExecutor implements CommandExecutor {

    private final JavaPlugin plugin;
    private final EventStartHandler eventStartHandler;
    private final EventEndHandler eventEndHandler;
    private final EventHandler eventHandler;
    private Location respawnLocation;
    private final Map<Integer, Location> teamSpawns;
    private String gameMode;

    public MazerunnerCommandExecutor(JavaPlugin plugin, EventStartHandler eventStartHandler, EventEndHandler eventEndHandler, EventHandler eventHandler) {
        this.plugin = plugin;
        this.eventStartHandler = eventStartHandler;
        this.eventEndHandler = eventEndHandler;
        this.eventHandler = eventHandler;
        this.teamSpawns = new HashMap<>();
        this.gameMode = "";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /mazerunner <start|respawn|spectate|teamspawn|events|players|team|event|reload>");
            return false;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            ((Mazerunners) plugin).loadMessages();
            ((Mazerunners) plugin).loadScoreboard();
            sender.sendMessage("Mazerunner configuration reloaded.");
            logConfigSettings();
            return true;
        }

        if (args[0].equalsIgnoreCase("start")) {
            if (args.length != 5) {
                sender.sendMessage("Usage: /mazerunner start <teamnumbers> <amountofplayersinteam> <hardcore|survival> <durationInSeconds>");
                return false;
            }
            int teamNumbers = Integer.parseInt(args[1]);
            int playersPerTeam = Integer.parseInt(args[2]);
            gameMode = args[3].toLowerCase();
            int durationInSeconds = Integer.parseInt(args[4]);
            if (!gameMode.equals("hardcore") && !gameMode.equals("survival")) {
                sender.sendMessage("Invalid game mode. Use 'hardcore' or 'survival'.");
                return false;
            }
            Set<Player> players = new HashSet<>(Bukkit.getOnlinePlayers());
            eventStartHandler.startEvent(players, teamNumbers, playersPerTeam, gameMode, durationInSeconds);
            return true;
        } else if (args[0].equalsIgnoreCase("respawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                respawnLocation = player.getLocation();
                sender.sendMessage("Respawn location set!");
            } else {
                sender.sendMessage("Only players can set the respawn location.");
            }
            return true;
        } else if (args[0].equalsIgnoreCase("spectate")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                spectateTeam(player);
            } else {
                sender.sendMessage("Only players can spectate.");
            }
            return true;
        } else if (args[0].equalsIgnoreCase("teamspawn")) {
            if (args.length != 2) {
                sender.sendMessage("Usage: /mazerunner teamspawn <teamnumber>");
                return false;
            }
            if (sender instanceof Player) {
                Player player = (Player) sender;
                int teamNumber = Integer.parseInt(args[1]);
                teamSpawns.put(teamNumber, player.getLocation());
                sender.sendMessage("Spawn location for team " + teamNumber + " set!");
            } else {
                sender.sendMessage("Only players can set the team spawn location.");
            }
            return true;
        } else if (args[0].equalsIgnoreCase("events")) {
            Map<Integer, String> ongoingEvents = eventHandler.getOngoingEvents();
            if (ongoingEvents.isEmpty()) {
                sender.sendMessage("There are no ongoing Mazerunner events.");
            } else {
                sender.sendMessage("Ongoing Mazerunner events:");
                for (Map.Entry<Integer, String> event : ongoingEvents.entrySet()) {
                    sender.sendMessage("- " + event.getKey() + ": " + event.getValue());
                }
            }
            return true;
        } else if (args[0].equalsIgnoreCase("players")) {
            if (args.length != 2) {
                sender.sendMessage("Usage: /mazerunner players <eventNumber>");
                return false;
            }
            try {
                int eventNumber = Integer.parseInt(args[1]);
                Set<String> players = eventHandler.getEventPlayers(eventNumber);
                if (players.isEmpty()) {
                    sender.sendMessage("No players found for event number " + eventNumber);
                } else {
                    sender.sendMessage("Players in event number " + eventNumber + ":");
                    for (String player : players) {
                        sender.sendMessage("- " + player);
                    }
                }
            } catch (NumberFormatException e) {
                sender.sendMessage("Invalid event number.");
            }
            return true;
        } else if (args[0].equalsIgnoreCase("team")) {
            if (args.length < 3) {
                sender.sendMessage("Usage: /mazerunner team <teamnumber> <add|remove|list> [playername]");
                return false;
            }
            int teamNumber;
            try {
                teamNumber = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                sender.sendMessage("Invalid team number.");
                return false;
            }

            String action = args[2];
            if (action.equalsIgnoreCase("list")) {
                listTeamPlayers(sender, teamNumber);
                return true;
            }

            if (args.length != 4) {
                sender.sendMessage("Usage: /mazerunner team <teamnumber> <add|remove> <playername>");
                return false;
            }

            String playerName = args[3];
            Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                sender.sendMessage("Player not found.");
                return false;
            }

            if (action.equalsIgnoreCase("add")) {
                addPlayerToTeam(player, teamNumber);
                sender.sendMessage("Player " + playerName + " added to team " + teamNumber);
            } else if (action.equalsIgnoreCase("remove")) {
                removePlayerFromTeam(player, teamNumber);
                sender.sendMessage("Player " + playerName + " removed from team " + teamNumber);
            } else {
                sender.sendMessage("Invalid action. Use 'add', 'remove', or 'list'.");
                return false;
            }
            return true;
        } else if (args[0].equalsIgnoreCase("event")) {
            if (args.length == 2 && args[1].equalsIgnoreCase("help")) {
                showHelp(sender);
                return true;
            } else if (args.length == 3 && args[1].equalsIgnoreCase("end")) {
                int eventNumber = Integer.parseInt(args[2]);
                eventEndHandler.endEvent(eventNumber);
                return true;
            } else {
                sender.sendMessage("Usage: /mazerunner event <help|end> [eventNumber]");
                return false;
            }
        }

        return false;
    }

    private void showHelp(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage("§6§lMaze§e§lRunner §6Commands:");
            player.sendMessage("§e/mazerunner event help - Show this help message.");
            player.sendMessage("§e/mazerunner reload - reload the config.yml");
            player.sendMessage("§e@team <message> - Send a message to your team.");
            // Add more commands and features as needed
        } else {
            sender.sendMessage("This command can only be used by players.");
        }
    }

    private void spectateTeam(Player player) {
        if ("survival".equalsIgnoreCase(gameMode)) {
            player.sendMessage("You cannot use this command in a survival event!");
            return;
        }

        Team team = player.getScoreboard().getPlayerTeam(player);
        if (team != null) {
            boolean foundTeamMember = false;
            for (String entry : team.getEntries()) {
                Player teamMember = Bukkit.getPlayer(entry);
                if (teamMember != null && !teamMember.equals(player)) {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.teleport(teamMember.getLocation());
                    player.sendMessage("You are now spectating " + teamMember.getName());
                    foundTeamMember = true;
                    break;
                }
            }
            if (!foundTeamMember) {
                player.sendMessage("There is no one to spectate!");
            }
        } else {
            player.sendMessage("You are not in a team!");
        }
    }

    private void addPlayerToTeam(Player player, int teamNumber) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("team" + teamNumber);
        if (team != null) {
            team.addEntry(player.getName());
        } else {
            player.sendMessage("Team " + teamNumber + " does not exist.");
        }
    }

    private void removePlayerFromTeam(Player player, int teamNumber) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("team" + teamNumber);
        if (team != null) {
            team.removeEntry(player.getName());
        } else {
            player.sendMessage("Team " + teamNumber + " does not exist.");
        }
    }

    private void listTeamPlayers(CommandSender sender, int teamNumber) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("team" + teamNumber);
        if (team == null) {
            sender.sendMessage("Team " + teamNumber + " does not exist.");
            return;
        }

        Set<String> players = team.getEntries();
        if (players.isEmpty()) {
            sender.sendMessage("No players in team " + teamNumber);
        } else {
            sender.sendMessage("Players in team " + teamNumber + ":");
            for (String player : players) {
                sender.sendMessage("- " + player);
            }
        }
    }

    private void logConfigSettings() {
        Logger logger = plugin.getLogger();
        FileConfiguration config = plugin.getConfig();

        logger.info("Mazerunner configuration reloaded:");
        logger.info("Welcome Message: " + config.getString("welcome-message"));
        logger.info("Death Message: " + config.getString("death-message"));
        logger.info("Scoreboard Enabled: " + config.getBoolean("scoreboard.enabled"));
        logger.info("Scoreboard Title: " + config.getString("scoreboard.title"));
        logger.info("Scoreboard Lines: " + config.getStringList("scoreboard.lines"));
    }

    public Location getRespawnLocation() {
        return respawnLocation;
    }

    public Location getTeamSpawnLocation(int teamNumber) {
        return teamSpawns.get(teamNumber);
    }

    public String getGameMode() {
        return gameMode;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public boolean isPlayerInTeam(Player player, int teamNumber) {
        Scoreboard scoreboard = player.getScoreboard();
        Team team = scoreboard.getTeam("Team" + teamNumber);
        return team != null && team.hasEntry(player.getName());
    }
}