package me.JuliusH_1.mazerunners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class MazerunnerCommandExecutor implements CommandExecutor {

    private final JavaPlugin plugin;
    private Location respawnLocation;
    private final Map<Integer, Location> teamSpawns = new HashMap<>();
    private String gameMode;

    public MazerunnerCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
        this.gameMode = "";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("Usage: /mazerunner <start|respawn|spectate|teamspawn|end|events|players|team>");
            return false;
        }

        if (args[0].equalsIgnoreCase("start")) {
            if (args.length != 4) {
                sender.sendMessage("Usage: /mazerunner start <teamnumbers> <amountofplayersinteam> <hardcore|survival>");
                return false;
            }
            int teamNumbers = Integer.parseInt(args[1]);
            int playersPerTeam = Integer.parseInt(args[2]);
            gameMode = args[3].toLowerCase();
            if (!gameMode.equals("hardcore") && !gameMode.equals("survival")) {
                sender.sendMessage("Invalid game mode. Use 'hardcore' or 'survival'.");
                return false;
            }
            Set<Player> players = new HashSet<>(Bukkit.getOnlinePlayers());
            ((Mazerunners) plugin).startEvent(players);
            startEvent(teamNumbers, playersPerTeam);
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
        } else if (args[0].equalsIgnoreCase("end")) {
            if (args.length != 2) {
                sender.sendMessage("Usage: /mazerunner end <eventNumber>");
                return false;
            }
            int eventNumber = Integer.parseInt(args[1]);
            ((Mazerunners) plugin).endEvent(eventNumber);
            return true;
        } else if (args[0].equalsIgnoreCase("events")) {
            Map<Integer, String> ongoingEvents = ((Mazerunners) plugin).getOngoingEvents();
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
            int eventNumber = Integer.parseInt(args[1]);
            Set<String> players = ((Mazerunners) plugin).getEventPlayers(eventNumber);
            if (players.isEmpty()) {
                sender.sendMessage("No players found for event number " + eventNumber);
            } else {
                sender.sendMessage("Players in event number " + eventNumber + ":");
                for (String player : players) {
                    sender.sendMessage("- " + player);
                }
            }
            return true;
        } else if (args[0].equalsIgnoreCase("team")) {
            if (args.length != 4) {
                sender.sendMessage("Usage: /mazerunner team <teamnumber> add/remove <playername>");
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
                sender.sendMessage("Invalid action. Use 'add' or 'remove'.");
                return false;
            }
            return true;
        }

        return false;
    }

    private void startEvent(int teamNumbers, int playersPerTeam) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        // Clear existing teams
        for (Team team : scoreboard.getTeams()) {
            team.unregister();
        }

        // Create new teams
        List<Team> teams = new ArrayList<>();
        for (int i = 1; i <= teamNumbers; i++) {
            Team team = scoreboard.registerNewTeam("Team" + i);
            teams.add(team);
        }

        // Get all online players and shuffle them
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        Collections.shuffle(players);

        // Assign players to teams and teleport them to their team spawn locations
        int teamIndex = 0;
        for (Player player : players) {
            if (teams.get(teamIndex).getSize() < playersPerTeam) {
                teams.get(teamIndex).addEntry(player.getName());
                Location spawnLocation = teamSpawns.get(teamIndex + 1);
                if (spawnLocation != null) {
                    player.teleport(spawnLocation);
                } else {
                    player.sendMessage("No spawn location set for team " + (teamIndex + 1));
                }
            } else {
                teamIndex++;
                if (teamIndex >= teams.size()) {
                    break;
                }
                teams.get(teamIndex).addEntry(player.getName());
                Location spawnLocation = teamSpawns.get(teamIndex + 1);
                if (spawnLocation != null) {
                    player.teleport(spawnLocation);
                } else {
                    player.sendMessage("No spawn location set for team " + (teamIndex + 1));
                }
            }
        }

        Bukkit.broadcastMessage("Starting Mazerunner event with " + teamNumbers + " teams and " + playersPerTeam + " players per team in " + gameMode + " mode.");
    }

    private void spectateTeam(Player player) {
        Team team = player.getScoreboard().getEntryTeam(player.getName());
        if (team != null) {
            for (String entry : team.getEntries()) {
                Player teamMember = Bukkit.getPlayer(entry);
                if (teamMember != null && !teamMember.equals(player)) {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.teleport(teamMember.getLocation());
                    player.sendMessage("You are now spectating " + teamMember.getName());
                    return;
                }
            }
            player.sendMessage("No team members to spectate.");
        } else {
            player.sendMessage("You are not in a team.");
        }
    }

    private void addPlayerToTeam(Player player, int teamNumber) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("Team" + teamNumber);
        if (team != null) {
            team.addEntry(player.getName());
        } else {
            player.sendMessage("Team " + teamNumber + " does not exist.");
        }
    }

    private void removePlayerFromTeam(Player player, int teamNumber) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("Team" + teamNumber);
        if (team != null) {
            team.removeEntry(player.getName());
        } else {
            player.sendMessage("Team " + teamNumber + " does not exist.");
        }
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
}