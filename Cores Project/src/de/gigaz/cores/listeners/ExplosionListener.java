package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
	public void onEntityExplodeEvent(EntityExplodeEvent event) {
		if(Main.getPlugin().getGameManager().getCurrentGameState().equals(GameState.INGAME_STATE)) {
			for(Block block : event.blockList())
				if(Main.getPlugin().getGameManager().getCore(block.getLocation())!=null) {
					block.setType(Material.AIR);
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
						@Override
						public void run() {
							block.setType(Material.BEACON);
						}}, 1L);
				}
		} else {
			event.setCancelled(true);
		}
	}
}
