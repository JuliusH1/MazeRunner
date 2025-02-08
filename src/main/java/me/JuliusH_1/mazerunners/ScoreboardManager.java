package me.JuliusH_1.mazerunners;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardManager {

    private final Mazerunners plugin;
    private final MazerunnerCommandExecutor commandExecutor;
    private static Scoreboard scoreboard;

    public ScoreboardManager(Mazerunners plugin, MazerunnerCommandExecutor commandExecutor) {
        this.plugin = plugin;
        this.commandExecutor = commandExecutor;
    }

    public void updateScoreboard(Player player) {
        FileConfiguration config = plugin.getConfig();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.getObjective("mazerunner");

        if (objective == null) {
            objective = scoreboard.registerNewObjective("mazerunner", "dummy", config.getString("scoreboard.title"));
            objective.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
        }

        int score = config.getStringList("scoreboard.lines").size();
        for (String line : config.getStringList("scoreboard.lines")) {
            line = line.replace("{teams}", String.valueOf(Bukkit.getOnlinePlayers().size() / 4));
            line = line.replace("{players}", String.valueOf(Bukkit.getOnlinePlayers().size()));
            line = line.replace("{gamemode}", commandExecutor.getGameMode());
            for (int i = 1; i <= 4; i++) {
                Team team = scoreboard.getTeam("Team" + i);
                if (team == null) {
                    team = scoreboard.registerNewTeam("Team" + i);
                }
                // Add player to the team if they are part of it
                if (commandExecutor.isPlayerInTeam(player, i)) {
                    team.addEntry(player.getName());
                }
                int playersLeft = team.getSize();
                String teamDisplay = "Team " + i + ": " + playersLeft;
                if (team.hasEntry(player.getName())) {
                    teamDisplay += " (you)";
                }
                line = line.replace("{team" + i + "}", teamDisplay);
            }
            objective.getScore(line).setScore(score--);
        }

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboardForAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateScoreboard(player);
        }
    }

    public static void loadScoreboard() {
        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager != null) {
            scoreboard = manager.getNewScoreboard();
        }
    }

    public static Scoreboard getScoreboard() {
        return scoreboard;
    }
}