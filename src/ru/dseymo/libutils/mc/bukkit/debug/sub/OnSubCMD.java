package ru.dseymo.libutils.mc.bukkit.debug.sub;

import org.bukkit.command.CommandSender;

import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.debug.LogMessage;
import ru.dseymo.libutils.debug.LogMessage.LogType;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.libutils.mc.bukkit.cmd.SubExtendCommand;
import ru.dseymo.libutils.mc.bukkit.debug.DebugCMD;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class OnSubCMD extends SubExtendCommand {

	public OnSubCMD() {
		super("on", false, "", "&6Enable debug mode");
	}
	
	@Override
	public boolean execute(CommandSender sender, Args args) {
		if(Debug.ENABLE) {
			Chat.FAIL.send(sender, "Debug already on");
			return false;
		}
		
		Debug.ENABLE = true;
		
		Debug.message(LogMessage.create("DEBUG", LogType.INFO, sender.getName() + ": Debug on"), false);
		
		Chat.SUCCESS.send(sender, "Debug on");
		
		return true;
	}
	
	@Override
	public boolean checkPerm(CommandSender sender) {
		return DebugCMD.CONDITION.test(sender.getName());
	}
	
}
