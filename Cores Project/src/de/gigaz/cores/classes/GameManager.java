package de.gigaz.cores.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
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

	
	private World map;
	private boolean coreBlue1 = true;
	private boolean coreBlue2 = true;
	private boolean coreRed1 = true;
	private boolean coreRed2 = true;
	
	public GameManager() {
		//new LobbyState();
	}
	
	public void setMap(String name) {
		//TODO check map valid
		this.map = Main.getPlugin().getWorld(name);
		//TODO copy map
	}
	
	public void setCoreState(Team team, boolean number, boolean value) {
		if(team.equals(Team.BLUE)) {
			if(number)
				coreBlue1 = value;
			else
				coreBlue2 = value;
		} else if(team.equals(Team.RED)) {
			if(number)
				coreRed1 = value;
			else
				coreRed2 = value;
		}
		
		checkWin();
	}
	
	public boolean getCoreState(Team team, boolean number) {
		if(team.equals(Team.BLUE)) {
			if(number)
				return coreBlue1;
			else
				return coreBlue2;
		} else if(team.equals(Team.RED)) {
			if(number)
				return coreRed1;
			else
				return coreRed2;
		}
		return false;
	}
	
	public void checkWin() {
		if(coreBlue1 == false && coreBlue2 == false)
			endGame(Team.RED);
		else if(coreRed1 == false && coreRed2 == false)
			endGame(Team.BLUE);
	}
	
	public void endGame(Team team) {
		Bukkit.broadcastMessage(Main.PREFIX+" "+team.getDisplayColor()+" won");
		
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
	

	public HashMap<String, PlayerProfile> getPlayerProfiles() {
		return playerProfiles;
	}

	public void setPlayerProfiles(HashMap<String, PlayerProfile> playerProfiles) {
		this.playerProfiles = playerProfiles;
	}

	public GameState getCurrentGameState() {
		return currentGameState;
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
  
