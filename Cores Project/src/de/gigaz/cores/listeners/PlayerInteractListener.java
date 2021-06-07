package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.inventories.AdminToolInventory;
import de.gigaz.cores.inventories.GameruleSettings;
import de.gigaz.cores.inventories.MapSelectInventory;
import de.gigaz.cores.inventories.MultiToolInventory;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.Team;


public class PlayerInteractListener implements Listener {
	@EventHandler
	public void onInventoryInteract(PlayerInteractEvent event) {

		Player player = event.getPlayer();
		GameManager gameManager = Main.getPlugin().getGameManager();
		PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
		Block block = event.getClickedBlock();
		
		if((Main.getPlugin().getGameManager().getCurrentGameState() == GameState.INGAME_STATE) && player.getWorld().equals(Main.getPlugin().getWorld(Main.COPIED_WORLD_NAME))) {
			if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
				if(block.getType().equals(Material.BEACON) && gameManager.getGameruleSetting(gameManager.miningFatiqueGamerule).getValue())
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 15*20, 0));
			} else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
	            if (player.getItemInHand().getType() == Material.WITHER_SKELETON_SKULL) {
	            	event.setCancelled(true);
	                //if(!player.getInventory().contains()) {
	               //player.sendMessage(ChatColor.DARK_AQUA + "You need a flint to shoot!");
	            	//player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1, 0);
	            	//e.setCancelled(true);
	            	float yaw = player.getLocation().getYaw();
	            	double D = 1.0;
	            	double x = -D*Math.sin(yaw*Math.PI/180);
	            	double z = D*Math.cos(yaw*Math.PI/180);
	            	Entity skull = player.getWorld().spawn(player.getLocation().add(x, 1.62, z), WitherSkull.class);
	            	skull.setVelocity(player.getLocation().getDirection().multiply(2));
	            }
			}
		} else {
			ItemStack mainHand = player.getInventory().getItemInMainHand();
			if(mainHand.containsEnchantment(Enchantment.ARROW_INFINITE))
				mainHand.removeEnchantment(Enchantment.ARROW_INFINITE);
			     if(mainHand.equals(Inventories.getMultiTool().build()))
				player.openInventory(MultiToolInventory.getInventory(playerProfile.getTeam()));
			else if(mainHand.equals(Inventories.getAdminTool().build()))
				player.openInventory(AdminToolInventory.getInventory());		
			else if(mainHand.equals(Inventories.getTeamRedSelector().disenchant().setLore(MultiToolInventory.getLore(Team.RED)).build()))
				player.chat("/c join red");	
			else if(mainHand.equals(Inventories.getTeamBlueSelector().disenchant().setLore(MultiToolInventory.getLore(Team.BLUE)).build()))
				player.chat("/c join blue");	
			else if(mainHand.equals(Inventories.getGameruleSettings().build())) {
				GameruleSettings.nextPage();
				player.openInventory(GameruleSettings.buildInventory());
			} else if(mainHand.equals(Inventories.getCustomizeInventory().build()))
				player.chat("/c inventory");
			else if(mainHand.equals(Inventories.getMapVote().build()))
				player.openInventory(MapSelectInventory.getNormalInventory());
			    
			if(!playerProfile.isEditMode())
				event.setCancelled(true);
		}
	}
}
