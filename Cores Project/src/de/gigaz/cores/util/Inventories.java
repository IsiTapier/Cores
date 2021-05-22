package de.gigaz.cores.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import de.gigaz.cores.classes.PlayerProfile;

public class Inventories {
	
	private static ItemBuilder multiTool = new ItemBuilder(Material.BEACON).setName("§bTeam Selection");
	private static ItemBuilder adminTool = new ItemBuilder(Material.REDSTONE_TORCH).setName("§cAdmin Tool");
	private static ItemBuilder teamRedSelector = new ItemBuilder(Material.RED_CONCRETE).setName("§7Team " + Team.RED.getDisplayColor());
	private static ItemBuilder teamBlueSelector = new ItemBuilder(Material.BLUE_CONCRETE).setName("§7Team "+ Team.BLUE.getDisplayColor());
	
	public static void setIngameIventory(PlayerProfile playerProfile) {
		Player player = playerProfile.getPlayer();
		Inventory inventory = player.getInventory();
		inventory.clear();
		inventory.setItem(0, new ItemBuilder(Material.STONE_SWORD).setBreakable(false).build());
		inventory.setItem(1, new ItemBuilder(Material.BOW).setBreakable(false).build());
		inventory.setItem(2, new ItemBuilder(Material.IRON_AXE).setBreakable(false).build());
		//inventory.setItem(5, new ItemBuilder(Material.BEEF).setAmount(64).build());
		inventory.setItem(6, new ItemBuilder(Material.GOLDEN_APPLE).setAmount(16).build());
		inventory.setItem(7, new ItemBuilder(Material.ARROW).setAmount(12).build());
		inventory.setItem(8, new ItemBuilder(Material.IRON_PICKAXE).setBreakable(false).build());
		if(playerProfile.getTeam().equals(Team.BLUE)) {
			inventory.setItem(3, new ItemBuilder(Material.WARPED_STEM).setAmount(64).build());
			inventory.setItem(4, new ItemBuilder(Material.WARPED_PLANKS).setAmount(64).build());
			inventory.setItem(5, new ItemBuilder(Material.WARPED_PLANKS).setAmount(64).build());
			player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setBreakable(false).build());
			player.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setBreakable(false).build());
			player.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setBreakable(false).build());
			player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setBreakable(false).build());
		} else if(playerProfile.getTeam().equals(Team.RED)) {
			inventory.setItem(3, new ItemBuilder(Material.CRIMSON_STEM).setAmount(64).build());
			inventory.setItem(4, new ItemBuilder(Material.CRIMSON_PLANKS).setAmount(64).build());
			inventory.setItem(4, new ItemBuilder(Material.CRIMSON_PLANKS).setAmount(64).build());
			player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setBreakable(false).build());
			player.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setBreakable(false).build());
			player.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).setBreakable(false).build());
			player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).setBreakable(false).build());
		}
	}
	
	public static void setLobbyInventory(PlayerProfile playerProfile) {
		Player player = playerProfile.getPlayer();
		Inventory inventory = player.getInventory();
		inventory.clear();
		//teamBlueSelector.getCopy().addEnchantment(Enchantment.ARROW_INFINITE, 10).build()
		if(playerProfile.getTeam().equals(Team.BLUE)) {
			inventory.setItem(3, new ItemBuilder(teamBlueSelector.getType()).setName(teamBlueSelector.getName()).addEnchantment(Enchantment.ARROW_INFINITE, 10).build());
			inventory.setItem(5, teamRedSelector.build());
		} else if(playerProfile.getTeam().equals(Team.RED)) {
			inventory.setItem(3, teamBlueSelector.build());
			inventory.setItem(5, new ItemBuilder(teamRedSelector.getType()).setName(teamBlueSelector.getName()).addEnchantment(Enchantment.ARROW_INFINITE, 10).build());
		} else {
			inventory.setItem(3, teamBlueSelector.build());
			inventory.setItem(5, teamRedSelector.build());
		}
		inventory.setItem(0, new ItemBuilder(Material.PAPER).setName("§7Map-Voting").build());
		inventory.setItem(4, multiTool.build());
		if(player.isOp()) {
			inventory.setItem(8, adminTool.build());
			/*inventory.setItem(1, new ItemBuilder(Material.MAP).setName("§7Select Map").build());
			inventory.setItem(6, new ItemBuilder(Material.CLOCK).setName("start Game").build());
			inventory.setItem(7, new ItemBuilder(Material.WHITE_CONCRETE).setName("change Teams").build());
			inventory.setItem(8, new ItemBuilder(Material.ARROW).setName("Edit Mode").build());*/
		}
	}

	public static ItemBuilder getMultiTool() {
		return multiTool;
	}

	public static ItemBuilder getAdminTool() {
		return adminTool;
	}

	public static ItemBuilder getTeamRedSelector() {
		return teamRedSelector;
	}

	public static ItemBuilder getTeamBlueSelector() {
		return teamBlueSelector;
	}
	
	
}
