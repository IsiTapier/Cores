package de.gigaz.cores.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import de.gigaz.cores.classes.GameruleSetting;
import de.gigaz.cores.classes.GameruleSetting.GameruleCategory;

public class Gamerules {
	//gamerule names
	//effects
	public static final String aqua = "aqua";
	public static final String haste = "haste";
	public static final String jumpboost = "jumpboost";
	public static final String speed = "speed";
	public static final String invisibility = "invisibility";
	public static final String glowing = "glowing";
	
	//general
	public static final String autoTeam = "auto Team";
	public static final String randomTeam = "random Teams"; //TODO
	public static final String winMusic = "Play Win Music";
	public static final String soundEffects = "Play Sound Effects";
	public static final String moreGamerules = "more gamerules";
	public static final String evenMoreGamerules = "even more gamerules";
	
	//ingame
	public static final String noFallDamage = "no Fall Damage";
	public static final String noKnockback = "no Knockback";
	public static final String miningFatique = "Abbaulähmung";
	public static final String quickRespawn = "Quick Respawn";
	public static final String autoBlockPlace = "Auto Block Placement";
	public static final String night = "Night";
	public static final String coreProtection = "Core Protection";
	public static final String spawnProtection = "Spawn Protection";
	public static final String firstCoreWins = "First Core Wins";
	public static final String repairCore = "Repair Core";
	
	//inventory
	public static final String combatAxe = "Combat Axe";
	public static final String shield = "Shield";
	public static final String infiniteArrows = "infinite arrows";
	public static final String superCrossbow = "super Crossbow";
	public static final String onehit = "onehit";
	public static final String moreArrows = "more arrows";
	public static final String moreGoldApples = "more gold apples";
	public static final String crossbow = "Crossbow";
	public static final String flintandsteel = "Flint and Steel";
	public static final String snowball = "Snowballs";
	public static final String lavaBukket = "Lava";
	public static final String waterBukket = "Water";
	public static final String enderpearl = "Enderpearls";
	public static final String fishingRod = "Fishing Rod";
	
	//inventory basics
	public static final String sword = "Sword";
	public static final String bow = "Bow";
	public static final String axe = "Axe";
	public static final String blocks = "Blocks";
	public static final String goldApples = "Gold Apples";
	public static final String arrows = "Arrows";
	public static final String pickaxe = "Pickaxe";
	public static final String armor = "Armor";
	
	//special
	public static final String witherSkull = "Wither Skulls";
	public static final String knockbackStick = "Knockback stick";
	public static final String bridgeEgg = "Bridge Eggs";
	public static final String instantWall = "Instant Wall";

	//gamerule settings
	private static final HashMap<String, GameruleSetting> gameruleSettings = new HashMap<String, GameruleSetting>() {{
		put(aqua, new GameruleSetting(GameruleCategory.GAME, 11, new ItemBuilder(Material.PUFFERFISH_BUCKET).setName(aqua).build()));
		put(haste, new GameruleSetting(GameruleCategory.GAME, 14, new ItemBuilder(Material.GOLDEN_PICKAXE).setName(haste).build()));
		put(jumpboost, new GameruleSetting(GameruleCategory.GAME, 17, new ItemBuilder(Material.RABBIT_FOOT).setName(jumpboost).build()));
		put(speed, new GameruleSetting(GameruleCategory.GAME, 16, new ItemBuilder(Material.LEATHER_BOOTS).setName(speed).build()));
		put(invisibility, new GameruleSetting(GameruleCategory.GAME, 18, new ItemBuilder(Material.WHITE_STAINED_GLASS).setName(invisibility).build()));
		put(glowing, new GameruleSetting(GameruleCategory.GAME, 19, new ItemBuilder(Material.SPECTRAL_ARROW).setName(glowing).build()));
		put(autoTeam, new GameruleSetting(GameruleCategory.GAME, 4, new ItemBuilder(Material.STRUCTURE_BLOCK).setName(autoTeam).build(), true));
		put(randomTeam, new GameruleSetting(GameruleCategory.GAME, 5, new ItemBuilder(Material.COMMAND_BLOCK).setName(randomTeam).build()));
		put(noFallDamage, new GameruleSetting(GameruleCategory.GAME, 12, new ItemBuilder(Material.IRON_BOOTS).setName(noFallDamage).addEnchantment(Enchantment.PROTECTION_FALL, 10).build()));
		put(miningFatique, new GameruleSetting(GameruleCategory.GAME, 15, new ItemBuilder(Material.WOODEN_SHOVEL).setName(miningFatique).build(), true));
		put(combatAxe, new GameruleSetting(GameruleCategory.INVENTORY, 5, new ItemBuilder(Material.IRON_AXE).setName(combatAxe).build(), true));
		put(shield, new GameruleSetting(GameruleCategory.INVENTORY, 10, new ItemBuilder(Material.SHIELD).setName(shield).build()));
		put(infiniteArrows, new GameruleSetting(GameruleCategory.SPECIAL, 0, new ItemBuilder(Material.ARROW).setName(infiniteArrows).addEnchantment(Enchantment.ARROW_INFINITE, 10).build()));
		put(superCrossbow, new GameruleSetting(GameruleCategory.SPECIAL, 0, new ItemBuilder(Material.CROSSBOW).setName(superCrossbow).addEnchantment(Enchantment.QUICK_CHARGE, 10).build()));
		put(onehit, new GameruleSetting(GameruleCategory.GAME, 21, new ItemBuilder(Material.NETHERITE_SWORD).setName(onehit).addEnchantment(Enchantment.DAMAGE_ALL, 10).build()));
		put(moreArrows, new GameruleSetting(GameruleCategory.INVENTORY, 1, new ItemBuilder(Material.ARROW).setName(moreArrows).setAmount(64).build()));
		put(moreGoldApples, new GameruleSetting(GameruleCategory.INVENTORY, 3, new ItemBuilder(Material.GOLDEN_APPLE).setName(moreGoldApples).setAmount(64).build()));
		put(crossbow, new GameruleSetting(GameruleCategory.INVENTORY, 9, new ItemBuilder(Material.CROSSBOW).setName(crossbow).build()));
		put(sword, new GameruleSetting(GameruleCategory.INVENTORY, 4, new ItemBuilder(Material.IRON_SWORD).setName(sword).build(), true));
		put(bow, new GameruleSetting(GameruleCategory.INVENTORY, 8, new ItemBuilder(Material.BOW).setName(bow).build(), true));
		put(axe, new GameruleSetting(GameruleCategory.INVENTORY, 6, new ItemBuilder(Material.GOLDEN_AXE).setName(axe).build()));
		put(blocks, new GameruleSetting(GameruleCategory.INVENTORY, 12, new ItemBuilder(Material.OAK_PLANKS).setName(blocks).build(), true));
		put(goldApples, new GameruleSetting(GameruleCategory.INVENTORY, 2, new ItemBuilder(Material.GOLDEN_APPLE).setAmount(16).setName(goldApples).build(), true));
		put(arrows, new GameruleSetting(GameruleCategory.INVENTORY, 0, new ItemBuilder(Material.ARROW).setAmount(12).setName(arrows).build(), true));
		put(pickaxe, new GameruleSetting(GameruleCategory.INVENTORY, 7, new ItemBuilder(Material.IRON_PICKAXE).setName(pickaxe).build(), true));
		put(armor, new GameruleSetting(GameruleCategory.INVENTORY, 11, new ItemBuilder(Material.IRON_CHESTPLATE).setName(armor).build(), true));
		put(flintandsteel, new GameruleSetting(GameruleCategory.INVENTORY, 15, new ItemBuilder(Material.FLINT_AND_STEEL).setName(flintandsteel).build()));
		put(snowball, new GameruleSetting(GameruleCategory.INVENTORY, 14, new ItemBuilder(Material.SNOWBALL).setAmount(16).setName(snowball).build()));
		put(lavaBukket, new GameruleSetting(GameruleCategory.INVENTORY, 16, new ItemBuilder(Material.LAVA_BUCKET).setName(lavaBukket).build()));
		put(fishingRod, new GameruleSetting(GameruleCategory.INVENTORY, 18, new ItemBuilder(Material.FISHING_ROD).setName(fishingRod).build()));
		put(waterBukket, new GameruleSetting(GameruleCategory.INVENTORY, 17, new ItemBuilder(Material.WATER_BUCKET).setName(waterBukket).build()));
		put(enderpearl, new GameruleSetting(GameruleCategory.INVENTORY, 13, new ItemBuilder(Material.ENDER_PEARL).setAmount(8).setName(enderpearl).build()));
		put(witherSkull, new GameruleSetting(GameruleCategory.SPECIAL, 0, new ItemBuilder(Material.WITHER_SKELETON_SKULL).setName(witherSkull).build()));
		put(quickRespawn, new GameruleSetting(GameruleCategory.GAME, 8, new ItemBuilder(Material.RED_BED).setName(quickRespawn).build()));
		put(knockbackStick, new GameruleSetting(GameruleCategory.SPECIAL, 0, new ItemBuilder(Material.STICK).addEnchantment(Enchantment.ARROW_KNOCKBACK, 10).setName(knockbackStick).build()));
		put(noKnockback, new GameruleSetting(GameruleCategory.GAME, 13, new ItemBuilder(Material.ANVIL).setName(noKnockback).build()));
		put(autoBlockPlace, new GameruleSetting(GameruleCategory.GAME, 22, new ItemBuilder(Material.DROPPER).setName(autoBlockPlace).build()));
		put(night, new GameruleSetting(GameruleCategory.GAME, 20, new ItemBuilder(Material.CLOCK).setName(night).build()));
		put(coreProtection, new GameruleSetting(GameruleCategory.GAME, 7, new ItemBuilder(Material.BEDROCK).setName(coreProtection).build(), true));
		put(spawnProtection, new GameruleSetting(GameruleCategory.GAME, 6, new ItemBuilder(Material.BEDROCK).setName(spawnProtection).build(), true));
		put(winMusic, new GameruleSetting(GameruleCategory.GAME, 3, new ItemBuilder(Material.JUKEBOX).setName(winMusic).build(), true));
		put(soundEffects, new GameruleSetting(GameruleCategory.GAME, 2, new ItemBuilder(Material.NOTE_BLOCK).setName(soundEffects).build(), true));
		put(firstCoreWins, new GameruleSetting(GameruleCategory.GAME, 10, new ItemBuilder(Material.BEACON).setName(firstCoreWins).build()));
		put(repairCore, new GameruleSetting(GameruleCategory.GAME, 9, new ItemBuilder(Material.END_CRYSTAL).setName(repairCore).build(), true));
		put(bridgeEgg, new GameruleSetting(GameruleCategory.SPECIAL, 0, new ItemBuilder(Material.EGG).addEnchantment(Enchantment.ARROW_INFINITE, 10).hideEnchants().setName(bridgeEgg).build()));
		put(moreGamerules, new GameruleSetting(GameruleCategory.GAME, 0, new ItemBuilder(Material.LECTERN).setName(moreGamerules).build()));
		put(evenMoreGamerules, new GameruleSetting(GameruleCategory.GAME, 1, new ItemBuilder(Material.LECTERN).setName(evenMoreGamerules).build(), true));
		put(instantWall, new GameruleSetting(GameruleCategory.SPECIAL, 0, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS).setName(instantWall).build()));
	}};
	
	//links between gamerule settings
	public static void setGameruleSetting(GameruleSetting setting, boolean value) {
		setting.setValue(value);
		String name = setting.getItem().getItemMeta().getDisplayName();
		       if(name.equals(arrows) && value) {
			setGameruleValue(moreArrows, false);
			setGameruleValue(infiniteArrows, false);
		} else if(name.equals(moreArrows) && value) {
			setGameruleValue(arrows, false);
			setGameruleValue(infiniteArrows, false);
		} else if(name.equals(infiniteArrows) && value) {
			setGameruleValue(arrows, false);
			setGameruleValue(moreArrows, false);
		} else if(name.equals(goldApples) && value) {
			setGameruleValue(moreGoldApples, false);
		} else if(name.equals(moreGoldApples) && value) {
			setGameruleValue(goldApples, false);
		} else if(name.equals(bow) && value) {
			//setGameruleValue(crossbow, false);
			//setGameruleValue(superCrossbow, false);
		} else if(name.equals(crossbow) && value) {
			setGameruleValue(bow, false);
			setGameruleValue(superCrossbow, false);
		} else if(name.equals(superCrossbow) && value) {
			setGameruleValue(bow, false);
			setGameruleValue(crossbow, false);
		} else if(name.equals(axe) && value) {
			setGameruleValue(combatAxe, false);
		} else if(name.equals(combatAxe) && value) {
			setGameruleValue(axe, false);
		} else if(name.equals(moreGamerules) && value) {
			setGameruleValue(evenMoreGamerules, false);
		} else if(name.equals(evenMoreGamerules) && value) {
			setGameruleValue(moreGamerules, false);
		}
	}
	
	public static HashMap<String, GameruleSetting> getGameruleSettings() {
		return gameruleSettings;
	}
	
	public static GameruleSetting getGameruleSetting(String name) {
		return gameruleSettings.get(name);
	}
	
	public static boolean getGameruleValue(String name) {
		return getGameruleSetting(name).getValue();
	}
	
	public static boolean getValue(String name) {
		return getGameruleValue(name);
	}
	
	public static void setGameruleSetting(String name, GameruleSetting gameruleSetting) {
		gameruleSettings.put(name, gameruleSetting);
	}
	
	public static void setGameruleValue(String name, boolean value) {
		getGameruleSetting(name).setValue(value);
	}
	
	public static void setValue(String name, boolean value) {
		setGameruleValue(name, value);
	}

}
