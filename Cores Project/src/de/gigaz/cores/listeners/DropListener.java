package de.gigaz.cores.listeners;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;

public class DropListener implements Listener {
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if(Main.getPlugin().getGameManager().getCurrentGameState() == GameState.INGAME_STATE && event.getPlayer().getWorld().equals(Main.getPlugin().getWorld("currentworld"))) {
			if(/*Main.getPlugin().getGameManager().getMap() != */!event.getPlayer().getWorld().equals(Main.getPlugin().getWorld("currentworld"))) {
				event.setCancelled(true);
			}
		} else if(!Main.getPlugin().getGameManager().getPlayerProfile(event.getPlayer()).isEditMode())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onPickup(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		if(player.getWorld().equals(Main.getPlugin().getGameManager().getLobbySpawn().getWorld())) {
			event.setCancelled(true);
			event.getItem().remove();
		}
	}
}
