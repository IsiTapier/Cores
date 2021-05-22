package de.gigaz.cores.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GUIs;

public class InventoryClickListener implements Listener {
	
	@EventHandler
	public void onInventoryInteract(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		Player player = (Player) event.getWhoClicked();
		ItemStack item = event.getCurrentItem();
		if(inv.getType().getDefaultTitle() == GUIs.getAdminToolInventory().getType().getDefaultTitle()) {
			if(item.getType() == GUIs.getAdminEditMode().getType()) {
				player.sendMessage(Main.PREFIX + "§7edit mode switched");	
			}
			if(item.getType() == GUIs.getAdminEditTeams().getType()) {
				player.sendMessage(Main.PREFIX + "§7edit teams");	
			}
			if(item.getType() == GUIs.getAdminSelectMap().getType()) {
				player.closeInventory();
				player.openInventory(GUIs.getMapSelectInventory());
			}
			if(item.getType() == GUIs.getAdminStartGame().getType()) {
				player.chat("/c start");
			}
			
			player.closeInventory();
			
		} else {
			return;
		}
		event.setCancelled(true);
	}

}
