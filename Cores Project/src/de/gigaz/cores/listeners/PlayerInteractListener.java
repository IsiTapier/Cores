package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GUIs;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Inventories;


public class PlayerInteractListener implements Listener {
	@EventHandler
	public void onInventoryInteract(PlayerInteractEvent event) {

		Player player = (Player) event.getPlayer();
		GameManager gameManager = Main.getPlugin().getGameManager();
		PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
		Block block = event.getClickedBlock();
		
		if(Main.getPlugin().getGameManager().getCurrentGameState() == GameState.INGAME_STATE) {
			if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				if(block.getType().equals(Material.BEACON))
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 15*20, 0));
			}
		} else {

	
			if(player.getItemInHand().getType() == Inventories.getMultiTool().build().getType()) {
				player.openInventory(GUIs.getMultiToolInventory(player));		;
			}
			if(player.getItemInHand().getType() == Inventories.getAdminTool().build().getType()) {
				player.openInventory(GUIs.getAdminToolInventory());		
			}
			if(player.getItemInHand().getType() == Inventories.getTeamRedSelector().build().getType()) {
				player.chat("/c join red");	
			}
			if(player.getItemInHand().getType() == Inventories.getTeamBlueSelector().build().getType()) {
				player.chat("/c join blue");	
			}
			if(!playerProfile.isEditMode())
				event.setCancelled(true);
		
		}
	}
}
