package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
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
		GameManager gameManager = Main.getPlugin().getGameManager();
		if(Main.getPlugin().getGameManager().getCurrentGameState().equals(GameState.INGAME_STATE)) {
			for(Block block : event.blockList())
				if(gameManager.checkProtection(block)) {
					Material type = block.getType();
					BlockData data = block.getBlockData();
					block.setType(Material.BEDROCK);
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
						@Override
						public void run() {
							block.setType(type);
							block.setBlockData(data);
						}}, 1L);
				}
		} else {
			event.setCancelled(true);
		}
	}
}
