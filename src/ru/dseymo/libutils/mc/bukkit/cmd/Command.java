package ru.dseymo.libutils.mc.bukkit.cmd;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import lombok.AccessLevel;
import lombok.Getter;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.libutils.mc.core.cmd.Args;

@Getter(AccessLevel.PROTECTED)
public abstract class Command implements CommandExecutor {
	public static String MESS_ONLY_PLAYERS = "Command only for players!";
	
	
	private String cmd;
	private String perm;
	private boolean onlyPlayer;
	
	public Command(String cmd, boolean onlyPlayer, String perm) {
		this.cmd = cmd;
		this.perm = perm;
		this.onlyPlayer = onlyPlayer;
	}
	
	public void init() {
		Bukkit.getPluginCommand(cmd).setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if(onlyPlayer && !(sender instanceof Player)) {
			cmdOnlyForPlayer(sender);
			return false;
		}
		
		if(perm.isEmpty() || sender.hasPermission(perm))
			 return execute(sender, new Args(args));
		else
			Chat.NO_PERM.send(sender);
		
		return false;
	}
	
	public abstract boolean execute(CommandSender sender, Args args);
	
	public void cmdOnlyForPlayer(CommandSender sender) {
		Chat.FAIL.send(sender, MESS_ONLY_PLAYERS);
	}
	
	protected boolean checkPerm(CommandSender sender) {
		return getPerm().isEmpty() || sender.hasPermission(getPerm());
	}
	
}
