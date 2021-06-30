package ru.dseymo.libutils.debug;

import lombok.Getter;

@Getter
public class LogMessage {
	public enum LogType {
		ERROR, WARNING, INFO;
	}
	
	public static LogMessage create(String key, LogType type, String...text) {
		LogMessage log = new LogMessage();
		
		log.key = key;
		log.text = text;
		log.type = type;
		
		return log;
	}
	
	
	private String key;
	private String[] text;
	private LogType type;
	
}
