package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import de.gigaz.cores.classes.Core;
import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.ScoreboardManager;
import de.gigaz.cores.util.Team;

public class BreakBlockListener implements Listener {

	@EventHandler
	public void onBreakBlock(BlockBreakEvent event) {
		
		GameManager gameManager = Main.getPlugin().getGameManager();
		Player player = event.getPlayer();
		FileConfiguration config = Main.getPlugin().getConfig();
		Team team = gameManager.getPlayerProfile(player).getTeam();
		World world = Main.getPlugin().getWorld("currentworld"); // gameManager.getMap();
		Location location = event.getBlock().getLocation();
		PlayerProfile playerProfile = gameManager.getPlayerProfile(player);

		if(gameManager.getCurrentGameState() == GameState.INGAME_STATE && player.getWorld().equals(Main.getPlugin().getWorld("currentworld"))) {
			if(!gameManager.getBuiltBlocks().contains(location)) {
				if(!playerProfile.isEditMode()) {
					gameManager.getBreakedBlocks().put(location, event.getBlock().getType());
					if(gameManager.checkCoreProtection(location)) {
						event.setCancelled(true);
						player.sendMessage(Main.PREFIX + "§7Du darfst hier §ckeine §7Blöcke abbauen");
					}
					
					
				} else {
					player.sendMessage(Main.PREFIX + "§8[§7Hinweis§8] §7Du bearbeitest gerade die Map: §6" + player.getWorld());
				}
			}
		} else {
			//Abbau Protection via EditMode
			if(!playerProfile.isEditMode()) {
				event.setCancelled(true);
				player.sendMessage(Main.PREFIX + "§7Du bist §cnicht§7 berechtigt einen Block abzubauen");
			}
		}
		

		
		if(event.getBlock().getType().equals(Material.BEACON)) {
			if(!(Main.getPlugin().getGameManager().getCurrentGameState() == GameState.INGAME_STATE))
				return;

			Core core = gameManager.getCore(location);
			if(core != null) {
				event.setCancelled(true);
				if(!core.getTeam().equals(playerProfile.getTeam())) {
					gameManager.getCores().remove(core);
					Bukkit.broadcastMessage(Main.PREFIX + "§7" + player.getName() + " hat den Core §6" + core.getDisplayName() + " §7 von Team " + core.getTeam().getDisplayColor() + " §7abgebaut");
					event.getBlock().setType(Material.BEDROCK);
					gameManager.checkWin();
					//if(!gameManager.checkWin()) {
						gameManager.playSound(Sound.BLOCK_BEACON_DEACTIVATE, world, 8);
					//}
					ScoreboardManager.drawAll();
				} else
					player.sendMessage(Main.PREFIX + "§7Du kannst deinen eigenen Core nicht abbauen");
			}
		}
	}	
}
