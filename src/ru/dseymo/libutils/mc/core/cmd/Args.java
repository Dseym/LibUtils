package ru.dseymo.libutils.mc.core.cmd;

import java.util.Arrays;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class Args {
	private final String[] args;
	
	public String get(int index) {
		if(index+1 > args.length)
			return "";
		
		return args[index];
	}
	
	public Integer getInt(int index) {
		String arg = get(index);
		
		if(arg.isEmpty())
			return null;
		
		try {
			return Integer.parseInt(arg);
		} catch (Exception e) {
			return null;
		}
	}
	
	public Args copyOf(int index) {
		if(get(index).isEmpty())
			return new Args(new String[0]);
		
		return new Args(Arrays.copyOfRange(args, index, args.length));
	}
	
	public int size() {
		return args.length;
	}
	
	public String[] toArray() {
		return args;
	}
	
	@Override
	public String toString() {
		String str = "";
		for(String arg: args)
			str += " " + arg;
		if(!str.isEmpty())
			str = str.substring(1);
		
		return str;
	}
	
}
