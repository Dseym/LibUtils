package ru.dseymo.libutils.mc.bukkit.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import ru.dseymo.libutils.mc.bukkit.Chat;

public class ExtendCommand extends SubExtendCommand {
	public static String PREFIX_HELP = "----------------------------------------";
	public static String SUFFIX_HELP = "----------------------------------------";
	
	
	public ExtendCommand(String cmd, boolean onlyPlayer, String perm, String descr) {
		super(cmd, onlyPlayer, perm, descr);
		
		init();
		
		Bukkit.getPluginCommand(cmd).setTabCompleter(this);
	}
	
	public void help(CommandSender sender) {
		Chat.NO_PREFIX.send(sender, "", PREFIX_HELP);
		
		for(String help: getHelp(sender))
			Chat.NO_PREFIX.send(sender, " /" + help);
		
		Chat.NO_PREFIX.send(sender, SUFFIX_HELP, "");
	}

}
