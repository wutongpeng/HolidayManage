package com.vito.holiday.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * @Title: DbHelper.java
 * @Description: 统一查询类 。预编译，通过?赋值方式可以防止漏洞注入方式，保证安全性。
 * @author wutp
 * @version 1.0
 * @time 2016-8-17下午4:37:39
 */
public class DbUtils {
	private static Connection conn=null;
	private static PreparedStatement ps=null;
	private static ResultSet rs=null;   
	private static Statement stmt = null;
	
	private DbUtils(){
	
	}	
	/**
	 * @Description: 
	 * @param sql
	 * @return PreparedStatement
	 * @throws Exception 
	 */
	public static PreparedStatement getPreparedStatement(Connection conn,String sql) 
			throws Exception{
		try {
			ps=conn.prepareStatement(sql);
			System.out.println("编译sql: " + sql );
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;			
		}finally{
			ConnectionUtil.BackPreparedStatement(conn,ps, null);			
		}
		return ps;
	}

	/**
	 * @Description:
	 * @auther: wutongpeng 2016年9月25日 
	 * @return
	 * @throws Exception 
	 */
	public static Statement getStatement(Connection conn) throws Exception{
		try {
			stmt=conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;			
		}finally{
			ConnectionUtil.BackPreparedStatement(conn,ps, null);			
		}
		return stmt;
	}
	
	/**
	 * @Description:返回表
	 * @param sql
	 * @param params	
	 * @return ResultSet
	 * @throws Exception 
	 */
	public static ResultSet getResultSet(Connection conn,String sql,String []params) 
			throws Exception{
		try {
			ps=conn.prepareStatement(sql);
			for(int i=0;i<params.length;i++)			
				ps.setString(i+1, params[i]);			
			rs=ps.executeQuery();
			System.out.println("执行sql: " + sql );
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionUtil.BackPreparedStatement(conn,ps, rs);
		}
		return rs;
	}
	
	/**
	 * @Description:返回表
	 * @param sql
	 * @param params	
	 * @return ResultSet
	 * @throws SQLException 
	 */
	public static ResultSet getResultSet2(Connection conn,String sql,String []params) 
			throws SQLException{
		try {
			ps=conn.prepareStatement(sql);
			for(int i=0;i<params.length;i++)			
				ps.setString(i+1, params[i]);			
			rs=ps.executeQuery();
			System.out.println("执行sql: " + sql );
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			//ConnectionUtil.BackPreparedStatement(conn,ps, rs);
		}
		return rs;
	}
	/**
	 * @Description:返回表
	 * @param sql
	 * @param params	
	 * @return ResultSet
	 * @throws SQLException 
	 */
	public static ResultSet getResultSet3(Connection conn,String sql,Object[] params) 
			throws SQLException{
		try {
			ps=conn.prepareStatement(sql);
			for(int i=0;i<params.length;i++)			
				ps.setObject(i+1, params[i]);			
			rs=ps.executeQuery();
			System.out.println("执行sql: " + sql );
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			//ConnectionUtil.BackPreparedStatement(conn,ps, rs);
		}
		return rs;
	}
	/**
	 * @Description:查看有多少记录
	 * @param sql
	 * @return
	 * @return int
	 * @throws Exception 
	 */
	public static int getExecuteCount(Connection conn,String sql,String []params) throws Exception{
		int sum=0;
		try {
			ps=conn.prepareStatement(sql);
			for(int i=0;i<params.length;i++)
				ps.setString(i+1, params[i]);
			rs=ps.executeQuery();
			System.out.println("执行sql: " + sql );
			if(rs.next())
				sum=rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionUtil.BackPreparedStatement(conn,ps, rs);
		}
		return sum;
	}
	
	/**
	 * @Description:获得最大编号
	 * @param sql
	 * @return
	 * @return String
	 * @throws Exception 
	 */
	@Deprecated
	public static String getMaxCount(String sql,String value) throws Exception
	{
		String max="0";
		try {
			ps=conn.prepareStatement(sql);			
			rs=ps.executeQuery();
			System.out.println("执行sql: " + sql );
			if(rs.next())
				max=rs.getString(value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionUtil.BackPreparedStatement(conn,ps, rs);
		}
		return max;
	}


	/**
	 * @Description:查找是否存指定信息
	 * @param sql
	 * @param params
	 * @return String
	 * @throws Exception 
	 */
	public static boolean executeQuery(Connection conn,String sql,String []params) throws Exception
	{		
		boolean confInfo = false;
		try {
			ps=conn.prepareStatement(sql);			
			for(int i=0;i<params.length;i++){
				System.out.println("参数为：" + params[i]);
 				ps.setString(i+1, params[i]);
 			}
			rs=ps.executeQuery();
			System.out.println("执行sql: " + sql );
			if(rs.next() && rs.getInt(1) >= 1)
				confInfo = true;			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;		
		} finally {
			ConnectionUtil.BackPreparedStatement(conn,ps, rs);		
		}
		return confInfo;		
	}


	/**
	 * @Description:增删改
	 * @param sql
	 * @param params
	 * @return boolean
	 * @throws Exception 
	 */
	public static boolean execute(Connection conn,String sql,String []params) throws Exception
	{
		boolean confInfo = false;
		try {
			ps=conn.prepareStatement(sql);
			for(int i=0;i<params.length;i++)
				ps.setString(i+1, params[i]);
			System.out.println("执行sql: " + sql );
			if(ps.executeUpdate()==1)
				confInfo = true;					
			} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionUtil.BackPreparedStatement(conn,ps, null);
		}
		return confInfo;		
	}
	
	/**
	 * @Description:显示表
	 * @auther: wutp 2016年10月17日
	 * @param conn
	 * @param sql
	 * @param params
	 * @return
	 * @return Vector[]
	 * @throws Exception 
	 */
	public static Vector[] query(Connection conn,String sql, String[] params) 
			throws Exception{
		// 初始化
		Vector[] data = new Vector[2];
		Vector<String> colums = new Vector<String>();
		Vector<Vector> rows = new Vector<Vector>();
		// Vector[2] = new Vector[2];
		// this.colums.add("员工号");
		// this.colums.add("姓名");
		// this.colums.add("性别");
		// this.colums.add("职位");			
		try {
			ResultSet rs = getResultSet(conn,sql, params);
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				colums.add(rsmd.getColumnName(i + 1));
			}
			while (rs.next()) {
				Vector<String> temp = new Vector<String>();
				for (int i = 0; i < rsmd.getColumnCount(); i++) {
					temp.add(rs.getString(i + 1));
				}
				rows.add(temp);
				// temp.add(rs.getString(1));
				// temp.add(rs.getString(2));
				// temp.add(rs.getString(3));
				// temp.add(rs.getString(4));
			}
			data[0] = colums;
			data[1] = rows;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionUtil.BackPreparedStatement(conn,ps, rs);
		}
		return data;
	}
	/**
	 * @Description:获取结果集，并将结果放在List中 
	 * @auther: wutp 2016年12月4日
	 * @param sql
	 * @param params
	 * @return
	 * @return List<Object>
	 * @throws Exception 
	 */
	public static List<Object> excuteQuery(Connection conn ,String sql, Object[] params) 
			throws Exception {  
	    ResultSet rs = getResultSet3(conn,sql, params);  
	    ResultSetMetaData rsmd = null;            
	    // 结果集列数  
	    int columnCount = 0;  
	    try {  
	        rsmd = rs.getMetaData();               
	        // 获得结果集列数  
	        columnCount = rsmd.getColumnCount();  
	    } catch (SQLException e1) {  
	        System.out.println(e1.getMessage());  
	    }   
	    List<Object> list = new ArrayList<Object>();   
	    try {  
	        while (rs.next()) {  
	            Map<String, Object> map = new HashMap<String, Object>();  
	            for (int i = 1; i <= columnCount; i++) {  
	                map.put(rsmd.getColumnLabel(i), rs.getObject(i));  
	            }  
	            list.add(map);  
	        }  
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	throw e;          
	    } finally {  
	    	ConnectionUtil.BackPreparedStatement(conn,ps, rs); 
	    }  	
	    return list;  
	}
	/**
	 * @Description:
	 * @auther: wutp 2016年10月23日
	 * @param rs
	 * @return
	 * @throws java.sql.SQLException
	 * @return List
	 */
	public static List<Map<String, Object>> resultSetToList(ResultSet rs) throws java.sql.SQLException {
		if (rs == null)
			return Collections.emptyList();
		ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
		int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> rowData = null;
		while (rs.next()) {
			rowData = new HashMap<String, Object>(columnCount);
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);			
		}
		System.out.println("list:" + list.toString());
		return list;
	}
	
	/**
	 * @Description:
	 * @auther: wutp 2016年10月23日
	 * @param rs
	 * @return
	 * @return List
	 * @throws Exception 
	 */
	/*public static List<GroupTable> resultSetToGroupTables(ResultSet rs) throws java.sql.SQLException {
		List<GroupTable> groups = new ArrayList<GroupTable>();
		GroupTable group = null;
		if (rs == null)
			return Collections.emptyList();
		ResultSetMetaData md = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
		int columnCount = md.getColumnCount(); // 返回此 ResultSet 对象中的列数
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> rowData = null;
		while (rs.next()) {
			rowData = new HashMap<String, Object>(columnCount);
			group = new GroupTable();
			for (int i = 1; i <= columnCount; i++) {
				//group.se
				rowData.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(rowData);			
		}
		System.out.println("list:" + list.toString());
		return list;
	}*/
	@Deprecated
	public static void BackPreparedStatement(Connection conn,PreparedStatement stmt,
			ResultSet rs)throws Exception {	
		try {
			if(stmt == null && rs == null)
				ConnectionUtil.BackPreparedStatement(conn);
			else if(rs == null)
				ConnectionUtil.BackPreparedStatement(conn,stmt);
			else
				ConnectionUtil.BackPreparedStatement(conn,stmt,rs);
			System.out.println("关闭数据库连接！");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
