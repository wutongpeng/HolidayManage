package com.vito.holiday.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.vito.holiday.util.ConnectionUtil;

/**
 * @Description:节假日管理dao层
 * @author wutp 2017年5月17日
 * @version 1.0
 */
public class HolidayManageDao {

	public static Integer getCountBySql(Connection conn, String sqlCmd,
			String[] paramter) throws Exception {
		int sum = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sqlCmd);
			for (int i = 0; i < paramter.length; i++) {
				ps.setString(i + 1, paramter[i]);
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				sum = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionUtil.BackPreparedStatement(ps,rs);
		}
		return sum;

	}

	public static void syncHoliday(Map<String, Object> PO, Connection conn,
			ArrayList<String[]> list) throws Exception {
		PreparedStatement deletePs = null;
		PreparedStatement ps = null;
		String[] params = null;
		String year = (String) PO.get("F_YEAR"); 
		String deleteSql = (String) PO.get("DELETESQL"); 
		String sql = " insert into PSJJR values(?,?,?,?) ";
		try {
			if (deleteSql != null && !"".equals(deleteSql)) {
				deletePs = conn.prepareStatement(deleteSql);
				deletePs.setString(1, year);
				deletePs.executeUpdate();
			}
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			for (int j = 0; j < list.size(); j++) {
				params = fillParamter((String[]) list.get(j));
				for (int i = 0; i < params.length; i++) {
					ps.setString(i + 1, params[i]);
				}
				ps.addBatch();
			}
			int[] result = ps.executeBatch();
			if (result.length != list.size()) {
				throw new Exception("执行失败！");
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
			throw e;
		} finally {
			ConnectionUtil.BackPreparedStatement(deletePs,null);
			ConnectionUtil.BackPreparedStatement(ps,null);
		}
	}

	private static String[] fillParamter(String[] date) {
		String[] fDate = new String[4];
		fDate[0] = date[0].substring(0, 4);
		fDate[1] = date[0].substring(4, 6);
		fDate[2] = date[0];
		fDate[3] = date[1];
		return fDate;
	}

	public static HashMap<String, String> listHoliday(Map<String, Object> PO,
			Connection conn) throws Exception {
		HashMap<String, String> holidayMap = new HashMap<String, String>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String year = (String) PO.get("F_YEAR");
		String month = (String) PO.get("F_MONTH");

		String sql = "select F_VDATE ,F_JJR from PSJJR "
				+ "where F_YEAR = ?  and F_MONTH = ? ";
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, year);
			ps.setString(2, month);
			rs = ps.executeQuery();
			String key;
			while (rs.next()) {
				key = rs.getString("F_VDATE").substring(6);
				holidayMap.put(key, rs.getString("F_JJR"));
			}
		} catch (SQLException pExt) {
			pExt.printStackTrace();
			throw pExt;
		} finally {
			ConnectionUtil.BackPreparedStatement(ps,rs);
		}
		return holidayMap;
	}

	@SuppressWarnings("resource")
	public static void updateHoliday(Map<String, Object> PO, Connection conn)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;

		String year = (String) PO.get("F_YEAR");
		String month = (String) PO.get("F_MONTH");
		String vdate = (String) PO.get("F_VDATE");
		String jjr = (String) PO.get("F_JJR");

		String selectSql = " select 1 from PSJJR where F_VDATE = ? ";
		String updateSql = " update PSJJR set F_JJR = ? where F_VDATE = ? ";

		String insertSql = "insert into PSJJR (F_YEAR,F_MONTH,F_VDATE,F_JJR) values(?,?,?,?)";
		try {
			ps = conn.prepareStatement(selectSql);
			ps.setString(1, vdate);
			rs = ps.executeQuery();
			if (rs.next()) {
				ps = conn.prepareStatement(updateSql);
				ps.clearParameters();
				ps.setString(1, jjr);
				ps.setString(2, vdate);
				ps.executeUpdate();
			} else {
				ps = conn.prepareStatement(insertSql);
				ps.clearParameters();
				ps.setString(1, year);
				ps.setString(2, month);
				ps.setString(3, vdate);
				ps.setString(4, jjr);
				ps.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionUtil.BackPreparedStatement(ps,rs);
		}

	}
}
