package de.gigaz.cores.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.IngameState;
import de.gigaz.cores.classes.LobbyState;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.Team;

public class MainCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			GameManager gameManager = Main.getPlugin().getGameManager();
			if(player.hasPermission("cores.admin")) {
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("setlobby")) {
						setConfigGeneralLocation(player, "lobbyspawn");
						player.sendMessage(Main.PREFIX + "§7Du hast den Lobbyspawn gesetzt");
					} else if(args[0].equalsIgnoreCase("start")) {
						player.sendMessage(Main.PREFIX + "§7Du hast das Spiel gestartet");
						LobbyState.stop();
					} else if(args[0].equalsIgnoreCase("stop")) {
						IngameState.stop();
						player.sendMessage(Main.PREFIX + "§7Du hast das Spiel beendet");
					} else if(args[0].equalsIgnoreCase("setdeathhight")) {
						setConfigLocation("deathhight", player.getLocation());
						player.sendMessage(Main.PREFIX + "§7Du hast die DeathHight auf §6" + Math.round(getConfigLocation("deathhight", player.getWorld()).getY()) + " §7gesetzt");
					}
				} else if(args.length == 2) {
					
					if(args[0].equalsIgnoreCase("configure")) {				
						World world = Main.getPlugin().getWorld(args[1]);
						player.teleport(world.getSpawnLocation());
						FileConfiguration config = Main.getPlugin().getConfig();
						config.set(Main.CONFIG_ROOT + "worlds." + args[1], null);
						Main.getPlugin().saveConfig();
						player.sendMessage(Main.PREFIX + "§7Du hast die Welt §2" + args[1] + "§7erstellt");
						
					} else if(args[0].equalsIgnoreCase("setspawn")) {
						if(args[1].equalsIgnoreCase("blue")) {
							setConfigLocation("blue.spawn", player.getLocation());
							player.sendMessage(Main.PREFIX + "§7Du hast den Spawn von Team " + Team.BLUE.getDisplayColor() + " §gesetzt");
						} else if(args[1].equalsIgnoreCase("red")) {
							setConfigLocation("red.spawn", player.getLocation());
							player.sendMessage(Main.PREFIX + "§7Du hast den Spawn von Team " + Team.RED.getDisplayColor() + " §gesetzt");
						}
						
					} else if(args[0].equalsIgnoreCase("join")) {
						if(args[1].equalsIgnoreCase("blue")) {
							Main.getPlugin().getGameManager().getPlayerProfile(player).setTeam(Team.BLUE);
							Main.getPlugin().getGameManager().getPlayerProfile(player).getPlayer().sendMessage(Main.PREFIX + "§7Du bist dem Team " + Team.BLUE.getDisplayColor() + " §7beigetreten");
						} else if(args[1].equalsIgnoreCase("red")) {
							Main.getPlugin().getGameManager().getPlayerProfile(player).setTeam(Team.RED);
							player.sendMessage(Main.PREFIX + "§7Du bist dem Team " + Team.RED.getDisplayColor() + " §7beigetreten");
						} else {
							
						}
					
					} else if(args[0].equalsIgnoreCase("setmap")) {
						Main.getPlugin().setMap(args[1]);
					} else if(args[0].equalsIgnoreCase("deletemap")) {
						
					}
					
				} else if(args.length == 3) {
					
					if(args[0].equalsIgnoreCase("setcore")) {
						if(args[1].equalsIgnoreCase("blue")) {
							if(args[2].equalsIgnoreCase("1")) {
								new SetCoreCommand(player, Team.BLUE, 1);
							} else if(args[2].equalsIgnoreCase("2")) {
								new SetCoreCommand(player, Team.BLUE, 2);
							}
							
						} else if(args[1].equalsIgnoreCase("red")) {
							if(args[2].equalsIgnoreCase("1")) {
								new SetCoreCommand(player, Team.RED, 1);
							} else if(args[2].equalsIgnoreCase("2")) {
								new SetCoreCommand(player, Team.RED, 2);
							}
							
						} else {
							
						}
					}
				}
			}
		}
		return false;	
	}

	public static void setConfigLocation(String root, Location location) {
		FileConfiguration config = Main.getPlugin().getConfig();
		config.set(Main.CONFIG_ROOT + "worlds." + location.getWorld().getName() + "." + root, location);
		Main.getPlugin().saveConfig();
	}
	
	public static Location getConfigLocation(String root, World world) {
		return getConfigLocation(root, world, true);
	}
	
	public static Location getConfigLocation(String root, World world, boolean modifyWorld) {
		FileConfiguration config = Main.getPlugin().getConfig();
		root = Main.CONFIG_ROOT + "worlds." + world.getName() + "." + root;
		//Bukkit.broadcastMessage(root);
		Location location = config.getLocation(root);
		if(root != null) {
			if(modifyWorld) {
				location.setWorld(Main.getPlugin().getWorld("currentWorld"));
			}
		}
		return location;
	}

	
	public static void setConfigGeneralLocation(Player player, String root) {
		FileConfiguration config = Main.getPlugin().getConfig();
		config.set(Main.CONFIG_ROOT + root, player.getLocation());
		Main.getPlugin().saveConfig();
	}
	
	public static Location getConfigGeneralLocation(String root) {
		FileConfiguration config = Main.getPlugin().getConfig();
		return config.getLocation(Main.CONFIG_ROOT + root);
	}
}
