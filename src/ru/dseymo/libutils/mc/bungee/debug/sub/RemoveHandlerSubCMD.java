package ru.dseymo.libutils.mc.bungee.debug.sub;

import net.md_5.bungee.api.CommandSender;
import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.mc.bungee.Chat;
import ru.dseymo.libutils.mc.bungee.cmd.SubExtendCommand;
import ru.dseymo.libutils.mc.bungee.debug.DebugCMD;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class RemoveHandlerSubCMD extends SubExtendCommand {
	
	public RemoveHandlerSubCMD() {
		super("removeHandler", "", "[keyMess] &6Remove your handler");
	}
	
	@Override
	public void execute(CommandSender sender, Args args) {
		if(!Debug.ENABLE) {
			Chat.FAIL.send(sender, "Debug mode off");
			return;
		} else if(args.get(0).isEmpty()) {
			Chat.FAIL.send(sender, "Enter key log messages");
			return;
		} else if(!Debug.HANDLERS.containsKey("sender:" + sender.getName() + ":" + args.get(0))) {
			Chat.FAIL.send(sender, "Key not found");
			return;
		}
		
		Debug.unregisterHandler("sender:" + sender.getName() + ":" + args.get(0));
		
		Chat.SUCCESS.send(sender, "Handler removed");
	}
	
	@Override
	public boolean checkPerm(CommandSender sender) {
		return DebugCMD.CONDITION.test(sender.getName());
	}
	
}
