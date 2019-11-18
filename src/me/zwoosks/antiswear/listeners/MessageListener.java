package me.zwoosks.antiswear.listeners;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.zwoosks.antiswear.Main;
import me.zwoosks.antiswear.Utils;

public class MessageListener implements Listener {
	
	private Main plugin;
	
	public MessageListener(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerMessage(AsyncPlayerChatEvent e) {
		Player player = e.getPlayer();
		if(!player.hasPermission("antiswear.bypass") && plugin.getConfig().getBoolean("antiswear.active")) {
			String index;
			if(Bukkit.getServer().getOnlineMode()) {
				index = player.getUniqueId().toString();
			} else {
				index = player.getName().toLowerCase();
			}
			if(Main.mutedPlayers.containsKey(index)) {
				e.setCancelled(true);
			} else {
				List<String> words = plugin.getConfig().getStringList("antiswear.words");
				String message = e.getMessage();
				String newMessage = message;
				boolean said = false;
				for(String s : words) {
					if(message.contains(s)) {
						int let = s.length();
						newMessage = message.replace(s, org.apache.commons.lang.StringUtils.repeat("*", let));
						said = true;
					}
				}
				if(said) {
					processWarn(player);
					e.setMessage(newMessage);
				}
			}
		}
	}
	
	private void processWarn(Player player) {
		String index;
		if(Bukkit.getServer().getOnlineMode()) {
			index = player.getUniqueId().toString();
		} else {
			index = player.getName().toLowerCase();
		}
		int warnedTimes = timesWarned(index);
		int max = plugin.getConfig().getInt("antiswear.muteOnXTimes");
		if(warnedTimes >= max-1) {
			plugin.getConfig().set("warned-players." + index, null);
			Main.mutedPlayers.put(index, plugin.getConfig().getInt("antiswear.muteMinutes"));
			player.sendMessage(Utils.chat(plugin.getConfig().getString("messages.muted").replace("%player%", player.getName().replace("%max%", Integer.toString(max).replace("%minutes%", plugin.getConfig().getString("antiswear.muteMinutes"))))));
		} else {
			plugin.getConfig().set("warned-players." + index, warnedTimes + 1);
			player.sendMessage(Utils.chat(plugin.getConfig().getString("messages.warn").replace("%player%", player.getName())));
		}
		plugin.saveConfig();
	}
	
	private int timesWarned(String index) {
		if(plugin.getConfig().isSet("warned-players." + index)) {
			return plugin.getConfig().getInt("warned-players." + index);
		} else {
			return 0;
		}
	}
	
}