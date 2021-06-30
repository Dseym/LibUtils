package ru.dseymo.libutils.mc.bungee.debug.sub;

import net.md_5.bungee.api.CommandSender;
import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.debug.LogMessage;
import ru.dseymo.libutils.debug.LogMessage.LogType;
import ru.dseymo.libutils.mc.bungee.Chat;
import ru.dseymo.libutils.mc.bungee.cmd.SubExtendCommand;
import ru.dseymo.libutils.mc.bungee.debug.DebugCMD;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class OffSubCMD extends SubExtendCommand {

	public OffSubCMD() {
		super("off", "", "&6Disable debug mode");
	}
	
	@Override
	public void execute(CommandSender sender, Args args) {
		if(!Debug.ENABLE) {
			Chat.FAIL.send(sender, "Debug already off");
			return;
		}
		
		Debug.message(LogMessage.create("DEBUG", LogType.INFO, sender.getName() + ": Debug off"), false);
		
		Debug.ENABLE = false;
		
		Chat.SUCCESS.send(sender, "Debug off");
	}
	
	@Override
	public boolean checkPerm(CommandSender sender) {
		return DebugCMD.CONDITION.test(sender.getName());
	}

}
