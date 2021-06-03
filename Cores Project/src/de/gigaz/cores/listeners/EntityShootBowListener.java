package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.ItemBuilder;

public class EntityShootBowListener implements Listener {
	@EventHandler
	public void onEntityShootBowEvent(EntityShootBowEvent event) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(gameManager.getCurrentGameState() != GameState.INGAME_STATE || !player.getWorld().equals(Main.getPlugin().getWorld("currentworld")))
				event.setCancelled(true);
			if(gameManager.getGameruleSetting(gameManager.infiniteArrowsGamerule).getValue()) {
				player.getInventory().setItem(player.getInventory().first(Material.ARROW), new ItemBuilder(Material.ARROW).setName("§6Infinity Arrow").addEnchantment(Enchantment.ARROW_INFINITE, 10).setAmount(gameManager.getGameruleSetting(gameManager.superCrossbowGamerule).getValue()||gameManager.getGameruleSetting(gameManager.crossbowGamerule).getValue()?2:1).setBreakable(false).build());
				Arrow arrow = (Arrow) event.getProjectile();
				arrow.setPickupStatus(PickupStatus.CREATIVE_ONLY);
			}
		}
	}
}