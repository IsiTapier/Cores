package de.gigaz.cores.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryAnvil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import de.gigaz.cores.classes.Core;
import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.IngameState;
import de.gigaz.cores.classes.LobbyState;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.AnvilGUI;
import de.gigaz.cores.util.AnvilGUI.AnvilClickEvent;
import de.gigaz.cores.util.AnvilGUI.AnvilSlot;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.ItemBuilder;
import de.gigaz.cores.util.Team;


public class MainCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			GameManager gameManager = Main.getPlugin().getGameManager();
			PlayerProfile pProfile = gameManager.getPlayerProfile(player);
			
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

					} else if(args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("end")) {
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
							if(pProfile.isEditMode()) {
								pProfile.setEditMode(false);
								player.sendMessage(Main.PREFIX + "§7Der Edit Mode wurde §cdeaktiviert");
							} else {
								pProfile.setEditMode(true);
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
							World world = Main.getPlugin().getWorld(args[1]);
							player.teleport(world.getSpawnLocation());
							String root = Main.CONFIG_ROOT+"worlds."+world.getName();
							FileConfiguration config = Main.getPlugin().getConfig();
							if(!config.contains(root)) {
								config.set(root, "");
								Main.getPlugin().saveConfig();
							}
							player.sendMessage(Main.PREFIX + "§7Du hast die Welt §2" + args[1] + " §7erstellt");
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
							if(gameManager.checkMap(Main.getPlugin().getWorld(args[1]), true)) {
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
					if(args[1].equalsIgnoreCase("blue") && !pProfile.getTeam().equals(Team.BLUE)) {
						pProfile.setTeam(Team.BLUE);
						Main.getPlugin().getGameManager().getPlayerProfile(player).getPlayer().sendMessage(Main.PREFIX + "§7Du bist dem Team " + Team.BLUE.getDisplayColor() + " §7beigetreten");
					} else if(args[1].equalsIgnoreCase("red") && !pProfile.getTeam().equals(Team.RED)) {
						pProfile.setTeam(Team.RED);
						player.sendMessage(Main.PREFIX + "§7Du bist dem Team " + Team.RED.getDisplayColor() + " §7beigetreten");
					} else {
						
					}
				
				}

				
			} else if(args.length >= 3) {
				
				if(player.hasPermission("cores.admin")) {
					if(args[0].equalsIgnoreCase("remove")) {
						FileConfiguration config = Main.getPlugin().getConfig();
						World world = Bukkit.getWorld(args[1]);
						if(world == null) {
							player.sendMessage(Main.PREFIX+"Diese Welt existiert nicht");
							return false;
						}
						if(!config.contains(Main.CONFIG_ROOT+"worlds."+world.getName())) {
							player.sendMessage(Main.PREFIX+"Diese Welt ist nicht in der Config gespeichert");
							return false;
						}
						if(args[2].equalsIgnoreCase("deathhight")) {
							if(!containsConfigLocation("deathhight", world)) {
								player.sendMessage(Main.PREFIX+"Die Deathhight ist nicht in der Config gespeichert");
								return false;
							}
							removeConfigLocation("deathhight", world);
							player.sendMessage(Main.PREFIX+"Du hast erfolgreich die Deathhight von der Map §6"+world.getName()+"§r entfernt");
							return false;
						}
						if(args[2].equalsIgnoreCase("map")) {
							config.set(Main.CONFIG_ROOT+"worlds."+world.getName(), null);
							Main.getPlugin().saveConfig();
							player.sendMessage(Main.PREFIX+"Du hast erfolgreich die Map §6"+world.getName()+"§r entfernt");
							return false;
						}
						if(args[2].equalsIgnoreCase("spawn")) {
							if(args.length < 4) {
								player.sendMessage(Main.PREFIX+"Bitte wähle ein Team aus");
								return false;
							}
							Team team = Team.getTeam(args[3]);
							if(team.equals(Team.UNSET)) {
								player.sendMessage(Main.PREFIX+"Bitte wähle ein gültiges Team aus");
								return false;
							}
							if(!containsConfigLocation(team.getDebugColor()+".spawn", world)) {
								player.sendMessage(Main.PREFIX+"Der Spawn von Team "+team.getDisplayColor()+" ist nicht in der Config gespeichert");
								return false;
							}
							removeConfigLocation(team.getDebugColor()+".spawn", world);
							player.sendMessage(Main.PREFIX+"Du hast erfolgreich den Spawn von Team "+team.getDisplayColor()+" auf der Map §6"+world.getName()+"§r entfernt");
							return false;
						}
						if(args[2].equalsIgnoreCase("core")) {
							if(args.length < 4) {
								player.sendMessage(Main.PREFIX+"Bitte wähle ein Team aus");
								return false;
							}
							Team team = Team.getTeam(args[3]);
							if(team.equals(Team.UNSET)) {
								player.sendMessage(Main.PREFIX+"Bitte wähle ein gültiges Team aus");
								return false;
							}
							if(args.length < 5) {
								player.sendMessage(Main.PREFIX+"Bitte wähle einen Core aus");
								return false;
							}
							if(args[4].equalsIgnoreCase("all")) {
								if(!containsConfigLocation(team.getDebugColor()+".core", world)) {
									player.sendMessage(Main.PREFIX+"Die Cores von Team "+team.getDisplayColor()+" sind nicht in der Config gespeichert");
									return false;
								}
								removeConfigLocation(team.getDebugColor()+".core", world);
								player.sendMessage(Main.PREFIX+"Du hast erfolgreich alle Cores von Team "+team.getDisplayColor()+" auf der Map §6"+world.getName()+"§r entfernt");
								return false;
							}
							if(!containsConfigLocation(team.getDebugColor()+".core."+args[4], world)) {
								player.sendMessage(Main.PREFIX+"Der Core §b"+args[4]+"§r von Team "+team.getDisplayColor()+" ist nicht in der Config gespeichert, gebe eine vorhandene Nummer an");
								return false;
							}
							removeConfigLocation(team.getDebugColor()+".core."+args[4], world);
							player.sendMessage(Main.PREFIX+"Du hast erfolgreich den Core §b"+args[4]+"§r von Team "+team.getDisplayColor()+" auf der Map §6"+world.getName()+"§r entfernt");
							return false;
						}
						if(args[2].equalsIgnoreCase("team")) {
							if(args.length < 4) {
								player.sendMessage(Main.PREFIX+"Bitte wähle ein Team aus");
								return false;
							}
							Team team = Team.getTeam(args[3]);
							if(team.equals(Team.UNSET)) {
								player.sendMessage(Main.PREFIX+"Bitte wähle ein gültiges Team aus");
								return false;
							}
							if(!containsConfigLocation(team.getDebugColor(), world)) {
								player.sendMessage(Main.PREFIX+"Das Team "+team.getDisplayColor()+" ist nicht in der Config gespeichert");
								return false;
							}
							removeConfigLocation(team.getDebugColor(), world);
							player.sendMessage(Main.PREFIX+"Du hast erfolgreich das Team "+team.getDisplayColor()+" von der Map §6"+world.getName()+"§r entfernt");
							return false;
						}
						player.sendMessage(Main.PREFIX+"Bitte gebe eine gültige Config Speicherung an: §7(map, team, spawn, core, deathhight)");
						return false;
						
						
						
					} else if(args[0].equalsIgnoreCase("setcorename")) {
						Team team = Team.getTeam(args[1]);
						if(team.equals(Team.UNSET)) {
							player.sendMessage(Main.PREFIX+"Bitte wähle ein gültiges Team aus");
							return false;
						}
						if(!containsConfigLocation(team.getDebugColor()+".core."+args[2], gameManager.getMap())) {
							player.sendMessage(Main.PREFIX+"Der Core §b"+args[2]+"§r von Team "+team.getDisplayColor()+" ist nicht in der Config gespeichert, gebe eine vorhandene Nummer an");
							return false;
						}
						if(args.length >= 4) {
							String name = args[3];
							Core.setConfigCoreName(gameManager.getMap().getSpawnLocation(), team, args[2], name);
							player.sendMessage(Main.PREFIX+"Du hast erfolgreich den Core §b"+args[2]+"§r von Team "+team.getDisplayColor()+" zu §6"+name+"§r umbenannt");
						} else {
							AnvilGUI gui = new AnvilGUI(player, "§1enter core name", "textInput", new AnvilGUI.AnvilClickEventHandler() {
				                @Override
				                public void onAnvilClick(AnvilClickEvent event) {
				                    if(event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT) {
				                        event.setWillClose(true);
				                        event.setWillDestroy(true);
				                        player.chat("/c setCoreName "+team.getDebugColor()+" "+args[2]+" "+event.getName());
				                    } else {
				                        event.setWillClose(false);
				                        event.setWillDestroy(false);
				                    }
				                }
				            }).setSlot(AnvilSlot.INPUT_LEFT, new ItemBuilder(Material.BEACON).setName(team.getColorCode()+Core.getConfigCoreName(gameManager.getMap().getSpawnLocation(), team, args[2])).setAmount(Integer.parseInt(args[2])).setLore("click ouput to submit").build());
							
						}
						
						
					}
				}
				
			} 
			if(args.length == 4) {
			
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
	
	public static boolean containsConfigLocation(String root, World world) {
		FileConfiguration config = Main.getPlugin().getConfig();
		return config.contains(Main.CONFIG_ROOT + "worlds." + world.getName() + "." + root);
	}
	
	public static void removeConfigLocation(String root, World world) {
		FileConfiguration config = Main.getPlugin().getConfig();
		config.set(Main.CONFIG_ROOT + "worlds." + world.getName() + "." + root, null);
		Main.getPlugin().saveConfig();
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
		location.setWorld(Main.getPlugin().getWorld("currentworld"));
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
