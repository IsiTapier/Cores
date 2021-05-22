package de.gigaz.cores.classes;

import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;

public class LobbyState {

	private int countdown;
	
	public LobbyState(int countdown) {
		this.countdown = countdown;
	}
	
	
	public static void start() {
		Main.getPlugin().getGameManager().setGameState(GameState.LOBBY_STATE);
	}
	
	public static void stop() {
		IngameState.start();
	}
	
}
