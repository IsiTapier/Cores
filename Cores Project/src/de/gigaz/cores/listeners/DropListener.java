package de.gigaz.cores.listeners;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;

public class DropListener implements Listener {
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if(Main.getPlugin().getGameManager().getCurrentGameState() == GameState.INGAME_STATE) {
			if(Main.getPlugin().getGameManager().getMap() != event.getPlayer().getWorld()) {
				event.setCancelled(true);
			}
		}
			
		if(!Main.getPlugin().getGameManager().getPlayerProfile(event.getPlayer()).isEditMode())
			event.setCancelled(true);
	}
}
