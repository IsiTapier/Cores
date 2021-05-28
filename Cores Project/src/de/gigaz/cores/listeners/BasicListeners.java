package de.gigaz.cores.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import de.gigaz.cores.main.Main;
import de.gigaz.cores.util.GameState;

public class BasicListeners implements Listener {
	
	@EventHandler
	public void onWeather(WeatherChangeEvent event) {
		event.getWorld().setStorm(false);
		event.setCancelled(true);
	}
	
		@EventHandler
	public void onFoodChange(FoodLevelChangeEvent event) {
		if(Main.getPlugin().getGameManager().getCurrentGameState() != GameState.INGAME_STATE)
			event.setCancelled(true);
	}
}
