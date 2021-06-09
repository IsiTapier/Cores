package de.gigaz.cores.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import de.gigaz.cores.classes.Core;
import de.gigaz.cores.classes.GameManager;
import de.gigaz.cores.classes.PlayerProfile;
import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Gamerules;

public class PlayerToggleSneakListener implements Listener {
	
	private final int coreRepairDistance = 2;
	
	@EventHandler
	public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
		GameManager gameManager = Main.getPlugin().getGameManager();
		Player player = event.getPlayer();
		PlayerProfile playerProfile = gameManager.getPlayerProfile(player);
		if(!Gamerules.getValue(Gamerules.repairCore))
			return;
		if(!gameManager.getCurrentGameState().equals(GameState.INGAME_STATE) || !player.getWorld().equals(gameManager.getCopiedWorld()))
			return;
		if(!event.isSneaking()) {
			playerProfile.stopCoreRepairing();
			return;
		}
		if(!player.getInventory().getItemInMainHand().getType().equals(Material.END_CRYSTAL))
			return;
		Core core = gameManager.getNearBrokenCore(player.getLocation(), coreRepairDistance, playerProfile.getTeam());
		if(core != null)
			playerProfile.startCoreRepairing(core);
	}
}
