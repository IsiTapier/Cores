package de.gigaz.cores.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;
import de.gigaz.cores.util.Team;

public class BasicListeners implements Listener {
	
	@EventHandler
	public void onWeather(WeatherChangeEvent event) {
		event.getWorld().setStorm(false);
		event.setCancelled(true);
	}
	
		@EventHandler
	public void onFoodChange(FoodLevelChangeEvent event) {
		if(!(event.getEntity() instanceof Player))
			return;
		if(Main.getPlugin().getGameManager().getCurrentGameState() != GameState.INGAME_STATE || !event.getEntity().getWorld().equals(Main.getPlugin().getWorld("currentworld")))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		Team team = Main.getPlugin().getGameManager().getPlayerProfile(event.getPlayer()).getTeam();
		Bukkit.broadcastMessage(team.getColorCode() + event.getPlayer().getName() + " §8» §7" + event.getMessage());
	}
}
