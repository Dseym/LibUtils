package ru.dseymo.libutils.mc.bukkit.debug.sub;

import org.bukkit.command.CommandSender;

import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.libutils.mc.bukkit.cmd.SubExtendCommand;
import ru.dseymo.libutils.mc.bukkit.debug.DebugCMD;
import ru.dseymo.libutils.mc.bukkit.debug.SenderLogHandler;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class CreateHandlerSubCMD extends SubExtendCommand {

	public CreateHandlerSubCMD() {
		super("createHandler", false, "", "[keyMess] &6Create handler for you");
	}
	
	@Override
	public boolean execute(CommandSender sender, Args args) {
		if(!Debug.ENABLE) {
			Chat.FAIL.send(sender, "Debug mode off");
			return false;
		} else if(args.get(0).isEmpty()) {
			Chat.FAIL.send(sender, "Enter key log messages");
			return false;
		} else if(Debug.HANDLERS.containsKey("sender:" + sender.getName() + ":" + args.get(0))) {
			Chat.FAIL.send(sender, "Key already exist");
			return false;
		}
		
		Debug.registerHandler("sender:" + sender.getName() + ":" + args.get(0), new SenderLogHandler(sender, args.get(0)));
		
		Chat.SUCCESS.send(sender, "Handler created");
		
		return true;
	}
	
	@Override
	public boolean checkPerm(CommandSender sender) {
		return DebugCMD.CONDITION.test(sender.getName());
	}

}
