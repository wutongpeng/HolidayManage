package com.vito.holiday.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * @Description: 
 * @author wutp 2016年10月17日
 * @version 1.0
 */
public class ConnectionUtil {

	
	
	private static String driver;
	private static String url;
	private static String user;
	private static String pwd;
	
	private ConnectionUtil()throws Exception{
		
	}
	
	
	/**
	 * @Description:
	 * @auther: wutp 2016年10月17日
	 * @return
	 * @throws Exception
	 * @return Connection
	 */
	public static Connection getConnection(){
		Connection con = null;	
		try {
			initParameter();
			Class.forName(driver);			
			con=DriverManager.getConnection(url,user,pwd);
		} catch (Exception e) {			
			e.printStackTrace();
			//throw e;
		}
		return con;
	}		
    
	
	private static void initParameter() {
		Properties prop = new Properties();
		InputStream in = Object.class.getResourceAsStream("/Properties.properties");   	
		try {    		
			if(in == null)
				throw new Exception("InputStream 读取失败！");
			
			prop.load(in);
			driver = prop.getProperty("driver").trim();
			url = prop.getProperty("url").trim();	
			user = prop.getProperty("user").trim();
			pwd = prop.getProperty("pwd").trim();    		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void BackPreparedStatement(Connection conn) throws Exception{
		if(conn ==null)
			return;
		try {			
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
  
	public static void BackPreparedStatement(Connection conn,PreparedStatement stmt)
			throws Exception {
		if (stmt == null)
			return;
		try {
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		if (conn == null)
			return;
		try {
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


	/**
	 * @Description:
	 * @auther: wutp 2016年10月17日
	 * @param conn
	 * @param stmt
	 * @param rs
	 * @return void
	 * @throws Exception 
	 */
	public static void BackPreparedStatement(PreparedStatement stmt,
			ResultSet rs)throws Exception {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	
		if (stmt == null)
			return;
		try {
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}


	/**
	 * @Description:
	 * @auther: wutp 2016年10月17日
	 * @param conn
	 * @param stmt
	 * @param rs
	 * @return void
	 * @throws Exception 
	 */
	public static void BackPreparedStatement(Connection conn,PreparedStatement stmt,
			ResultSet rs)throws Exception {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
	
		if (stmt == null)
			return;
		try {
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		if (conn == null)
			return;
		try {
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
}
