package de.gigaz.cores.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.gigaz.cores.classes.Core;
import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.IngameState;
import de.gigaz.cores.classes.LobbyState;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Team;

public class MainCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			GameManager gameManager = Main.getPlugin().getGameManager();
			PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
			
			if(args.length == 1) {
				if(player.hasPermission("cores.admin")) {
					if(args[0].equalsIgnoreCase("setlobby")) {
						setConfigGeneralLocation(player, "lobbyspawn");
						player.sendMessage(Main.PREFIX + "§7Du hast den Lobbyspawn gesetzt");
					} else if(args[0].equalsIgnoreCase("start")) {
						if(gameManager.getMap() != null) {
							if(gameManager.checkMap(gameManager.getMap())) {
								if(gameManager.getCurrentGameState() != GameState.INGAME_STATE) {
									player.sendMessage(Main.PREFIX + "§7Du hast das Spiel §6gestartet");
									LobbyState.stop();
								} else {
									player.sendMessage(Main.PREFIX + "§7Das Spiel hat bereits begonnen");
								}
							}
						}

					} else if(args[0].equalsIgnoreCase("stop")) {
						if(gameManager.getCurrentGameState() == GameState.INGAME_STATE) {
							IngameState.stop(Team.UNSET);
							player.sendMessage(Main.PREFIX + "§7Du hast das Spiel beendet");
						} else {
							player.sendMessage(Main.PREFIX + "§7Es läuft noch gar kein Spiel");
						}
						
					} else if(args[0].equalsIgnoreCase("corelist")) {
						player.sendMessage(Main.PREFIX + "§7Core Liste:");
						for(Core core : gameManager.getCores()) {		
							player.sendMessage("§8> " + core.getTeam().getColorCode() + core.getNumber() + " §8(§7Name: §6" + core.getName() + "§8)");
						}
					} else if(args[0].equalsIgnoreCase("gamestate")) {
						player.sendMessage(Main.PREFIX + "§7" + gameManager.getCurrentGameState().getName());
										
					} else if(args[0].equalsIgnoreCase("setdeathhight")) {
						setConfigLocation("deathhight", player.getLocation());
						player.sendMessage(Main.PREFIX + "§7Du hast die DeathHight auf §6" + Math.round(getConfigLocation("deathhight", player.getWorld()).getY()) + " §7gesetzt");
					} else if(args[0].equalsIgnoreCase("edit")) {
						if(player.hasPermission("cores.admin")) {
							if(playerProfile.isEditMode()) {
								playerProfile.setEditMode(false);
								player.sendMessage(Main.PREFIX + "§7Der Edit Mode wurde §cdeaktiviert");
							} else {
								playerProfile.setEditMode(true);
								player.sendMessage(Main.PREFIX + "§7Der Edit Mode wurde §aaktiviert");
							}
						}
					} else if(args[0].equalsIgnoreCase("help")) {
						player.sendMessage("§7╔ "+ Main.PREFIX + " §8- §6Hilfe");
						player.sendMessage("§7╠ §6Lobby§7: §8/§7c setlobby");
						player.sendMessage("§7╠ §6Cores§7: §8/§7c setcore <Team> <Nummer> [<Name>]");
						player.sendMessage("§7╠ §6Spawns§7: §8/§7c setspawn <Team>");
						player.sendMessage("§7╠ §6Deathhight§7: §8/§7c setDeathHight");
						player.sendMessage("§7╚ §6Map-Auswahl§7: §8/§7c setmap <Map>");
					}
				} else {
					player.sendMessage(Main.PERMISSION_DENIED);
				}
			} else if(args.length == 2) {
				if(player.hasPermission("cores.admin")) {
					if(args[0].equalsIgnoreCase("configure")) {				
						//World world = Main.getPlugin().getWorld(args[1]);
						if(Bukkit.getWorlds().contains(Bukkit.getWorld(args[1]))) {
							World world = Bukkit.getWorld(args[1]);
							player.teleport(world.getSpawnLocation());
							FileConfiguration config = Main.getPlugin().getConfig();
							config.set(Main.CONFIG_ROOT + "worlds." + args[1], null);
							Main.getPlugin().saveConfig();
							player.sendMessage(Main.PREFIX + "§7Du hast die Welt §2" + args[1] + "§7erstellt");
						}
					} else if(args[0].equalsIgnoreCase("setspawn")) {
						if(args[1].equalsIgnoreCase("blue")) {
							setConfigLocation("blue.spawn", player.getLocation());
							player.sendMessage(Main.PREFIX + "§7Du hast den Spawn von Team " + Team.BLUE.getDisplayColor() + " §7gesetzt");
						} else if(args[1].equalsIgnoreCase("red")) {
							setConfigLocation("red.spawn", player.getLocation());
							player.sendMessage(Main.PREFIX + "§7Du hast den Spawn von Team " + Team.RED.getDisplayColor() + " §7gesetzt");
						}
						
					} else if(args[0].equalsIgnoreCase("setmap")) {
						//Main.getPlugin().setMap(args[1]);
						if(Bukkit.getWorld(args[1]) != null) {
							if(gameManager.checkMap(Bukkit.getWorld(args[1]))) {
								gameManager.setMap(args[1]);
								Bukkit.broadcastMessage(Main.PREFIX + "§7Die Map: §6" + args[1] + "§7 wurde von " + player.getName() + " ausgewählt");
							}
								
						} else {
							player.sendMessage(Main.PREFIX + "§7Die Map: §6" + args[1] + " §7existiert §cnicht");
						}
						
					} else if(args[0].equalsIgnoreCase("deletemap")) {
						
					}
				} else {
					player.sendMessage(Main.PERMISSION_DENIED);
				}
				if(args[0].equalsIgnoreCase("join")) {
					if(args[1].equalsIgnoreCase("blue") && !playerProfile.getTeam().equals(Team.BLUE)) {
						playerProfile.setTeam(Team.BLUE);
						Main.getPlugin().getGameManager().getPlayerProfile(player).getPlayer().sendMessage(Main.PREFIX + "§7Du bist dem Team " + Team.BLUE.getDisplayColor() + " §7beigetreten");
					} else if(args[1].equalsIgnoreCase("red") && !playerProfile.getTeam().equals(Team.RED)) {
						playerProfile.setTeam(Team.RED);
						player.sendMessage(Main.PREFIX + "§7Du bist dem Team " + Team.RED.getDisplayColor() + " §7beigetreten");
					} else {
						
					}
				
				}

				
			} else if(args.length == 4) {
				if(player.hasPermission("cores.admin")) {
					if(args[0].equalsIgnoreCase("setcore")) {
						if(args[1].equalsIgnoreCase("blue")) {
							new SetCoreCommand(player, Team.BLUE, args[2], args[3]);
							
						} else if(args[1].equalsIgnoreCase("red")) {
							new SetCoreCommand(player, Team.RED, args[2], args[3]);
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
		return getConfigLocation(root, world, false);
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
		if(location == null)
			return null;
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
