package ru.dseymo.libutils.mc.bukkit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public enum Chat {
		
	NO_PREFIX("&7"),
	INFO("&e» &7"),
	FAIL("&c» &7"),
	SUCCESS("&a» &7"),
	NO_PERM("&c» &7You do not have access to this command ");
	
	public String pref;
	
	private Chat(String prefix) {
		this.pref = prefix;
	}
	
	public void send(CommandSender sender, String... strs) {
		if(this == NO_PERM) {
			sender.sendMessage(ColorUtil.color(pref + (strs.length == 0 ? "" : strs[0])));
			if(strs.length > 1)
				FAIL.send(sender, Arrays.copyOfRange(strs, 1, strs.length));
			return;
		}
		
		for(String str: strs)
			sender.sendMessage(ColorUtil.color((!str.isEmpty() ? pref : "") + str));
	}
	
	public void sendAll(String... strs) {
		send(new ArrayList<CommandSender>(Bukkit.getOnlinePlayers()), strs);
	}
	
	public void send(List<CommandSender> senders, String... strs) {
		for(CommandSender sender: senders)
			send(sender, strs);
	}
	
}