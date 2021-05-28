package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GUIs;
import de.gigaz.cores.util.Inventories;

public class InventoryClickListener implements Listener {
	
	@EventHandler
	public void onInventoryInteract(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inv = event.getInventory();
		ItemStack item = event.getCurrentItem();
		player.sendMessage(player.getOpenInventory().getTitle());
		player.sendMessage(GUIs.getAdminSelectMap().getName());
		if(player.getOpenInventory().getTitle().equalsIgnoreCase(Inventories.getAdminTool().getName())) {
			Bukkit.broadcastMessage(GUIs.getAdminToolInventory().getType().getDefaultTitle());
			if(item.getType() == GUIs.getAdminEditMode().getType()) {
				player.chat("/c edit");	
			}
			if(item.getType() == GUIs.getAdminEditTeams().getType()) {
				player.sendMessage(Main.PREFIX + "�7edit teams");	
			}
			if(item.getType() == GUIs.getAdminSelectMap().getType()) {
				player.closeInventory();
				player.sendMessage(player.getOpenInventory().getTitle());
				player.openInventory(GUIs.getMapSelectInventory());
				Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.getPlugin(), new Runnable() {
					
					@Override
					public void run() {
						Bukkit.broadcastMessage("runnable");
						player.sendMessage(player.getOpenInventory().getTitle());
					}
				}, 20);
				
			}
			if(item.getType() == GUIs.getAdminStartGame().getType()) {
				player.chat("/c start");
			}
			event.setCancelled(true);
			player.closeInventory();
			
		}
	
	}

}
