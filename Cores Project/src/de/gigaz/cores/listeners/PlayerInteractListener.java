package de.gigaz.cores.listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.inventories.ActionBlockInventory;
import de.gigaz.cores.inventories.AdminToolInventory;
import de.gigaz.cores.inventories.GameruleSettings;
import de.gigaz.cores.inventories.MapSelectInventory;
import de.gigaz.cores.inventories.MultiToolInventory;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.special.ActionBlock;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Gamerules;
import de.gigaz.cores.util.Inventories;
import de.gigaz.cores.util.ItemBuilder;
import de.gigaz.cores.util.Team;


public class PlayerInteractListener implements Listener {
	
	private ArrayList<Integer> potatos = new ArrayList<Integer>();
	float lastyaw;
	double lastx;
	double lastz;
	double xchange;
	double zchange;
	
	@EventHandler
	public void onInventoryInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		GameManager gameManager = Main.getPlugin().getGameManager();
		PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
		Block block = event.getClickedBlock();
		
		if(Main.getPlugin().getGameManager().getCurrentGameState() == GameState.INGAME_STATE) {
			if(player.getWorld().equals(Main.getPlugin().getWorld(Main.COPIED_WORLD_NAME))) {
				if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
					if(block == null)
						return;
					if(block.getType().equals(Material.BEACON) && Gamerules.getValue(Gamerules.miningFatique))
						player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, Gamerules.getValue(Gamerules.miningFatique, true)-1, false, false, false));
					else
						player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
				} else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
		            if (player.getInventory().getItemInMainHand().getType() == Material.WITHER_SKELETON_SKULL) {
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
		            if(block == null)
						return;
		            if(block.getType().equals(Material.BEACON))
		            	event.setCancelled(true);
				} else
	                
	                if(player.getInventory().getItemInMainHand().getType().equals(Material.NETHERITE_HOE)) {
	                	if(player.getInventory().contains(Material.POTATO)) {
	                		boolean explosive;
	                		if(player.getInventory().getItem(player.getInventory().first(Material.POTATO)).getItemMeta().getDisplayName().equalsIgnoreCase("explosive potato"))
	                			explosive = true;
	                		else
	                			explosive = false;
	                		player.getInventory().getItem(player.getInventory().first(Material.POTATO)).setAmount(player.getInventory().getItem(player.getInventory().first(Material.POTATO)).getAmount()-1);
	                		player.getInventory().setItemInMainHand(new ItemBuilder(Material.STONE_HOE).setName("Cooldown").build());
	                		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
								@Override
								public void run() {
									player.getInventory().setItem(player.getInventory().first(Material.STONE_HOE), new ItemBuilder(Material.NETHERITE_HOE).setName("Potato Gun").build());
								}}, 2*20L);
	                		float yaw = player.getLocation().getYaw();
	                    	double D = 1.0;
	                    	double x = -D*Math.sin(yaw*Math.PI/180);
	                    	double z = D*Math.cos(yaw*Math.PI/180);
	                        Item item =  player.getWorld().dropItem(player.getLocation().add(x, 1.62, z), new ItemStack(Material.POTATO));
	                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 2.5f);
	                        item.setVelocity(player.getLocation().getDirection().multiply(3));
	                        item.setPickupDelay(Integer.MAX_VALUE);
	                        final int index = potatos.size();
	                        lastyaw = item.getLocation().getYaw();
	                        lastx = item.getLocation().getX();
	                        lastz = item.getLocation().getZ();
	                        potatos.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
	        				@Override
	        				public void run() {
	        					/*if(Math.abs(item.getLocation().getYaw() - lastyaw) > 0) {
	        						Bukkit.broadcastMessage("test"+lastyaw+" "+item.getLocation().getYaw());
	        						lastyaw = item.getLocation().getYaw();
	        					}*/
	        					/*if(xchange!=0&&Math.abs(xchange-(lastx-item.getLocation().getX())) > 1) {
	        						//Bukkit.broadcastMessage("oldx"+xchange+"newx"+(lastx-item.getLocation().getX()));
	        						item.remove();
	        						for(Entity entity : item.getNearbyEntities(0.5, 0.5, 0.5))
	        							((LivingEntity) entity).damage(12);
	        						if(potatos.get(index) != null) {
	        							Bukkit.getScheduler().cancelTask(potatos.get(index));
	        							potatos.set(index, null);
	        						}
	        						if(explosive)
	        							item.getWorld().createExplosion(item.getLocation(), 1.5F, false, true, (Entity)player);
	        					}
	        					if(zchange!=0&&Math.abs(zchange-(lastz-item.getLocation().getZ())) > 1) {
	        						//Bukkit.broadcastMessage("oldz"+zchange+"newz"+(lastz-item.getLocation().getZ()));
	        						item.remove();
	        						for(Entity entity : item.getNearbyEntities(0.5, 0.5, 0.5))
	        							((LivingEntity) entity).damage(12);
	        						if(potatos.get(index) != null) {
	        							Bukkit.getScheduler().cancelTask(potatos.get(index));
	        							potatos.set(index, null);
	        						}
	        						if(explosive)
	        							item.getWorld().createExplosion(item.getLocation(), 1.5F, false, true, (Entity)player);
	        					}
	        					xchange = lastx-item.getLocation().getX();
	        					zchange = lastz-item.getLocation().getZ();
	        					lastx = item.getLocation().getX();
	        					lastz = item.getLocation().getZ();*/
	        					if(item.getNearbyEntities(0.5, 0.5, 0.5).size() != 0) {
	        						//Bukkit.broadcastMessage("entity");
	        						item.remove();
	        						for(Entity entity : item.getNearbyEntities(0.5, 0.5, 0.5))
	        							((LivingEntity) entity).damage(12);
	        						if(potatos.get(index) != null) {
	        							Bukkit.getScheduler().cancelTask(potatos.get(index));
	        							potatos.set(index, null);
	        						}
	        						if(explosive)
	        							item.getWorld().createExplosion(item.getLocation(), 1.5F, false, true, (Entity)player);
	        					}
	        					if(!item.getLocation().add(item.getVelocity().multiply(3)).getBlock().getType().equals(Material.AIR)) {
	        						//Bukkit.broadcastMessage("block");
	        						item.remove();
	        						if(potatos.get(index) != null) {
	        							Bukkit.getScheduler().cancelTask(potatos.get(index));
	        							potatos.set(index, null);
	        						}
	        						if(explosive)
	        							item.getWorld().createExplosion(item.getLocation(), 1.5F, false, true, (Entity)player);
	        					}
	        				} }, 2L, 1L));
	                	}
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
			else if(mainHand.equals(Inventories.getGameruleSettings().build()))
				player.openInventory(GameruleSettings.buildCategoryMenu());
			else if(mainHand.equals(Inventories.getCustomizeInventory().build()))
				player.chat("/c inventory");
			else if(mainHand.equals(Inventories.getMapVote().build()))
				player.openInventory(MapSelectInventory.getNormalInventory());
			    
			if(!playerProfile.isEditMode())
				event.setCancelled(true);
			
			if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {			
				if(event.getClickedBlock().getType().equals(ActionBlock.ACTION_BLOCK_MATERIAL)) {
					event.setCancelled(true);
					for(ActionBlock actionBlock : gameManager.getActionBlocks()) {
						if(actionBlock.getLocation().equals(block.getLocation())) {
							playerProfile.setEditActionBlock(actionBlock);
							player.openInventory(ActionBlockInventory.getInventory(actionBlock));
							break;
						}
					}
					
				}
			}
		
		}
		
	}
}
