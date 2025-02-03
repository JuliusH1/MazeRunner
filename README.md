# MazeRunner Plugin

MazeRunner is a Minecraft plugin that adds a maze-running event to the game. Players can join teams, set spawn points, and compete in different game modes.

###This plugin is still in development and not yet ready for production use.

###This plugin has been created for the server [VoidCraft Network](https://shop.voidcraftmc.net)

## Features

- Start and end maze-running events
- Manage teams (create, delete, list, add, remove players)
- Set respawn and team spawn locations
- Spectate team members

## Commands

- `/mazerunner start <teamnumbers> <amountofplayersinteam> <hardcore|survival>`: Start a new maze-running event
- `/mazerunner respawn`: Set the respawn location
- `/mazerunner spectate`: Spectate a team member
- `/mazerunner teamspawn <teamnumber>`: Set the spawn location for a team
- `/mazerunner end <eventNumber>`: End an ongoing event
- `/mazerunner events`: List ongoing events
- `/mazerunner players <eventNumber>`: List players in an event
- `/mazerunner team <create|delete|list|add|remove> [arguments]`: Manage teams

## Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/JuliusH1/MazeRunner.git