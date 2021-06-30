package ru.dseymo.libutils.mc.bukkit;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import ru.dseymo.libutils.mc.bukkit.cmd.Command;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class ConfirmCMD extends Command {
	private static String CMD = "confirm";
	public static String MESSAGE_CANCEL = "Cancelled",
				  		 MESSAGE_HOVER = "Click to confirm",
				  		 MESSAGE_PREFIX = "    &4&l";
	
	public static int DELAY_TICKS = 100;
	
	private static HashMap<UUID, Runnable> confirms = new HashMap<>();
	
	public static void init(String cmd) {
		CMD = cmd;
		
		new ConfirmCMD();
	}
	
	@SuppressWarnings("deprecation")
	public static void addConfirm(Player p, String message, Plugin plugin, Runnable run) {
		UUID uuid = p.getUniqueId();
		if(confirms.containsKey(uuid)) return;
		
		TextComponent mainComponent = new TextComponent(ColorUtil.color(MESSAGE_PREFIX + message));
		mainComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MESSAGE_HOVER).create()));
		mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + CMD));
		
		Chat.NO_PREFIX.send(p, "");
		p.spigot().sendMessage(mainComponent);
		Chat.NO_PREFIX.send(p, "");
		
		confirms.put(uuid, run);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!confirms.containsKey(uuid) || confirms.get(uuid) != run) return;
				
				confirms.remove(uuid);
				Chat.FAIL.send(p, MESSAGE_CANCEL);
			}
			
		}.runTaskLater(plugin, DELAY_TICKS);
	}
	
	
	public ConfirmCMD() {
		super(CMD, true, "");
	}
	
	@Override
	public boolean execute(CommandSender sender, Args args) {
		Player p = (Player)sender;
		UUID uuid = p.getUniqueId();
		
		if(confirms.containsKey(uuid)) {
			confirms.get(uuid).run();
			confirms.remove(uuid);
			
			return true;
		}
		
		return false;
	}
	
}