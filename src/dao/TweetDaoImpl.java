package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import refined.TTable;
import database.MySqlDataBase;

public class TweetDaoImpl extends BaseDaoImpl implements ITweetDAO {
	

	@SuppressWarnings("finally")
	@Override
	public long save(String tableName, String fieldName, String value) {
		String sql = "insert into " + tableName + "(`" + fieldName
				+ "`) values (?);";
		PreparedStatement psmt = null;
		ResultSet rs=null;
		long id = 0l;
		try {
//			System.out.println(sql);
			psmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			psmt.setString(1, value);
			psmt.executeUpdate();

			rs = psmt.getGeneratedKeys();
			rs.next();
			id = rs.getLong(1);
			rs.close();
			psmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}
			if (psmt != null) {
				try {
					psmt.close();
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}
			return id;
		}
	}

	@Override
	public void save(TTable ttable) {
		String sql = "insert into ttable(`tid`,`uid`,`lid`,`did`,`iid`,`alist`) values (?,?,?,?,?,?);";
		PreparedStatement psmt = null;
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setLong(1, ttable.getTid());
			psmt.setLong(2, ttable.getUid());
			psmt.setLong(3, ttable.getLid());
			psmt.setLong(4, ttable.getDid());
			psmt.setLong(5, ttable.getIid());
			psmt.setString(6, ttable.getAString());
			psmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if (psmt != null) {
				try {
					psmt.close();
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public long saveOrNot(String tableName, String fieldName, String value) {
		long key = query(tableName,fieldName,value);
		if (key == 0){
			key = save(tableName,fieldName,value);
		}
		return key;
	}
	
	@SuppressWarnings("finally")
	public long query(String tableName, String fieldName, String value) {
		String sql = "select id from "+tableName+" where "+fieldName+" = ?;";
		PreparedStatement psmt = null;
		ResultSet rs=null;
		long id = 0l;
		try {
//			System.out.println(sql);
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, value);
			rs = psmt.executeQuery();
			if(rs.next()){
				id = rs.getLong(1);
			}
			rs.close();
			psmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}
			if (psmt != null) {
				try {
					psmt.close();
				} catch (final SQLException e) {
					e.printStackTrace();
				}
			}
			return id;
		}
	}

	

}
