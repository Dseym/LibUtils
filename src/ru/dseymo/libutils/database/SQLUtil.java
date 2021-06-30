package ru.dseymo.libutils.database;

import java.sql.Connection;
import java.sql.SQLException;

import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.debug.LogMessage;
import ru.dseymo.libutils.debug.LogMessage.LogType;

public class SQLUtil {
	
	public static void removeTable(Connection c, String table) throws SQLException {
		String sql = "DROP TABLE `" + table + "`";
		
		c.prepareStatement(sql).execute();
		
		Debug.message(LogMessage.create("SQL", LogType.INFO, "SQL: " + sql), true);
	}
	
	public static void createTable(Connection c, String table, String columns) throws SQLException {
		String sql = "CREATE TABLE `" + table + "` (id INT NOT NULL AUTO_INCREMENT, PRIMARY KEY (id), " + columns + ")";
		
		c.prepareStatement(sql).execute();
		
		Debug.message(LogMessage.create("SQL", LogType.INFO, "SQL: " + sql), true);
	}
	
	public static void clearTable(Connection c, String table) throws SQLException {
		String sql = "TRUNCATE TABLE `" + table + "`";
		
		c.prepareStatement(sql).execute();
		
		Debug.message(LogMessage.create("SQL", LogType.INFO, "SQL: " + sql), true);
	}
	
	public static boolean existsTable(Connection c, String table) throws SQLException {
		boolean isExist = c.getMetaData().getTables(null, null, table, null).next();
		
		Debug.message(LogMessage.create("SQL", LogType.INFO, "Table:" + table + (isExist ? "Exist" : "Not exist")), false);
		
		return isExist;
	}
	
}
