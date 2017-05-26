package com.vito.holiday.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

/**
 * @Description: 节假日管理-月对象
 * @author wutp 2017年5月15日
 * @version 1.0
 */
public class JMonthPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String week[] = { "日", "一", "二", "三", "四", "五", "六" };

	GridLayout gridLayout1 ,gridLayout2;
	JPanel northPanel = new JPanel();
	JPanel centerPanel = new JPanel();
	JLabel titleName[] = new JLabel[7]; // 周一到周天的按键
	JDayPanel[] dayPanel = new JDayPanel[42];// 用来输出日历的天的数组
	
	HolidayManageDlg holidayManageDlg;
	private String[] days;
	private String date;
	
	/**
	 * 节假日集合
	 */
	private HashMap<String,String> holidays;
	
	public JMonthPanel(HolidayManageDlg holidayManageDlg, String[] days, String date,HashMap<String,String> holidays) throws Exception{
		super();
		try{
			this.holidayManageDlg = holidayManageDlg;
			this.days = days;
			this.date = date;
			this.holidays = holidays;
			jbInit();
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}

	private void jbInit(){
		this.setLayout(new BorderLayout());
		this.setBorder(new BevelBorder(BevelBorder.LOWERED));
		this.add(northPanel,BorderLayout.NORTH);
		this.add(centerPanel,BorderLayout.CENTER);
		gridLayout1 = new GridLayout(1, 7, 1, 1);
		gridLayout2 = new GridLayout(6, 7, 1, 1);
		northPanel.setLayout(gridLayout1);
		centerPanel.setLayout(gridLayout2);
		centerPanel.setBorder(new EtchedBorder());
		
		for (int i = 0; i < 7; i++) { 
			titleName[i] = new JLabel(week[i]);
			titleName[i].setFont(new Font("TimesRoman",Font.BOLD,20));
			titleName[i].setHorizontalAlignment(JLabel.CENTER);
			titleName[i].setBackground(Color.gray);
			titleName[i].setForeground(Color.black);	
			northPanel.add(titleName[i]);
		}
		
		for (int i = 0; i < 42; i++) {
			dayPanel[i] = new JDayPanel(this,i,getHolidayStatus(days[i]));
			centerPanel.add(dayPanel[i]);
		}
	}
	
	public String[] getDays() {
		return days;
	}

	public String getDate() {
		return date;
	}
	
	public String getHolidayStatus(String key) {
		String value ;
		if(key == null){
			return "0";
		}
		if(key.length() == 1){
			key = "0"+key;
		}
		try{
			value = this.holidays.get(key);
		}catch(Exception e){
			e.printStackTrace();
			value = "0";
		}
		value  = value == null ? "0" : value;
		return value ;
	}
	

}
