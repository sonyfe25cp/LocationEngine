package dao;

import refined.TTable;

public interface ITweetDAO {
	
	public long save(String tableName,String fieldName,String value);
	
	public long saveOrNot(String tableName,String fieldName,String value);
	
	public void save(TTable ttable);
	
}
