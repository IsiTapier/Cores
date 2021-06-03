package de.gigaz.cores.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.CountdownTimer;
import de.gigaz.cores.util.GameState;

public class LobbyState {
	
	private static final boolean uselast = false;

	private static boolean countdownActive = false;
	
	private static CountdownTimer countdownLong;
	private static CountdownTimer countdownShort;
	
	//voting
	private static HashMap<Player, World> voted = new HashMap<Player, World>();
	private static HashMap<World, Integer> votes = new HashMap<World, Integer>();
	
	public LobbyState() {
		
	}
	
	
	public static void start() {
		GameManager gameManager = Main.getPlugin().getGameManager();
		gameManager.setGameState(GameState.LOBBY_STATE);
		gameManager.resetMap();
		startTimer();
	}
	
	public static void stop() {
		if(countdownLong != null)
			countdownLong.stopTimer();
		setMap();
		clearVotes();
		countdownActive = true;
		startCountdown();
	}
	
	public static void earlyStop() {
		if(countdownShort != null)
			countdownShort.stopTimer();
		Bukkit.broadcastMessage(ChatColor.YELLOW + "Das Spiel hat begonnen!");
		countdownActive = false;
		IngameState.start();
	}
	
	private static void startTimer() {
		countdownLong = new CountdownTimer(Main.getPlugin(), 5*60, 0, 60,
				//before timer
		        () -> {},
		        //after timer
		        () -> { Bukkit.broadcastMessage(ChatColor.YELLOW + "Das Spiel startet bald!"); stop();},
		        //while timer
		        (t) -> { Bukkit.broadcastMessage(ChatColor.YELLOW + "Das Spiel startet in " + (t.getSecondsLeft()/60) + " Minuten!");}
		);
		countdownLong.scheduleTimer();
	}
	
	private static void startCountdown() {
		countdownActive = true;
		countdownShort = new CountdownTimer(Main.getPlugin(), 10, 10, 1,
				//before timer
		        () -> { Bukkit.broadcastMessage(ChatColor.YELLOW + "Das Spiel startet in §c20§e Sekunden!");},
		        //after timer
		        () -> { Bukkit.broadcastMessage(ChatColor.YELLOW + "Das Spiel hat begonnen!"); countdownActive = false; IngameState.start();},
		        //while timer
		        (t) -> { Bukkit.broadcastMessage(ChatColor.YELLOW + "Das Spiel startet in " + ChatColor.RED + t.getSecondsLeft() + ChatColor.YELLOW + " Sekunden!");}
		);
		countdownShort.scheduleTimer();
	}
	
	private static void setMap() {
		//check if map already set
		GameManager gameManager = Main.getPlugin().getGameManager();
		if(gameManager.getMap() != null && Main.getPlugin().worldValid(gameManager.getMap().getName()) != null)
			return;
		//set by voting
		World world = endVoting();
		if(world == null) {
			//set by old
			if(uselast && gameManager.getLastMap() != null && Main.getPlugin().worldValid(gameManager.getLastMap().getName()) != null)
				world = gameManager.getLastMap();
			else {
				//set by random
				//prevent errors
				FileConfiguration config = Main.getPlugin().getConfig();
				if(!config.contains(Main.CONFIG_ROOT+"worlds")) {
					Bukkit.broadcastMessage(Main.PREFIX+"§cERROR:§r no config path found");
					return;
				}
				if(!(config.getConfigurationSection(Main.CONFIG_ROOT+"worlds").getValues(false).size() > 0)) {
					Bukkit.broadcastMessage(Main.PREFIX+"§cERROR:§r no config Worlds");
					return;
				}
				//get valid maps
				ArrayList<World> worlds = new ArrayList<World>();
				for(String worldname : config.getConfigurationSection(Main.CONFIG_ROOT+"worlds").getValues(false).keySet())
					if(Main.getPlugin().worldValid(worldname) != null)
						worlds.add(Main.getPlugin().getWorld(worldname));
				if(worlds.size() == 0) {
					Bukkit.broadcastMessage(Main.PREFIX+"§cERROR:§r no valid World found");
					return;
				}
				//get random
				Random random = new Random();
				world = worlds.get(random.nextInt(worlds.size()));
			}
		} else
			printVotes(world);
		Bukkit.broadcastMessage(world.getName());
		gameManager.setMap(world);
	}
	
	public static boolean voteMap(Player player, World world) {
		//check if Player has allready voted
		if(voted.containsKey(player) && voted.get(player).equals(world))
			return false;
		if(voted.containsKey(player))
			votes.put(voted.get(player), votes.get(voted.get(player))-1);
		voted.put(player, world);
		//actual vote
		if(!votes.containsKey(world))
			votes.put(world, 1);
		else
			votes.put(world, votes.get(world)+1);
		//printVotes();
		return true;
	}
	
	private static World endVoting() {
		ArrayList<Entry<World, Integer>> results = new ArrayList<Entry<World, Integer>>(votes.entrySet());
		
		for(int i = 1; i < results.size(); i++) {
			if(results.get(i).getValue() < results.get(i-1).getValue()) {
				results.remove(i);
				i--;
			} else if(results.get(i).getValue() > results.get(i-1).getValue()) {
				for(int x = i-1; x >= 0; x--)
					results.remove(x);
				i = 0;
			}
		}
		Random random = new Random();
		if(results.size() == 0)
			return null;
		return results.get(random.nextInt(results.size())).getKey();
	}
	
	private static void clearVotes() {
		voted.clear();
		votes.clear();
	}
	
	private static void printVotes(World world) {
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("<<<<<<<<<<>>>>>>>>>>");
		Bukkit.broadcastMessage(Main.PREFIX+"§6Votes:");
		for(Entry<Player, World> entry : voted.entrySet())
			Bukkit.broadcastMessage(entry.getKey().getName() + ": " + entry.getValue().getName());
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(Main.PREFIX+"§6Results:");
		for(Entry<World, Integer> entry : votes.entrySet())
			Bukkit.broadcastMessage(entry.getKey().getName() + ": " + entry.getValue().toString());
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(Main.PREFIX+"§6Winner: §b"+world.getName());
		Bukkit.broadcastMessage("<<<<<<<<<<>>>>>>>>>>");
		Bukkit.broadcastMessage("");
	}
	
	public static boolean isStarting() {
		return countdownActive;
	}
	
}
