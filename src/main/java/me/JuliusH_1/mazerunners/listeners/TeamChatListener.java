package me.JuliusH_1.mazerunners.listeners;

import me.JuliusH_1.mazerunners.MazerunnerCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Team;

public class TeamChatListener implements Listener {

    private final MazerunnerCommandExecutor commandExecutor;

    public TeamChatListener(MazerunnerCommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (message.startsWith("@team ")) {
            event.setCancelled(true);
            Team team = player.getScoreboard().getEntryTeam(player.getName());
            if (team != null) {
                String teamMessage = ChatColor.GREEN + "[Team] " + player.getName() + ": " + message.substring(6);
                for (String entry : team.getEntries()) {
                    Player teamMember = Bukkit.getPlayer(entry);
                    if (teamMember != null) {
                        teamMember.sendMessage(teamMessage);
                    }
                }
            } else {
                player.sendMessage(ChatColor.RED + "You are not in a team.");
            }
        }
    }
}