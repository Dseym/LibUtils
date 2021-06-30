package ru.dseymo.libutils.mc.bungee.debug.sub;

import net.md_5.bungee.api.CommandSender;
import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.mc.bungee.Chat;
import ru.dseymo.libutils.mc.bungee.cmd.SubExtendCommand;
import ru.dseymo.libutils.mc.bungee.debug.DebugCMD;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class GetKeysSubCMD extends SubExtendCommand {
	
	public GetKeysSubCMD() {
		super("getKeys", "", "&6Get active keys for messages");
	}
	
	@Override
	public void execute(CommandSender sender, Args args) {
		if(!Debug.ENABLE) {
			Chat.FAIL.send(sender, "Debug mode off");
			return;
		}
		
		Chat.NO_PREFIX.send(sender, "");
		Chat.SUCCESS.send(sender, "Keys");
		
		for(String key: Debug.getKeys())
			Chat.INFO.send(sender, key);
		
		Chat.NO_PREFIX.send(sender, "");
	}
	
	@Override
	public boolean checkPerm(CommandSender sender) {
		return DebugCMD.CONDITION.test(sender.getName());
	}
	
}
