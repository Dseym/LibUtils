package ru.dseymo.libutils.mc.bukkit.debug.sub;

import org.bukkit.command.CommandSender;

import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.libutils.mc.bukkit.cmd.SubExtendCommand;
import ru.dseymo.libutils.mc.bukkit.debug.DebugCMD;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class GetKeysSubCMD extends SubExtendCommand {
	
	public GetKeysSubCMD() {
		super("getKeys", false, "", "&6Get active keys for messages");
	}
	
	@Override
	public boolean execute(CommandSender sender, Args args) {
		if(!Debug.ENABLE) {
			Chat.FAIL.send(sender, "Debug mode off");
			return false;
		}
		
		Chat.NO_PREFIX.send(sender, "");
		Chat.SUCCESS.send(sender, "Keys");
		
		for(String key: Debug.getKeys())
			Chat.INFO.send(sender, key);
		
		Chat.NO_PREFIX.send(sender, "");
		
		return true;
	}
	
	@Override
	public boolean checkPerm(CommandSender sender) {
		return DebugCMD.CONDITION.test(sender.getName());
	}
	
}
