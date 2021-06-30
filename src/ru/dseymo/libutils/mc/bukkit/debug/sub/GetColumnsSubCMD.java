package ru.dseymo.libutils.mc.bukkit.debug.sub;

import java.sql.Connection;
import java.sql.ResultSet;

import org.bukkit.command.CommandSender;

import ru.dseymo.libutils.ThreadService;
import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.debug.LogMessage;
import ru.dseymo.libutils.debug.LogMessage.LogType;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.libutils.mc.bukkit.cmd.SubExtendCommand;
import ru.dseymo.libutils.mc.bukkit.debug.DebugCMD;
import ru.dseymo.libutils.mc.core.cmd.Args;

public class GetColumnsSubCMD extends SubExtendCommand {
	
	public GetColumnsSubCMD() {
		super("getColumns", false, "", "[table] &6Get all columns in table");
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
		}
		
		ThreadService.startSync("debug", () -> {
			long time = System.currentTimeMillis();
			
			try(Connection c = DebugCMD.DB.getConnect()) {
				String sql = "DESCRIBE " + args.get(0);
				ResultSet rs = c.prepareStatement(sql).executeQuery();
				
				Debug.message(LogMessage.create("DEBUG-SQL", LogType.INFO, sender.getName() + ":SQL: " + sql), false);
				
				Chat.INFO.send(sender, "Field       Type       Null       Key       Default       Extra");
				while(rs.next())
					Chat.INFO.send(sender, rs.getObject("Field") + "   " + rs.getObject("Type") + "   " + rs.getObject("Null") + "   " + rs.getObject("Key") + "   " + rs.getObject("Default") + "   " + rs.getObject("Extra"));
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
