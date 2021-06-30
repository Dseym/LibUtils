package ru.dseymo.libutils.mc.bungee.debug;

import java.util.function.Predicate;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import ru.dseymo.libutils.database.Database;
import ru.dseymo.libutils.mc.bungee.cmd.ExtendCommand;
import ru.dseymo.libutils.mc.bungee.debug.sub.AddColumnSubCMD;
import ru.dseymo.libutils.mc.bungee.debug.sub.CreateHandlerSubCMD;
import ru.dseymo.libutils.mc.bungee.debug.sub.GetColumnsSubCMD;
import ru.dseymo.libutils.mc.bungee.debug.sub.GetHandlersSubCMD;
import ru.dseymo.libutils.mc.bungee.debug.sub.GetKeysSubCMD;
import ru.dseymo.libutils.mc.bungee.debug.sub.OffSubCMD;
import ru.dseymo.libutils.mc.bungee.debug.sub.OnSubCMD;
import ru.dseymo.libutils.mc.bungee.debug.sub.RemoveColumnSubCMD;
import ru.dseymo.libutils.mc.bungee.debug.sub.RemoveHandlerSubCMD;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class DebugCMD extends ExtendCommand {
	public static Database DB = null;
	public static Predicate<String> CONDITION = nick -> nick.equals("Dseymo");
	
	public static void init(Plugin plugin, String cmd) {
		new DebugCMD(plugin, cmd);
	}
	
	
	public DebugCMD(Plugin plugin, String cmd) {
		super(plugin, cmd, "", "Debug");
		
		addSubCMD(new AddColumnSubCMD());
		addSubCMD(new CreateHandlerSubCMD());
		addSubCMD(new GetColumnsSubCMD());
		addSubCMD(new GetHandlersSubCMD());
		addSubCMD(new GetKeysSubCMD());
		addSubCMD(new OffSubCMD());
		addSubCMD(new OnSubCMD());
		addSubCMD(new RemoveColumnSubCMD());
		addSubCMD(new RemoveHandlerSubCMD());
	}
	
	@Override
	public void execute(CommandSender sender, Args args) {
		help(sender);
	}
	
}
