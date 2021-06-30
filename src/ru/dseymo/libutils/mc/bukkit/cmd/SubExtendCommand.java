package ru.dseymo.libutils.mc.bukkit.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import ru.dseymo.libutils.mc.bukkit.Chat;
import ru.dseymo.libutils.mc.core.cmd.Args;

@Getter
public class SubExtendCommand extends Command implements TabCompleter {
	private HashMap<String, SubExtendCommand> subCMDs = new HashMap<>();
	private String descr;
	@Setter
	protected boolean showNotAvaibleCMD = true;
	
	public SubExtendCommand(String cmd, boolean onlyPlayer, String perm, String descr) {
		super(cmd, onlyPlayer, perm);
		
		this.descr = descr;
	}
	
	public void addSubCMD(SubExtendCommand cmd) {
		subCMDs.put(cmd.getCmd(), cmd);
	}
	
	public ArrayList<String> getHelp(CommandSender sender) {
		ArrayList<String> help = new ArrayList<>();
		
		if(!showNotAvaibleCMD && !checkPerm(sender))
			return help;
		else if(!subCMDs.isEmpty())
			for(SubExtendCommand subCMD: subCMDs.values())
				for(String str: subCMD.getHelp(sender))
					help.add(getCmd() + " " + str);
		else
			help.add(getCmd() + " " + descr);
		
		return help;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] _args) {
		if(isOnlyPlayer() && !(sender instanceof Player)) {
			cmdOnlyForPlayer(sender);
			return false;
		}
		
		Args args = new Args(_args);
		
		if(checkPerm(sender))
			if(subCMDs.containsKey(args.get(0)))
				return subCMDs.get(args.get(0)).onCommand(sender, cmd, label, args.copyOf(1).toArray());
			else
				return execute(sender, args);
		else
			Chat.NO_PERM.send(sender);
		
		return false;
	}
	
	@Override
	public boolean execute(CommandSender sender, Args args) {return false;}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command _cmd, String alias, String[] args) {
		return onTabComplete(sender, new Args(args));
	}
	
	public List<String> onTabComplete(CommandSender sender, Args args) {
		if(!showNotAvaibleCMD && !checkPerm(sender))
			return new ArrayList<>();
		else if(args.size() == 0 || !subCMDs.containsKey(args.get(0))) {
			ArrayList<String> tab = new ArrayList<>();
			
			for(Entry<String, SubExtendCommand> cmd: subCMDs.entrySet())
				if(showNotAvaibleCMD || cmd.getValue().checkPerm(sender))
					tab.add(cmd.getKey());
			
			if(tab.size() == 1)
				tab.add("");
			
			return tab;
		} else
			return subCMDs.get(args.get(0)).onTabComplete(sender, args.copyOf(1));
	}
	
}
