package me.zwoosks.antiswear;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Main extends JavaPlugin {
	
	public static HashMap<String, Integer> mutedPlayers = new HashMap<String, Integer>();
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                for(Map.Entry<String, Integer> me : Main.mutedPlayers.entrySet()) {
                	Main.mutedPlayers.put(me.getKey(), me.getValue() + 1);
                	if((me.getValue() + 1) >= getConfig().getInt("antiswear.muteMinutes")) {
                		Main.mutedPlayers.remove(me.getKey());
                	}
                }
            }
        }, 20L*60);
	}
	
}
