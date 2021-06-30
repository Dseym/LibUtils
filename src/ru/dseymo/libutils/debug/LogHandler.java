package ru.dseymo.libutils.debug;

public interface LogHandler {
	public void newMessage(LogMessage message);
	
	public void newLogInFile(LogMessage message);
}
