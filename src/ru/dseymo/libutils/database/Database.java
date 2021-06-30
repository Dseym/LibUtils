package ru.dseymo.libutils.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.debug.LogMessage;
import ru.dseymo.libutils.debug.LogMessage.LogType;

public final class Database {
	private static final HashMap<String, Database> DBs = new HashMap<>();
	
	public static Database getDB(String host, String login, String pass, String name, int maxConns) {
		String key = host + ":" + name;
		
		if(!DBs.containsKey(key))
			DBs.put(key, new Database(key, host, login, pass, name, maxConns));
		
		Database db = DBs.get(key);
		
		db.links++;
		
		return db;
	}
	
	
	private String key;
	private BoneCP pool;
	private int links = 0;
	
	private Database(String key, String host, String login, String pass, String name, int maxConns) {
		this.key = key;
		BoneCPConfig config = new BoneCPConfig();
		
		config.setJdbcUrl("jdbc:mysql://" + host + "/" + name);
		config.setUsername(login); 
		config.setPassword(pass);
		config.setMinConnectionsPerPartition(1);
		config.setMaxConnectionsPerPartition(maxConns);
		config.setPartitionCount(3);
		
		try {
			pool = new BoneCP(config);
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	public void remove() {
		DBs.remove(key);
		
		pool.shutdown();
		
		links = 0;
	}
	
	
	public void close() {
		if(--links < 1)
			remove();
	}
	
	
	public Connection getConnect() throws SQLException {
		Debug.message(LogMessage.create("SQL", LogType.INFO, "DB: Getting connection"), false);
		
		return pool.getConnection();
	}
	
}
