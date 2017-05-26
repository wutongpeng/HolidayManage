package com.vito.holiday.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.vito.holiday.dao.HolidayManageDao;
import com.vito.holiday.util.ConnectionUtil;
import com.vito.holiday.util.JResponseObject;

public class HolidayService {
	/**
	 * @throws Exception 
	 * @Description: 批量保存节假日信息
	 * @auther: wutp 2017年5月17日
	 */
	public static JResponseObject syncHoliday(Object Param, Object Data) {
		JResponseObject RO = null;
		Connection conn = null;
		@SuppressWarnings("unchecked")
		Map<String,Object> po = (Map<String, Object>) Param;
		try {
			@SuppressWarnings("unchecked")
			ArrayList<String[]> list = (ArrayList<String[]>) Data;
			conn = ConnectionUtil.getConnection();
			HolidayManageDao.syncHoliday(po,conn,list);
			RO = new JResponseObject("同步成功！", 1,"");
		} catch (Exception pExt) {
			pExt.printStackTrace();
			RO = new JResponseObject("", -1, pExt.getMessage());
			return RO;
		} finally {
			try {
				ConnectionUtil.BackPreparedStatement(conn);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return RO;
	}
	
	/**
	 * @throws Exception 
	 * @Description: 查询某个月的节假日信息
	 * @auther: wutp 2017年5月17日
	 */
	public static Object listHoliday(Object Param, Object Data) {
		JResponseObject RO = null;
		Connection conn = null;
		@SuppressWarnings("unchecked")
		Map<String,Object> po = (Map<String, Object>) Param;
		try {
			conn = ConnectionUtil.getConnection();
			HashMap<String,String> map = HolidayManageDao.listHoliday(po,conn);
			RO = new JResponseObject(map, 1,"");
		} catch (Exception pExt) {
			pExt.printStackTrace();
			RO = new JResponseObject("", -1, pExt.getMessage());
			return RO;
		} finally {
			try {
				ConnectionUtil.BackPreparedStatement(conn);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return RO;
	}
	
	/**
	 * @throws Exception 
	 * @Description: 增加或修改一条节假日信息
	 * @auther: wutp 2017年5月17日
	 */
	public static Object updateHoliday(Object Param, Object Data) {
		JResponseObject RO = null;
		Connection conn = null;
		@SuppressWarnings("unchecked")
		Map<String,Object> po = (Map<String, Object>) Param;
		try {
			conn = ConnectionUtil.getConnection();
			HolidayManageDao.updateHoliday(po,conn);
			RO = new JResponseObject("", 1,"");
		} catch (Exception pExt) {
			pExt.printStackTrace();
			RO = new JResponseObject("", -1, pExt.getMessage());
			return RO;
		} finally {
			try {
				ConnectionUtil.BackPreparedStatement(conn);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return RO;
	}

	public static JResponseObject getCountBySql(Object Param, Object Data) {
		JResponseObject RO = null;
		Connection conn = null;
		@SuppressWarnings("unchecked")
		Map<String,Object> po = (Map<String, Object>) Param;
		String[] paramter = (String[]) Data;
		try {
			conn = ConnectionUtil.getConnection();
			String cmdText = (String) po.get("CMDTEXT");
			Integer count = HolidayManageDao.getCountBySql(conn,cmdText,paramter);
			RO = new JResponseObject(count, 1,"");
		} catch (Exception pExt) {
			pExt.printStackTrace();
			RO = new JResponseObject(null, -1, pExt.getMessage());
			return RO;
		} finally {
			try {
				ConnectionUtil.BackPreparedStatement(conn);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return RO;
	}
}
