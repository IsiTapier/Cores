package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import de.gigaz.cores.util.GUIs;
import de.gigaz.cores.util.Inventories;


public class PlayerInteractListener implements Listener {
	@EventHandler
	public void onInventoryInteract(PlayerInteractEvent event) {

		Player player = (Player) event.getPlayer();
		
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
	}
}
