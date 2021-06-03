package de.gigaz.cores.util;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.inventories.MultiToolInventory;

public class Inventories {
	private static ItemBuilder voteMap = new ItemBuilder(Material.PAPER).setName("§7Map-Voting");
	private static ItemBuilder customizeInventory = new ItemBuilder(Material.CHEST).setName("customize inventory");
	private static ItemBuilder multiTool = new ItemBuilder(Material.BEACON).setName("§bTeam Selection");
	private static ItemBuilder adminTool = new ItemBuilder(Material.REDSTONE_TORCH).setName("§cAdmin Tool");
	private static ItemBuilder teamRedSelector = new ItemBuilder(Material.RED_CONCRETE).setName("§7Team " + Team.RED.getDisplayColor()).hideEnchants();
	private static ItemBuilder teamBlueSelector = new ItemBuilder(Material.BLUE_CONCRETE).setName("§7Team "+ Team.BLUE.getDisplayColor()).hideEnchants();
	private static ItemBuilder teamRandomSelector = new ItemBuilder(Material.COMMAND_BLOCK).setName("§7Team§r Random").hideEnchants();
	private static ItemBuilder gameruleSettings = new ItemBuilder(Material.WRITABLE_BOOK).setName("Gamerule Settings");
	public static final String defaultInventoryName = "customize inventory";
	
	public static Inventory getDefaultInventory() {
		Inventory inventory = Bukkit.createInventory(null, 1*9, defaultInventoryName);
		inventory.setItem(0, new ItemBuilder(Material.IRON_SWORD).setBreakable(false).build());
		inventory.setItem(1, new ItemBuilder(Material.BOW).setBreakable(false).build());
		inventory.setItem(2, new ItemBuilder(Material.IRON_AXE).setBreakable(false).build());
		inventory.setItem(3, new ItemBuilder(Material.OAK_LOG).setAmount(64).build());
		inventory.setItem(4, new ItemBuilder(Material.OAK_PLANKS).setAmount(64).build());
		inventory.setItem(5, new ItemBuilder(Material.OAK_PLANKS).setAmount(64).build());
		//inventory.setItem(5, new ItemBuilder(Material.BEEF).setAmount(64).build());
		inventory.setItem(6, new ItemBuilder(Material.GOLDEN_APPLE).setAmount(16).build());
		inventory.setItem(7, new ItemBuilder(Material.ARROW).setAmount(12).build());
		inventory.setItem(8, new ItemBuilder(Material.IRON_PICKAXE).setBreakable(false).build());
		return inventory;
	}
	
	public static void setIngameIventory(PlayerProfile playerProfile) {
		Player player = playerProfile.getPlayer();
		Inventory inventory = player.getInventory();
		inventory.clear();
		inventory.setContents(playerProfile.getInventory().getContents());
		/*inventory.setItem(0, new ItemBuilder(Material.STONE_SWORD).setBreakable(false).build());
		inventory.setItem(1, new ItemBuilder(Material.BOW).setBreakable(false).build());
		inventory.setItem(2, new ItemBuilder(Material.IRON_AXE).setBreakable(false).build());
		//inventory.setItem(5, new ItemBuilder(Material.BEEF).setAmount(64).build());
		inventory.setItem(6, new ItemBuilder(Material.GOLDEN_APPLE).setAmount(16).build());
		inventory.setItem(7, new ItemBuilder(Material.ARROW).setAmount(12).build());
		inventory.setItem(8, new ItemBuilder(Material.IRON_PICKAXE).setBreakable(false).build());*/
		if(playerProfile.getTeam().equals(Team.BLUE)) {
			for(int i = 0; i < 9; i++) {
				if(inventory.getItem(i).getType().equals(Material.OAK_LOG))
					inventory.setItem(i, new ItemBuilder(Material.WARPED_STEM).setAmount(64).build());
				else if(inventory.getItem(i).getType().equals(Material.OAK_PLANKS))
					inventory.setItem(i, new ItemBuilder(Material.WARPED_PLANKS).setAmount(64).build());
			}
			/*inventory.setItem(3, new ItemBuilder(Material.WARPED_STEM).setAmount(64).build());
			inventory.setItem(4, new ItemBuilder(Material.WARPED_PLANKS).setAmount(64).build());
			inventory.setItem(5, new ItemBuilder(Material.WARPED_PLANKS).setAmount(64).build());*/
			Color c = Color.fromRGB(0, 0, 255);
			player.getInventory().setBoots(setColor(new ItemBuilder(Material.LEATHER_BOOTS).setBreakable(false).build(), c));
			player.getInventory().setLeggings(setColor(new ItemBuilder(Material.LEATHER_LEGGINGS).setBreakable(false).build(), c));
			player.getInventory().setChestplate(setColor(new ItemBuilder(Material.LEATHER_CHESTPLATE).setBreakable(false).build(), c));
			player.getInventory().setHelmet(setColor(new ItemBuilder(Material.LEATHER_HELMET).setBreakable(false).build(), c));
		} else if(playerProfile.getTeam().equals(Team.RED)) {
			for(int i = 0; i < 9; i++) {
				if(inventory.getItem(i).getType().equals(Material.OAK_LOG))
					inventory.setItem(i, new ItemBuilder(Material.CRIMSON_STEM).setAmount(64).build());
				else if(inventory.getItem(i).getType().equals(Material.OAK_PLANKS))
					inventory.setItem(i, new ItemBuilder(Material.CRIMSON_PLANKS).setAmount(64).build());
			}
			/*inventory.setItem(3, new ItemBuilder(Material.CRIMSON_STEM).setAmount(64).build());
			inventory.setItem(4, new ItemBuilder(Material.CRIMSON_PLANKS).setAmount(64).build());
			inventory.setItem(5, new ItemBuilder(Material.CRIMSON_PLANKS).setAmount(64).build());*/
			Color c = Color.fromRGB(255, 0, 0);
			player.getInventory().setBoots(setColor(new ItemBuilder(Material.LEATHER_BOOTS).setBreakable(false).build(), c));
			player.getInventory().setLeggings(setColor(new ItemBuilder(Material.LEATHER_LEGGINGS).setBreakable(false).build(), c));
			player.getInventory().setChestplate(setColor(new ItemBuilder(Material.LEATHER_CHESTPLATE).setBreakable(false).build(), c));
			player.getInventory().setHelmet(setColor(new ItemBuilder(Material.LEATHER_HELMET).setBreakable(false).build(), c));
		}
	}
	
	public static void setLobbyInventory(PlayerProfile playerProfile) {
		Player player = playerProfile.getPlayer();
		Inventory inventory = player.getInventory();
		inventory.clear();
		inventory.setItem(3, teamBlueSelector.disenchant().setLore(MultiToolInventory.getLore(Team.BLUE)).build());
		inventory.setItem(5, teamRedSelector.disenchant().setLore(MultiToolInventory.getLore(Team.RED)).build());
		if(playerProfile.getTeam().equals(Team.BLUE))
			inventory.setItem(3, teamBlueSelector.addEnchantment(Enchantment.ARROW_INFINITE, 10).build());
		else if(playerProfile.getTeam().equals(Team.RED))
			inventory.setItem(5, teamRedSelector.addEnchantment(Enchantment.ARROW_INFINITE, 10).build());
		inventory.setItem(0, voteMap.build());
		inventory.setItem(1, customizeInventory.build());
		inventory.setItem(4, multiTool.build());
		if(player.isOp()) {
			inventory.setItem(7, gameruleSettings.build());
			inventory.setItem(8, adminTool.build());
			/*inventory.setItem(1, new ItemBuilder(Material.MAP).setName("§7Select Map").build());
			inventory.setItem(6, new ItemBuilder(Material.CLOCK).setName("start Game").build());
			inventory.setItem(7, new ItemBuilder(Material.WHITE_CONCRETE).setName("change Teams").build());
			inventory.setItem(8, new ItemBuilder(Material.ARROW).setName("Edit Mode").build());*/
		}
	}
	
	public static ItemBuilder getMapVote() {
		return voteMap;
	}
	
	public static ItemBuilder getCustomizeInventory() {
		return customizeInventory;
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
	
	public static ItemBuilder getTeamRandomSelector() {
		return teamRandomSelector;
	}
	
	public static ItemBuilder getGameruleSettings() {
		return gameruleSettings;
	}
	
	public static ItemStack setColor(ItemStack armor, Color c) {
		LeatherArmorMeta meta = (LeatherArmorMeta) armor.getItemMeta();
		meta.setColor(c);
		armor.setItemMeta(meta);
		return armor;
	}
	
}
