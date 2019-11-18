package me.zwoosks.antiswear;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandManager implements CommandExecutor {

	private Main plugin;

	public CommandManager(Main plugin) {
		this.plugin = plugin;
		plugin.getCommand("antiswear").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("help")) {
				sendHelpMessage(sender);
			} else if (args[0].equalsIgnoreCase("add")) {
				if (sender.hasPermission("antiswear.add")) {
					if (args.length >= 2) {
						String tarjet = getArgs(args);
						List<String> newList = plugin.getConfig().getStringList("antiswear.words");
						newList.add(tarjet);
						plugin.getConfig().set("antiswear.words", newList);
						sender.sendMessage(Utils.chat(plugin.getConfig().getString("messages.changesSaved").replace("%player%", sender.getName())));
						plugin.saveConfig();
					} else {
						sender.sendMessage(Utils.chat(plugin.getConfig().getString("messages.addUsage").replace("%player%", sender.getName())));
					}
				} else {
					sender.sendMessage(Utils
							.chat(plugin.getConfig().getString("messages.noPerm").replace("%player%", sender.getName())));
				}
			} else if (args[0].equalsIgnoreCase("remove")) {
				if (sender.hasPermission("antiswear.remove")) {
					if (args.length >= 2) {
						String tarjet = getArgs(args);
						if(listContains(tarjet)) {
							List<String> newList = plugin.getConfig().getStringList("antiswear.words");
							newList.remove(tarjet);
							plugin.getConfig().set("antiswear.words", newList);
							sender.sendMessage(Utils.chat(plugin.getConfig().getString("messages.changesSaved").replace("%player%", sender.getName())));
							plugin.saveConfig();
						} else {
							sender.sendMessage(Utils.chat(plugin.getConfig().getString("messages.cannotRemove").replace("%player%", sender.getName())));
						}
					} else {
						sender.sendMessage(Utils.chat(plugin.getConfig().getString("messages.removeUsage")));
					}
				} else {
					sender.sendMessage(Utils.chat(
							plugin.getConfig().getString("messages.noPerm").replace("%perm%", "antiswear.remove").replace("%player%", sender.getName())));
				}
			} else {
				sendHelpMessage(sender);
			}
		} else {
			sendHelpMessage(sender);
		}
		return true;
	}

	private void sendHelpMessage(CommandSender sender) {
		if (sender.hasPermission("antiswear.viewhelp")) {
			List<String> helpMessage = plugin.getConfig().getStringList("messages.help");
			for (String s : helpMessage) {
				sender.sendMessage(Utils.chat(s.replace("%player%", sender.getName())));
			}
		} else {
			sender.sendMessage(Utils
					.chat(plugin.getConfig().getString("messages.noPerm").replace("%perm%", "antiswear.viewhelp").replace("%player%", sender.getName())));
		}
	}
	
	private String getArgs(String[] args) {
		String toReturn = "";
		for(String s : args) {
			toReturn = toReturn + " " + s;
		}
		toReturn = toReturn.substring(1);
		return toReturn;
	}
	
	private boolean listContains(String s) {
		return plugin.getConfig().getStringList("antiswear.words").contains(s);
	}

}