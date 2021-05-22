package de.gigaz.cores.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.main.Main;

public class BuildBlockListener implements Listener {
	
	@EventHandler
	public void onBuild(BlockPlaceEvent event) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		if(!gameManager.getBuiltBlocks().contains(event.getBlock()))
			gameManager.getBuiltBlocks().add(event.getBlock());	
	}
}
