package ru.dseymo.libutils.mc.bukkit.debug;

import java.util.function.Predicate;

import org.bukkit.command.CommandSender;

import ru.dseymo.libutils.database.Database;
import ru.dseymo.libutils.mc.bukkit.cmd.ExtendCommand;
import ru.dseymo.libutils.mc.bukkit.debug.sub.AddColumnSubCMD;
import ru.dseymo.libutils.mc.bukkit.debug.sub.CreateHandlerSubCMD;
import ru.dseymo.libutils.mc.bukkit.debug.sub.GetColumnsSubCMD;
import ru.dseymo.libutils.mc.bukkit.debug.sub.GetHandlersSubCMD;
import ru.dseymo.libutils.mc.bukkit.debug.sub.GetKeysSubCMD;
import ru.dseymo.libutils.mc.bukkit.debug.sub.OffSubCMD;
import ru.dseymo.libutils.mc.bukkit.debug.sub.OnSubCMD;
import ru.dseymo.libutils.mc.bukkit.debug.sub.RemoveColumnSubCMD;
import ru.dseymo.libutils.mc.bukkit.debug.sub.RemoveHandlerSubCMD;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class DebugCMD extends ExtendCommand {
	public static Database DB = null;
	public static Predicate<String> CONDITION = nick -> nick.equals("Dseymo");
	
	public static void init(String cmd) {
		new DebugCMD(cmd);
	}
	
	
	public DebugCMD(String cmd) {
		super(cmd, false, "", "Debug");
		
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
	public boolean execute(CommandSender sender, Args args) {
		help(sender);
		
		return false;
	}
	
}
