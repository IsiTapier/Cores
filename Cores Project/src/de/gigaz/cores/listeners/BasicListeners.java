package de.gigaz.cores.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class BasicListeners implements Listener {
	
	@EventHandler
	public void onWeather(WeatherChangeEvent event) {
		event.getWorld().setStorm(false);
		event.setCancelled(true);
	}
}
