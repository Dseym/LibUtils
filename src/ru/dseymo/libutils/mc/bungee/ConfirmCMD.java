package ru.dseymo.libutils.mc.bungee;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.scheduler.BungeeScheduler;
import ru.dseymo.libutils.mc.bungee.cmd.Command;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class ConfirmCMD extends Command {
	private static String CMD = "confirm";
	public static String MESSAGE_CANCEL = "Cancelled",
				  		 MESSAGE_HOVER = "Click to confirm",
				  		 MESSAGE_PREFIX = "    &4&l";
	
	public static int DELAY_SECONDS = 5;
	
	public static final BungeeScheduler SCHEDULER = new BungeeScheduler();
	
	private static HashMap<UUID, Runnable> confirms = new HashMap<>();
	
	public static void init(Plugin plugin, String cmd) {
		CMD = cmd;
		
		new ConfirmCMD(plugin);
	}
	
	@SuppressWarnings("deprecation")
	public static void addConfirm(ProxiedPlayer p, String message, Plugin plugin, Runnable run) {
		UUID uuid = p.getUniqueId();
		if(confirms.containsKey(uuid)) return;
		
		TextComponent mainComponent = new TextComponent(ColorUtil.color(MESSAGE_PREFIX + message));
		mainComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(MESSAGE_HOVER).create()));
		mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + CMD));
		
		Chat.NO_PREFIX.send(p, "");
		p.sendMessage(mainComponent);
		Chat.NO_PREFIX.send(p, "");
		
		confirms.put(uuid, run);
		
		SCHEDULER.schedule(plugin, new Runnable() {
			
			@Override
			public void run() {
				if(!confirms.containsKey(uuid) || confirms.get(uuid) != run) return;
				
				confirms.remove(uuid);
				Chat.FAIL.send(p, MESSAGE_CANCEL);
			}
			
		}, DELAY_SECONDS, TimeUnit.SECONDS);
	}
	
	
	public ConfirmCMD(Plugin plugin) {
		super(CMD, "");
		
		init(plugin);
	}

	@Override
	public void execute(CommandSender sender, Args args) {
		ProxiedPlayer p = (ProxiedPlayer)sender;
		UUID uuid = p.getUniqueId();
		
		if(confirms.containsKey(uuid)) {
			confirms.get(uuid).run();
			confirms.remove(uuid);
		}
	}
	
}