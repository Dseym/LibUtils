package ru.dseymo.libutils.mc.bungee.cmd;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import ru.dseymo.libutils.mc.bungee.Chat;

public class ExtendCommand extends SubExtendCommand {
	public static String PREFIX_HELP = "----------------------------------------";
	public static String SUFFIX_HELP = "----------------------------------------";
	
	
	public ExtendCommand(Plugin plugin, String cmd, String perm, String descr, String...aliases) {
		super(cmd, perm, descr, aliases);
		
		init(plugin);
	}
	
	public void help(CommandSender sender) {
		Chat.NO_PREFIX.send(sender, "", PREFIX_HELP);
		
		for(String help: getHelp(sender))
			Chat.NO_PREFIX.send(sender, " /" + help);
		
		Chat.NO_PREFIX.send(sender, SUFFIX_HELP, "");
	}

}
