package dao;

import java.sql.Connection;
import java.sql.SQLException;

import database.MySqlDataBase;

public class BaseDaoImpl {
	protected Connection conn =null;
	
	public BaseDaoImpl(){
		conn = MySqlDataBase.getConnection();
	}
	
	
	public void release() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		MySqlDataBase.release();
	}
}
