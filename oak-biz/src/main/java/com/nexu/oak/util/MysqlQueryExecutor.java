package com.nexu.oak.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * 实例类，动态构建 data source，执行对应的sql
 * 用途：目标数据库数据查询
 *
 * @author dongfeng
 * @version $Id: MysqlQueryExecutor.java, v 0.1 2017-1-3 下午1:59:09 dongfeng Exp $
 */
public class MysqlQueryExecutor {
	private static Logger logger=Logger.getLogger(MysqlQueryExecutor.class);
	private static String DTRIVER_NAME = "com.mysql.jdbc.Driver"; 
	private Connection conn;
	
	public MysqlQueryExecutor(String url,String user, String pwd) throws ClassNotFoundException, SQLException{
	    Class.forName(DTRIVER_NAME);
	    conn = DriverManager.getConnection(url, user, pwd); 
	}
	
	public void closeConnection() throws SQLException{
	    if(conn!=null){
	        conn.close();
        }
	}
	
	public ResultSet query(String query){
		
        Statement stmt=null;
        ResultSet rs=null;
        try{
            stmt=conn.createStatement();
            stmt.setQueryTimeout(1);
            rs=stmt.executeQuery(query);
		}catch (Exception e) {
    	    logger.error("query fail", e);
        }
		return rs;
        
	}

}

