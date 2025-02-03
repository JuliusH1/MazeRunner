package me.JuliusH_1.mazerunners;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MazerunnerTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("mazerunner")) {
            if (args.length == 1) {
                return Arrays.asList("start", "respawn", "spectate", "teamspawn", "end", "events", "players", "team");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("team")) {
                return Arrays.asList("1", "2", "3", "4"); // List of team numbers
            } else if (args.length == 3 && args[0].equalsIgnoreCase("team")) {
                return Arrays.asList("add", "remove", "list");
            } else if (args.length == 4 && args[0].equalsIgnoreCase("team")) {
                return Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .collect(Collectors.toList());
            } else if (args.length == 2 && args[0].equalsIgnoreCase("start")) {
                return Arrays.asList("2", "3", "4", "5");
            } else if (args.length == 3 && args[0].equalsIgnoreCase("start")) {
                return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16");
            } else if (args.length == 4 && args[0].equalsIgnoreCase("start")) {
                return Arrays.asList("hardcore", "survival");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("teamspawn")) {
                return Arrays.asList("1", "2", "3", "4");
            } else if (args.length == 2 && args[0].equalsIgnoreCase("event")) {
                return Arrays.asList("end");
            }
        }
        return new ArrayList<>();
    }
}