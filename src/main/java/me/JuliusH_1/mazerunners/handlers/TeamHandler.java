package me.JuliusH_1.mazerunners.handlers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class TeamHandler {

    private final Scoreboard scoreboard;
    private final Map<Integer, Team> teams = new HashMap<>();

    public TeamHandler() {
        this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    public void createTeams(int teamNumbers) {
        for (int i = 1; i <= teamNumbers; i++) {
            Team team = scoreboard.registerNewTeam("team" + i);
            team.setDisplayName("Team " + i);
            teams.put(i, team);
        }
    }

    public void addPlayerToTeam(Player player, int teamNumber) {
        Team team = teams.get(teamNumber);
        if (team != null) {
            team.addEntry(player.getName());
        }
    }

    public void removePlayerFromTeam(Player player, int teamNumber) {
        Team team = teams.get(teamNumber);
        if (team != null) {
            team.removeEntry(player.getName());
        }
    }

    public Team getTeam(int teamNumber) {
        return teams.get(teamNumber);
    }

    public Map<Integer, Team> getTeams() {
        return teams;
    }
}