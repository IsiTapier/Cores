package de.gigaz.cores.util;

public enum GameState {
	LOBBY_STATE(1), INGAME_STATE(2), ENDING_STATE(3);
	
	private int number;

	GameState(int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}
}
