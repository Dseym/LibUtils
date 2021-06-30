package ru.dseymo.libutils.mc.bungee.cmd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.TabExecutor;
import ru.dseymo.libutils.mc.bungee.Chat;
import ru.dseymo.libutils.mc.core.cmd.Args;

@Getter
public class SubExtendCommand extends Command implements TabExecutor {
	private HashMap<String, SubExtendCommand> subCMDs = new HashMap<>();
	private String descr;
	@Setter
	protected boolean showNotAvaibleCMD = true;
	
	public SubExtendCommand(String cmd, String perm, String descr, String...aliases) {
		super(cmd, perm, aliases);
		
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
	public void execute(CommandSender sender, String[] _args) {
		Args args = new Args(_args);
		
		if(checkPerm(sender))
			if(subCMDs.containsKey(args.get(0)))
				subCMDs.get(args.get(0)).execute(sender, args.copyOf(1).toArray());
			else
				execute(sender, args);
		else
			Chat.NO_PERM.send(sender);
	}
	
	@Override
	public void execute(CommandSender sender, Args args) {}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
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
	
	protected boolean checkPerm(CommandSender sender) {
		return getPerm().isEmpty() || sender.hasPermission(getPerm());
	}
	
}
