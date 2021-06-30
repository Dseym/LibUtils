package ru.dseymo.libutils.mc.bungee.cmd;

import lombok.AccessLevel;
import lombok.Getter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import ru.dseymo.libutils.mc.bungee.Chat;
import ru.dseymo.libutils.mc.core.cmd.Args;

@Getter(AccessLevel.PROTECTED)
public abstract class Command extends net.md_5.bungee.api.plugin.Command {
	private String cmd;
	private String perm;
	
	public Command(String cmd, String perm, String...aliases) {
		super(cmd, perm, aliases);
		
		this.cmd = cmd;
		this.perm = perm;
	}
	
	public void init(Plugin plugin) {
		BungeeCord.getInstance().getPluginManager().registerCommand(plugin, this);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(checkPerm(sender))
			 execute(sender, new Args(args));
		else
			Chat.NO_PERM.send(sender);
	}
	
	public abstract void execute(CommandSender sender, Args args);
	
	protected boolean checkPerm(CommandSender sender) {
		return getPerm().isEmpty() || sender.hasPermission(getPerm());
	}
	
}
