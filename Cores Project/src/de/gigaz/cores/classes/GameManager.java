package de.gigaz.cores.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.inventories.AdminToolInventory;
import de.gigaz.cores.inventories.MultiToolInventory;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.ItemBuilder;
import de.gigaz.cores.util.Team;

public class GameManager {
	
	private ArrayList<PlayerProfile> playerProfiles = new ArrayList<PlayerProfile>();;
	private LobbyState gameStates[] = new LobbyState[3];
	private GameState currentGameState = GameState.LOBBY_STATE;
	private LobbyState lobbyState;
	private IngameState ingameState;
	private EndingState endingState;
	private HashMap<Location, Material> breakedBlocks = new HashMap<Location, Material>();
	private ArrayList<Location> builtBlocks = new ArrayList<Location>();					
	private HashMap<String, GameruleSetting> gameruleSettings = new HashMap<String, GameruleSetting>() {{
		put("test", new GameruleSetting(new ItemBuilder(Material.TNT).setName("test").build()));
		put("test1", new GameruleSetting(new ItemBuilder(Material.TNT).setName("test1").build()));
		put("test2", new GameruleSetting(new ItemBuilder(Material.TNT).setName("test2").build()));
		put("test3", new GameruleSetting(new ItemBuilder(Material.TNT).setName("test3").build()));
		put("test4", new GameruleSetting(new ItemBuilder(Material.TNT).setName("test4").build()));
	}};
	
	private int blockProtectionRadius = 2;
	private int blockProtectionHeight = 4;
	
	private World map;
	private World lastMap;
	private World configureMap;
	private ArrayList<Core> cores = new ArrayList<Core>();
	private ArrayList<Core> stockedCores = new ArrayList<Core>();
	
	public GameManager() {
		//new LobbyState();
		FileConfiguration config = Main.getPlugin().getConfig();
		if(config.contains(Main.CONFIG_ROOT + "currentMap")) {
			String name = config.getString(Main.CONFIG_ROOT + "currentMap");
			//Main.getPlugin().saveConfig();
			if(name == null)
				return;
			lastMap = Main.getPlugin().getWorld(name);
		}
	}

	public void setMap(World map) {
		this.map = map;
		lastMap = map;
		
		if(currentGameState.equals(GameState.INGAME_STATE))
			IngameState.stop(Team.UNSET);
		for(Player player : Main.getPlugin().getWorld("currentworld").getPlayers())
			player.teleport(Main.getPlugin().getGameManager().getLobbySpawn());
		Main.getPlugin().setMap(map.getName());
		
		/*FileConfiguration config = Main.getPlugin().getConfig();
		config.set(Main.CONFIG_ROOT + "currentMap", this.map.getName());
		//Main.getPlugin().saveConfig();
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				Main.getPlugin().saveConfig();
			}
		}, 1*20);*/
		
	}
	
	public void setMap(String name) {
		setMap(Main.getPlugin().getWorld(name));
	}
	
	public void setVirtualMap(World world) {
		map = world;
	}
	
	public World getMap() {
		return map;
	}
	
	public World getLastMap() {
		return lastMap;
	}
	
	public World getConfigureMap() {
		return configureMap;
	}
	
	public void setConfigureMap(World world) {
		configureMap = world;
	}
	
	public void resetMap() {
		map = null;
	}
	
	public boolean checkWin() {
		if(getCores(Team.BLUE).size() == 0) {
			endGame(Team.RED);
			return true;
		}
		if(getCores(Team.RED).size() == 0) {
			endGame(Team.BLUE);
			return true;
		}
		return false;
	}
	
	public void endGame(Team team) {
		if(currentGameState == GameState.INGAME_STATE) {
			IngameState.stop(team);
		}
	}
	
	public void registerCores() {
		FileConfiguration config = Main.getPlugin().getConfig();
		String root = Main.CONFIG_ROOT + "worlds." + map.getName() + ".blue.core.";
		String name = null;
		
		cores.clear(); //new
		boolean valid = false;
		for(int x = 0; x <= 30; x++) {
			if(config.contains(root + x + ".location")) {
				Location location = config.getLocation(root + x + ".location");
				location.setWorld(Main.getPlugin().getWorld("currentworld"));
				//Bukkit.broadcastMessage("§bCore" + x);
				cores.add(new Core(location, Team.BLUE, x + ""));
				valid = true;
			}
		}
		root = Main.CONFIG_ROOT + "worlds." + map.getName() + ".red.core.";
		for(int x = 0; x <= 30; x++) {
			if(config.contains(root + x + ".location")) {
				Location location = config.getLocation(root + x + ".location");
				location.setWorld(Main.getPlugin().getWorld("currentworld"));
				//Bukkit.broadcastMessage("§cCore" + x);
				cores.add(new Core(location, Team.RED, x + ""));

				valid = true;
			}
		}
		//Bukkit.broadcastMessage(cores.size() + " size");
		if(valid == false) {
				Bukkit.broadcastMessage(Main.PREFIX + "§7Das Spiel besitzt §ckeine§7 vollständig konfigurierte Map");
		}
		
	}

	
	public Core getCore(Location location) {
		for(Core core : cores) {
			if(core.getLocation().equals(location)) {
				return core;
			}
		}
		return null;
	}
	
	public void stockCores() {
		registerCores();
		stockedCores.clear();
		for(Core core : getCores()) {
			stockedCores.add(core);
		}
	}
	public ArrayList<Core> getStockedCores() {
		return stockedCores;
	}
	
	public ArrayList<Core> getStockedCores(Team team) {
		ArrayList<Core> list = new ArrayList<Core>();
		for(Core core : stockedCores) {
			if(core.getTeam().equals(team)) {
				list.add(core);
			}
		}
		return list;
	}
	
	public ArrayList<Player> getPlayersOfTeam(Team team) {
		ArrayList<Player> players = new ArrayList<Player>();
		for(PlayerProfile playerProfile : getPlayerProfiles()/*Bukkit.getOnlinePlayers()*/) {
			Player player = playerProfile.getPlayer();
			if(Main.getPlugin().getGameManager().getPlayerProfile(player).getTeam() == team) {
				players.add(player);
			}
		}		
		return players;
	}
	public List<Player> getPlayersOfTeamAsList(Team team) {
		List<Player> players = getPlayersOfTeam(team);
		return players;
	}
	
	public boolean checkMap(World world, boolean message) {
		boolean valid = true;
		FileConfiguration config = Main.getPlugin().getConfig();
		String root = Main.CONFIG_ROOT + "worlds." + world.getName() + ".";
		if(!config.contains(root + "blue.spawn")) {
			//Bukkit.broadcastMessage("blue spawn");
			valid = false;
			}
		if(!config.contains(root + "red.spawn")) {
			//Bukkit.broadcastMessage("red spawn");
			valid = false;
			}
		if(!config.contains(root + "red.core")) {
			//Bukkit.broadcastMessage("red core");
			valid = false;
			}
		if(!config.contains(root + "blue.core")) {
			//Bukkit.broadcastMessage("blue core");
			valid = false;
			}
		if(!config.contains(root + "deathhight")) {
			//Bukkit.broadcastMessage("deathhight");
			valid = false;
			}
		if(valid == false) {
			if(message)
				Bukkit.broadcastMessage("§8[§fHinweis§8]§7 Die Map §6" + world.getName() + "§7 ist noch §cnicht§7 vollständig konfiguriert");
		}
		return valid;
	}
	public boolean checkMap(World world) {
		return checkMap(world, false);
	}
	
	public ArrayList<Core> getCores() {
		return cores;
	}
	
	public ArrayList<Core> getCores(Team team) {
		ArrayList<Core> list = new ArrayList<Core>();
		for(Core core : cores) {
			if(core.getTeam().equals(team)) {
				list.add(core);
			}
		}
		return list;
	}

	public ArrayList<PlayerProfile> getPlayerProfiles() {
		return playerProfiles;
	}

	public void setPlayerProfiles(ArrayList<PlayerProfile> playerProfiles) {
		this.playerProfiles = playerProfiles;
	}

	public GameState getCurrentGameState() {
		return currentGameState;
	}

	public boolean checkCoreProtection(Location location) {
		for(Core core : getCores()) {
			Location coreLocation = core.getLocation();
			if(coreLocation.getY() - location.getY() <= -blockProtectionHeight) continue;
			if(coreLocation.getY() - location.getY() >= blockProtectionHeight) continue;
			location = new Location(location.getWorld(), location.getX(), coreLocation.getY(), location.getZ());
			if(coreLocation.distance(location) < blockProtectionRadius) {
				if(location != coreLocation) {
					return true;
				}			
			}		
		}
		return false;
	}

	public void setGameState(GameState gameState) {
		this.currentGameState = gameState;
	}
	
	public PlayerProfile getPlayerProfile(Player player) {
		//return playerProfiles.get(player.getName());
		for(PlayerProfile playerProfile : playerProfiles) {
			if(playerProfile.getPlayer().getName().equals(player.getName()))
				return playerProfile;
		}
		return null;
	}
	
	public void addPlayer(Player player) {
		playerProfiles.add(new PlayerProfile(player));
	}
	
	public void removePlayer(Player player) {
		playerProfiles.remove(playerProfiles.indexOf(getPlayerProfile(player)));
	}
 
	public LobbyState[] getGameStates() {
		return gameStates;
	}

	public void setGameStates(LobbyState[] gameStates) {
		this.gameStates = gameStates;
	}

	public LobbyState getLobbyState() {
		return lobbyState;
	}

	public void setLobbyState(LobbyState lobbyState) {
		this.lobbyState = lobbyState;
	}

	public IngameState getIngameState() {
		return ingameState;
	}

	public void setIngameState(IngameState ingameState) {
		this.ingameState = ingameState;
	}

	public EndingState getEndingState() {
		return endingState;
	}

	public void setEndingState(EndingState endingState) {
		this.endingState = endingState;
	}

	public void setCurrentGameState(GameState currentGameState) {
		this.currentGameState = currentGameState;
	}

	public HashMap<Location, Material> getBreakedBlocks() {
		return breakedBlocks;
	}
	
	public Location getLobbySpawn() {
		Location location = MainCommand.getConfigGeneralLocation("lobbyspawn");
		return location;
	}
	
	public Location getSpawnOfTeam(Team team, World world) {
		return MainCommand.getConfigLocation(team.getDebugColor() + ".spawn", world);
	}

	public void setBreakedBlocks(HashMap<Location, Material> breakedBlocks) {
		this.breakedBlocks = breakedBlocks;
	}

	public ArrayList<Location> getBuiltBlocks() {
		return builtBlocks;
	}

	public void setBuiltBlocks(ArrayList<Location> builtBlocks) {
		this.builtBlocks = builtBlocks;
	}
	
	
	public int getBlockProtectionRadius() {
		return blockProtectionRadius;
	}

	public int getBlockProtectionHeight() {
		return blockProtectionHeight;
	}

	public void playSound(Sound sound, World world, int tone) {
		for(Player player : world.getPlayers()) {
			player.playSound(player.getLocation(), sound, tone, 1);		
		}
	}
	public void playSound(Sound sound, World world, int tone, Team team) {
		for(Player player : world.getPlayers()) {
			Team playerTeam = Main.getPlugin().getGameManager().getPlayerProfile(player).getTeam();
			if(playerTeam.equals(team)) {
				player.playSound(player.getLocation(), sound, tone, 1);
			}				
		}
	}
	public void playSound(Sound sound, World world) {
		playSound(sound, world, 5);
	}
	
	public void autoTeam(PlayerProfile playerProfile) {
		int red = 0;
		int blue = 0;
		for(PlayerProfile player : getPlayerProfiles()) {
			if(player.getPlayer().isOnline() || Main.autoteamcountoffline) {
				if(player.getTeam().equals(Team.RED))
					red++;
				else if(player.getTeam().equals(Team.BLUE))
					blue++;
			}
			
		}
		if(red < blue)
			playerProfile.setTeam(Team.RED);
		else
			playerProfile.setTeam(Team.BLUE);
	}
	
	public void setTeams() {
		ArrayList<PlayerProfile> offlinePlayers = new ArrayList<PlayerProfile>();
		for(PlayerProfile playerProfile : getPlayerProfiles()) {
			Player player = playerProfile.getPlayer();
			if(!player.isOnline()) {
				offlinePlayers.add(playerProfile);
				continue;
			}
			if(playerProfile.getTeam().equals(Team.UNSET))
				autoTeam(playerProfile);
		}
		if(Main.autoteamsetoffline) {
			for(PlayerProfile playerProfile : offlinePlayers) {
				if(playerProfile.getTeam().equals(Team.UNSET))
					autoTeam(playerProfile);
			}
		}
	}
	
	public HashMap<String, GameruleSetting> getGameruleSettings() {
		return gameruleSettings;
	}
	
	public void setGameruleSettings(HashMap<String, GameruleSetting> gameruleSettings) {
		this.gameruleSettings = gameruleSettings;
	}
	
	public GameruleSetting getGameRuleSetting(String name) {
		return gameruleSettings.get(name);
	}
	
	public void setGameRuleSetting(String name, GameruleSetting gameruleSetting) {
		gameruleSettings.put(name, gameruleSetting);
	}
}
  
