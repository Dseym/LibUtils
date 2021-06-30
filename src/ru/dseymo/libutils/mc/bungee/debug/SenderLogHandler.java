package ru.dseymo.libutils.mc.bungee.debug;

import net.md_5.bungee.api.CommandSender;
import ru.dseymo.libutils.debug.LogHandler;
import ru.dseymo.libutils.debug.LogMessage;
import ru.dseymo.libutils.mc.bungee.Chat;

public class SenderLogHandler implements LogHandler {
	private CommandSender sender;
	private String key;
	
	public SenderLogHandler(CommandSender sender, String key) {
		this.sender = sender;
		this.key = key;
	}
	
	@Override
	public void newMessage(LogMessage message) {
		if(message.getKey().equals(key)) {
			Chat.NO_PREFIX.send(sender, "");
			
			switch(message.getType()) {
				case ERROR:
					Chat.FAIL.send(sender, "&1HANDLER&8:&2" + message.getKey() + "&8:&4ERROR");
					break;
					
				case INFO:
					Chat.INFO.send(sender, "&1HANDLER&8:&2" + message.getKey() + "&8:&7INFO");
					break;
					
				case WARNING:
					Chat.FAIL.send(sender, "&1HANDLER&8:&2" + message.getKey() + "&8:&eWARNING");
					break;
			}
			
			Chat.INFO.send(sender, message.getText());
			Chat.NO_PREFIX.send(sender, "");
		}
	}
	
	@Override
	public void newLogInFile(LogMessage message) {}
}
