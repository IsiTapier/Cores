package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;

public class ExplosionListener implements Listener {
	
	@EventHandler
	public void onBlockExplodeEvent(BlockExplodeEvent event) {
	}
	
	@EventHandler
	public void onBlockExplodeEvent(EntityExplodeEvent event) {
	
		if(event.getEntity().getType().equals(EntityType.WITHER_SKULL) && Main.getPlugin().getGameManager().getCurrentGameState().equals(GameState.INGAME_STATE) && Main.getPlugin().getGameManager().getCore(event.getLocation(), 3) != null)
			event.setCancelled(true);
		}
}
