package ru.dseymo.libutils.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import ru.dseymo.libutils.database.Database;
import ru.dseymo.libutils.database.SQLObject;
import ru.dseymo.libutils.database.SQLUtil;
import ru.dseymo.libutils.debug.Debug;
import ru.dseymo.libutils.debug.LogMessage;
import ru.dseymo.libutils.debug.LogMessage.LogType;

public abstract class SQLStorage<K, V extends SQLObject> implements Storage<K, V> {
	
	private Database db;
	protected String table;
	protected HashMap<K, V> data = new HashMap<>();
	
	public SQLStorage(Database database, String table) {
		db = database;
		this.table = table;
		
		try(Connection c = getConnection()) {
			init(c);
			
			Debug.message(LogMessage.create("SQL", LogType.INFO, "Initilization:" + table + ": Done"), true);
		} catch (Exception e) {
			Debug.message(LogMessage.create("SQL", LogType.ERROR, "Initilization:" + table + ": " + e.getMessage()), true);
			
			e.printStackTrace();
		}
	}
	
	public abstract void init(Connection c) throws SQLException;
	
	
	protected Connection getConnection() throws SQLException {
		return db.getConnect();
	}
	
	@Override
	public void set(K key, V value, boolean useCache) {
		if(useCache)
			data.put(key, value);
		
		boolean isUpdate = false;
		
		try(Connection c = getConnection()) {
			if(value.getId() == 0) {
				HashMap<String, Object> values = value.saveNow(false);
				
				isUpdate = false;
				
				if(values != null && !values.isEmpty())
					value.setId(insert(c, values));
			} else {
				HashMap<String, Object> values = value.saveNow(true);
				
				isUpdate = true;
				
				if(values != null && !values.isEmpty())
					update(c, value.getId(), values);
			}
		} catch (Exception e) {
			Debug.message(LogMessage.create("SQL", LogType.ERROR, "SQL:" + key.toString() + ":ID" + value.getId() + ":" + value.saveNow(isUpdate).toString() + ": " + e.getMessage()), true);
			
			e.printStackTrace();
		}
	}
	
	protected int insert(Connection c, HashMap<String, Object> values) throws SQLException {
		StringBuilder sql = new StringBuilder("INSERT INTO ");
		sql.append(table)
		   .append(" (");
		for(String key: values.keySet())
			sql.append(key).append(",");
		sql.deleteCharAt(sql.length()-1);
		sql.append(")");
		
		sql.append(" VALUES(");
		for(int i = 0; i < values.size(); i++)
			sql.append("?,");
		sql.deleteCharAt(sql.length()-1);
		sql.append(")");
		
		PreparedStatement stmt = c.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
		int i = 1;
		for(Object obj: values.values())
			stmt.setObject(i++, obj);
		
		stmt.execute();
		
		Debug.message(LogMessage.create("SQL", LogType.INFO, "SQL:" + sql), true);
		
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		return rs.getInt(1);
	}
	
	protected void update(Connection c, int id, HashMap<String, Object> values) throws SQLException {
		StringBuilder sql = new StringBuilder("UPDATE ");
		sql.append(table)
		   .append(" SET ");
		for(String key: values.keySet()) {
			sql.append(key).append("=?,");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(" WHERE id=?");
		
		PreparedStatement stmt = c.prepareStatement(sql.toString());
		int i = 1;
		for(Object obj: values.values())
			stmt.setObject(i++, obj);
		stmt.setInt(i, id);
		
		stmt.executeUpdate();
		
		Debug.message(LogMessage.create("SQL", LogType.INFO, "SQL:" + sql), true);
	}
	
	@Override
	public Iterator<Entry<K, V>> iterator() {
		return data.entrySet().iterator();
	}
	
	public void unload(K key) {
		if(contains(key, true))
			data.remove(key);
	}
	
	public List<K> keys() {
		return new ArrayList<>(data.keySet());
	}
	
	public List<V> values() {
		return new ArrayList<>(data.values());
	}
	
	@Override
	public void close() {
		for(K key: new ArrayList<>(data.keySet()))
			unload(key);
	}

	@Override
	public void clear() {
		data.clear();
		try(Connection c = getConnection()) {
			SQLUtil.clearTable(c, table);
		} catch (Exception e) {
			Debug.message(LogMessage.create("SQL", LogType.ERROR, "Clear:" + table + ": " + e.getMessage()), true);
			
			e.printStackTrace();
		}
	}

}
