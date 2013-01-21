package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

public class MySqlDataBase {
	static Logger logger = Logger.getLogger(MySqlDataBase.class);
	private static Connection conn;

	static String driver = "org.gjt.mm.mysql.Driver";
	static String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/sinaweibo?useUnicode=true&characterEncoding=utf-8";
	static String username = "root";
	static String password = "";

	private MySqlDataBase() {

	}

	@SuppressWarnings("finally")
	public synchronized static Connection getConnection() {
		if (conn != null) {
			return conn;
		} else {
			try {
				Class.forName(driver);
				if (driver == null || jdbcUrl == null || username == null
						|| password == null) {
					logger.error("can't init databasepool, check the file 'database.properties'");
				}
				Class.forName(driver);
				conn = DriverManager.getConnection(jdbcUrl, username, password);
			} catch (ClassNotFoundException e) {
				logger.error("make sure the mysql database driver jar exist!");
			} catch (MySQLSyntaxErrorException e) {
				logger.error("make sure the sinaweibo database exist!");
			} catch (SQLException e) {
				logger.error("check the connection sql!");
			}finally{
				return conn;
			}
		}
	}

	public static void release() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn = null;
		}
	}

	public static void main(String[] args){
		MySqlDataBase.getConnection();
	}
}
