package de.gigaz.cores.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import de.gigaz.cores.commands.MainCommand;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Team;

public class GameManager {
	
	private HashMap<String, PlayerProfile> playerProfiles = new HashMap<String, PlayerProfile>();;
	private LobbyState gameStates[] = new LobbyState[3];
	private GameState currentGameState;
	private LobbyState lobbyState;
	private IngameState ingameState;
	private EndingState endingState;
	private HashMap<Location, Material> breakedBlocks = new HashMap<Location, Material>();
	private ArrayList<Location> builtBlocks = new ArrayList<Location>();
	
	

	private boolean coreBlue1;
	private boolean coreBlue2;
	private boolean coreRed1;
	private boolean coreRed2;
	
	private int blockProtectionRadius = 2;
	private int blockProtectionHeight = 4;
	
	private World map;
	private ArrayList<Core> cores = new ArrayList<Core>();
	private ArrayList<Core> stockedCores = new ArrayList<Core>();
	
	public GameManager() {
		//new LobbyState();
	}
	
	public void setMap(String name) {
		//TODO check map valid
		//this.map = Main.getPlugin().getWorld(name);
		//TODO copy map
		this.map = Bukkit.getWorld(name);
	}
	
	
	public void checkWin() {
		
		/*if(coreBlue1 == false && coreBlue2 == false)
			endGame(Team.RED);
		else if(coreRed1 == false && coreRed2 == false)
			endGame(Team.BLUE);*/
		if(getCores(Team.BLUE).size() == 0) {
			endGame(Team.BLUE);
		}
		if(getCores(Team.RED).size() == 0) {
			endGame(Team.RED);
		}
	}
	
	public void endGame(Team team) {
		Bukkit.broadcastMessage(Main.PREFIX+" "+team.getDisplayColor()+" won");
		
		if(currentGameState == GameState.INGAME_STATE) {
			IngameState.stop();
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
				//Bukkit.broadcastMessage("�bCore" + x);
				cores.add(new Core(location, Team.BLUE, x + ""));
				valid = true;
			}
		}
		root = Main.CONFIG_ROOT + "worlds." + map.getName() + ".red.core.";
		for(int x = 0; x <= 30; x++) {
			if(config.contains(root + x + ".location")) {
				Location location = config.getLocation(root + x + ".location");
				//Bukkit.broadcastMessage("�cCore" + x);
				cores.add(new Core(location, Team.RED, x + ""));

				valid = true;
			}
		}
		//Bukkit.broadcastMessage(cores.size() + " size");
		if(valid == false) {
			Bukkit.broadcastMessage(Main.PREFIX + "�7Das Spiel besitzt �ckeine�7 vollst�ndig konfigurierte Map");
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
		stockedCores = getCores();
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
		for(Player player : Bukkit.getOnlinePlayers()) {
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

	public HashMap<String, PlayerProfile> getPlayerProfiles() {
		return playerProfiles;
	}

	public void setPlayerProfiles(HashMap<String, PlayerProfile> playerProfiles) {
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

	public World getMap() {
		return map;
	}

	public void setMap(World map) {
		
		this.map = map;
	}
	
	public PlayerProfile getPlayerProfile(Player player) {
		return playerProfiles.get(player.getName());
	}
	
	public void addPlayer(Player player) {
		playerProfiles.put(player.getName(), new PlayerProfile(player));
	}
	
	public void removePlayer(Player player) {
		playerProfiles.remove(player.getName(), new PlayerProfile(player));
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
}
  
