package ru.dseymo.libutils.mc.bukkit.debug.sub;

import java.sql.Connection;

import org.bukkit.command.CommandSender;

import ru.dseymo.libutils.ThreadService;
import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.debug.LogMessage;
import ru.dseymo.libutils.debug.LogMessage.LogType;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.libutils.mc.bukkit.cmd.SubExtendCommand;
import ru.dseymo.libutils.mc.bukkit.debug.DebugCMD;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class RemoveColumnSubCMD extends SubExtendCommand {
	
	public RemoveColumnSubCMD() {
		super("removeColumn", false, "", "[table] [nameColumn] &6Remove column");
	}
	
	@Override
	public boolean execute(CommandSender sender, Args args) {
		if(!Debug.ENABLE) {
			Chat.FAIL.send(sender, "Debug mode off");
			return false;
		} else if(DebugCMD.DB == null) {
			Chat.FAIL.send(sender, "DB is &4NULL");
			return false;
		} else if(args.get(0).isEmpty()) {
			Chat.FAIL.send(sender, "Enter a name the table");
			return false;
		} else if(args.get(1).isEmpty()) {
			Chat.FAIL.send(sender, "Enter a name the column");
			return false;
		}
		
		ThreadService.startSync("debug", () -> {
			long time = System.currentTimeMillis();
			
			try(Connection c = DebugCMD.DB.getConnect()) {
				String sql = "ALTER TABLE `%table%` DROP COLUMN `%nameColumn%`".replace("%table%", args.get(0)).replace("%nameColumn%", args.get(1));
				
				c.prepareStatement(sql).execute();
				
				Debug.message(LogMessage.create("DEBUG-SQL", LogType.INFO, sender.getName() + ":SQL: " + sql), false);
				
				Chat.SUCCESS.send(sender, "Success");
			} catch (Exception e) {
				Debug.message(LogMessage.create("DEBUG-SQL", LogType.ERROR, sender.getName() + ": " + e.getMessage()), false);
				
				Chat.FAIL.send(sender, e.getMessage());
			}
			
			Chat.INFO.send(sender, "Time: " + (System.currentTimeMillis()-time) + "ms");
		});
		
		return true;
	}
	
	@Override
	public boolean checkPerm(CommandSender sender) {
		return DebugCMD.CONDITION.test(sender.getName());
	}
	
}
