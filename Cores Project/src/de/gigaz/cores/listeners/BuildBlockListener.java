package de.gigaz.cores.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;

public class BuildBlockListener implements Listener {
	
	@EventHandler
	public void onBuild(BlockPlaceEvent event) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		Block block = event.getBlock();
		Location location = block.getLocation();
		World world = location.getWorld();
		Player player = event.getPlayer();
		PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
		
		if(gameManager.getCurrentGameState() != GameState.INGAME_STATE || !world.equals(gameManager.getCopiedWorld())) {
			//BauProtection via Edit Mode
			if(!playerProfile.isEditMode()) {
				event.setCancelled(true);
				player.sendMessage(Main.PREFIX + "§7Du bist §cnicht§7 berechtigt einen Block zu bauen");
			} 
		} else {
			if(!playerProfile.isEditMode()) {
				if((gameManager.checkCoreProtection(location) && gameManager.getGameruleSetting(gameManager.coreProtectionGamerule).getValue()) || (gameManager.checkSpawnProtection(location) && gameManager.getGameruleSetting(gameManager.spawnProtectionGamerule).getValue())) {
					event.setCancelled(true);
					player.sendMessage(Main.PREFIX + "§7Du darfst hier §ckeine §7Blöcke bauen");
				}
			} else {
				player.sendMessage("§8[§7Hinweis§8] §7Du bearbeitest gerade die Map: §6" + world.getName());
			}
		}
		

	}
}
