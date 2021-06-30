package ru.dseymo.libutils.mc.bungee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;

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
			sender.sendMessage(new ComponentBuilder(ColorUtil.color(pref + (strs.length == 0 ? "" : strs[0]))).create());
			if(strs.length > 1)
				FAIL.send(sender, Arrays.copyOfRange(strs, 1, strs.length));
			return;
		}
		
		for(String str: strs)
			sender.sendMessage(new ComponentBuilder(ColorUtil.color((!str.isEmpty() ? pref : "") + str)).create());
	}
	
	public void sendAll(String... strs) {
		send(new ArrayList<CommandSender>(BungeeCord.getInstance().getPlayers()), strs);
	}
	
	public void send(List<CommandSender> senders, String... strs) {
		for(CommandSender sender: senders)
			send(sender, strs);
	}
	
}