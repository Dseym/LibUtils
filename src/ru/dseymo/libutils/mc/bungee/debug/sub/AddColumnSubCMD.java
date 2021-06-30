package ru.dseymo.libutils.mc.bungee.debug.sub;

import java.sql.Connection;
import java.util.Arrays;

import net.md_5.bungee.api.CommandSender;
import ru.dseymo.libutils.ThreadService;
import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.debug.LogMessage;
import ru.dseymo.libutils.debug.LogMessage.LogType;
import ru.dseymo.libutils.mc.bungee.Chat;
import ru.dseymo.libutils.mc.bungee.cmd.SubExtendCommand;
import ru.dseymo.libutils.mc.bungee.debug.DebugCMD;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class AddColumnSubCMD extends SubExtendCommand {
	
	public AddColumnSubCMD() {
		super("addColumn", "", "[table] [column] &6Add a new column");
	}
	
	@Override
	public void execute(CommandSender sender, Args args) {
		if(!Debug.ENABLE) {
			Chat.FAIL.send(sender, "Debug mode off");
			return;
		} else if(DebugCMD.DB == null) {
			Chat.FAIL.send(sender, "DB is &4NULL");
			return;
		} else if(args.get(0).isEmpty()) {
			Chat.FAIL.send(sender, "Enter a name the table");
			return;
		} else if(args.get(1).isEmpty()) {
			Chat.FAIL.send(sender, "Enter the column");
			return;
		}
		
		ThreadService.startSync("debug", () -> {
			long time = System.currentTimeMillis();
			
			try(Connection c = DebugCMD.DB.getConnect()) {
				String[] _args = args.toArray();
				String other = new Args(Arrays.copyOfRange(_args, 1, _args.length)).toString();
				String sql = "ALTER TABLE `%table%` ADD COLUMN %column%".replace("%table%", args.get(0)).replace("%column%", other);
				
				c.prepareStatement(sql).execute();
				
				Debug.message(LogMessage.create("DEBUG-SQL", LogType.INFO, sender.getName() + ":SQL: " + sql), false);
				
				Chat.SUCCESS.send(sender, "Success");
			} catch (Exception e) {
				Debug.message(LogMessage.create("DEBUG-SQL", LogType.ERROR, sender.getName() + ": " + e.getMessage()), false);
				
				Chat.FAIL.send(sender, e.getMessage());
			}
			
			Chat.INFO.send(sender, "Time: " + (System.currentTimeMillis()-time) + "ms");
		});
	}
	
	@Override
	public boolean checkPerm(CommandSender sender) {
		return DebugCMD.CONDITION.test(sender.getName());
	}
	
}
