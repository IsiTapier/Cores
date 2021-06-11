package de.gigaz.cores.inventories;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import de.gigaz.cores.util.Gamerules;
import de.gigaz.cores.util.ItemBuilder;
import de.gigaz.cores.util.Team;
import de.gigaz.cores.util.inventory.InventoryClass;
import de.gigaz.cores.util.inventory.InventoryItem;
import de.gigaz.cores.util.inventory.InventorySlot;
import de.gigaz.cores.util.inventory.InventoryItem.DisplayCondition;
import de.gigaz.cores.util.inventory.InventoryItem.ItemAmount;

public class IngameInventory {
	
	public static InventoryClass getInventory(Team team) {
		return new InventoryClass("normal inventory", 18, new HashMap<Integer, InventorySlot>() {{
		put(0, new InventorySlot(
				new InventoryItem(0, new ItemBuilder(Material.IRON_SWORD).setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.sword);}}),
				new InventoryItem(1, new ItemBuilder(Material.STICK).setName("Knockback Stick").addEnchantment(Enchantment.KNOCKBACK, 2).hideEnchants().build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.knockbackStick);}})));
		put(1, new InventorySlot(
				new InventoryItem(0, new ItemBuilder(Material.CROSSBOW).setName("X-BOW").setLore("The legend X-Bow of strengthness.").setAmount(1).addEnchantment(Enchantment.QUICK_CHARGE, 5).hideEnchants().setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.superCrossbow);}}),
				new InventoryItem(1, new ItemBuilder(Material.CROSSBOW).setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.crossbow);}}),
				new InventoryItem(2, new ItemBuilder(Material.BOW).setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.bow);}})));
		put(2, new InventorySlot(
				new InventoryItem(0, new ItemBuilder(Material.IRON_AXE).setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.combatAxe);}}),
				new InventoryItem(1, new ItemBuilder(Material.GOLDEN_AXE).setBreakable(false).addEnchantment(Enchantment.DIG_SPEED, 1).hideEnchants().build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.axe);}})));
		put(3, new InventorySlot(
				new InventoryItem(0, new ItemBuilder(Material.OAK_LOG).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.UNSET) && Gamerules.getValue(Gamerules.blocks);}}),
				new InventoryItem(1, new ItemBuilder(Material.CRIMSON_STEM).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.RED) && Gamerules.getValue(Gamerules.blocks);}}),
				new InventoryItem(1, new ItemBuilder(Material.WARPED_STEM).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.BLUE) && Gamerules.getValue(Gamerules.blocks);}})));
		put(4, new InventorySlot(
				new InventoryItem(0, new ItemBuilder(Material.OAK_PLANKS).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.UNSET) && Gamerules.getValue(Gamerules.blocks);}}),
				new InventoryItem(1, new ItemBuilder(Material.CRIMSON_PLANKS).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.RED) && Gamerules.getValue(Gamerules.blocks);}}),
				new InventoryItem(1, new ItemBuilder(Material.WARPED_PLANKS).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.BLUE) && Gamerules.getValue(Gamerules.blocks);}})));
		put(5, new InventorySlot(
				new InventoryItem(0, new ItemBuilder(Material.OAK_PLANKS).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.UNSET) && Gamerules.getValue(Gamerules.blocks);}}),
				new InventoryItem(1, new ItemBuilder(Material.CRIMSON_PLANKS).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.RED) && Gamerules.getValue(Gamerules.blocks);}}),
				new InventoryItem(1, new ItemBuilder(Material.WARPED_PLANKS).setAmount(64).build(), new DisplayCondition() {@Override public boolean getCondition() {return team.equals(Team.BLUE) && Gamerules.getValue(Gamerules.blocks);}})));
		put(6, new InventorySlot(
				new InventoryItem(0, new ItemBuilder(Material.GOLDEN_APPLE).build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.goldApples);}}, new ItemAmount() {@Override public int getAmount() {return (int) Math.pow(2, Gamerules.getValue(Gamerules.goldApples, true)+1);}})));
		put(7, new InventorySlot(
				new InventoryItem(1, new ItemBuilder(Material.ARROW).build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.arrows)&&!Gamerules.isValue(Gamerules.arrows, 5);}}, new ItemAmount() {@Override public int getAmount() {return (int) Math.pow(2, Gamerules.getValue(Gamerules.arrows, true)+2);}}),
				new InventoryItem(1, new ItemBuilder(Material.ARROW).setName("§6Infinity Arrow").addEnchantment(Enchantment.ARROW_INFINITE, 10).setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.arrows)&&Gamerules.isValue(Gamerules.arrows, 5);}}, new ItemAmount() {@Override public int getAmount() {return (Gamerules.getValue(Gamerules.superCrossbow) || Gamerules.getValue(Gamerules.crossbow))?2:1;}}),
				new InventoryItem(5, new ItemBuilder(Material.FLINT_AND_STEEL).setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.flintandsteel);}}),
				new InventoryItem(6, new ItemBuilder(Material.SNOWBALL).setAmount(16).build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.snowball);}}),
				new InventoryItem(7, new ItemBuilder(Material.LAVA_BUCKET).build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.lavaBukket);}}),
				new InventoryItem(8, new ItemBuilder(Material.WATER_BUCKET).build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.waterBukket);}}),
				new InventoryItem(3, new ItemBuilder(Material.ENDER_PEARL).setAmount(8).build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.enderpearl);}}),
				new InventoryItem(4, new ItemBuilder(Material.FISHING_ROD).setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.fishingRod);}}),
				new InventoryItem(0, new ItemBuilder(Material.WITHER_SKELETON_SKULL).setName("Wither Skull").setLore("Right click to throw a wither skull").build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.witherSkull);}})));
		put(8, new InventorySlot(
				new InventoryItem(0, new ItemBuilder(Material.IRON_PICKAXE).setBreakable(false).build(), new DisplayCondition() {@Override public boolean getCondition() {return Gamerules.getValue(Gamerules.pickaxe);}})));
		}});
	}
	
}
