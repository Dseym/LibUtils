package ru.dseymo.libutils.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import lombok.Getter;
import lombok.Setter;

public abstract class SQLObject {
	@Getter @Setter
	private int id = 0;
	
	public SQLObject() {}
		
	public SQLObject(ResultSet rs) throws SQLException {
		id = rs.getInt("id");
		
		load(rs);
	}
	
	public abstract void load(ResultSet rs) throws SQLException;
	
	public abstract void save();
	
	public abstract HashMap<String, Object> saveNow(boolean isUpdate);
	
	public abstract void delete();
	
}
