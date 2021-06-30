package ru.dseymo.libutils.mc.bukkit.debug.sub;

import org.bukkit.command.CommandSender;

import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.libutils.mc.bukkit.cmd.SubExtendCommand;
import ru.dseymo.libutils.mc.bukkit.debug.DebugCMD;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class RemoveHandlerSubCMD extends SubExtendCommand {
	
	public RemoveHandlerSubCMD() {
		super("removeHandler", false, "", "[keyMess] &6Remove your handler");
	}
	
	@Override
	public boolean execute(CommandSender sender, Args args) {
		if(!Debug.ENABLE) {
			Chat.FAIL.send(sender, "Debug mode off");
			return false;
		} else if(args.get(0).isEmpty()) {
			Chat.FAIL.send(sender, "Enter key log messages");
			return false;
		} else if(!Debug.HANDLERS.containsKey("sender:" + sender.getName() + ":" + args.get(0))) {
			Chat.FAIL.send(sender, "Key not found");
			return false;
		}
		
		Debug.unregisterHandler("sender:" + sender.getName() + ":" + args.get(0));
		
		Chat.SUCCESS.send(sender, "Handler removed");
		
		return true;
	}
	
	@Override
	public boolean checkPerm(CommandSender sender) {
		return DebugCMD.CONDITION.test(sender.getName());
	}
	
}
