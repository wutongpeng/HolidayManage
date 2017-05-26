package com.vito.holiday.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.vito.holiday.service.HolidayService;
import com.vito.holiday.util.JResponseObject;

/*import jfoundation.bridge.classes.JResponseObject;
import jfoundation.dataobject.classes.JParamObject;
import jframework.foundation.classes.JActiveDComDM;*/


import com.vito.holiday.util.LunarFunction;
import com.vito.holiday.util.StringFunction;

/**
 * @Description: 节假日管理-日对象
 * @author wutp 2017年5月15日
 * @version 1.0
 */
public class JDayPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	BorderLayout borderLayout = new BorderLayout();
	JPanel northPanel;
	JPanel centerPanel;
	JPanel sourthPanel;
	JPanel eastPanel;
	JPanel westPanel;
	
	private JLabel gregorianLabel;
	private JLabel lunarLabel;
	private JLabel statusLabel;
	private JPanel statusPanel;
	private MyMouseListener mouseListener = new MyMouseListener(); 
	
	private JMonthPanel jMonthPanel;
	
	/**
	 * 在月单元格的位置
	 */
	private int dayId;
	
	/**
	 * 是周几
	 */
	private String weekId ;
	
	/**
	 * 是否是周末 true:是;false:否。
	 */
	private boolean week;
	
	/**
	 * 是否是节假日，0：上班，1：休 。
	 */
	private String holiday ;
	
	/**
	 * 阳历
	 */
	private String gregorianDay;
	
	/**
	 * 农历
	 */
	private String chinaDayString;
	
	public JDayPanel(JMonthPanel jMonthPanel, int i,String holiday) {
		super();
		this.jMonthPanel = jMonthPanel;
		this.dayId = i;
		this.holiday = holiday;
		calculationData();
		initUI();
	}
	
	private void calculationData(){
		this.weekId = weekId(this.dayId % 7);
		this.week = isWeek();
		this.gregorianDay = jMonthPanel.getDays()[dayId] ==null ? "" : jMonthPanel.getDays()[dayId];
		if(!"".equals(gregorianDay)){
			try {
				this.chinaDayString =  LunarFunction.getLunarDayString(jMonthPanel.getDate()+StringFunction.fillBeforeValue(gregorianDay)+"日");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	private String weekId(int i){
		return JMonthPanel.week[i];
	}

	private boolean isWeek(){
		if("六".equals(weekId) || "日".equals(weekId)){
			return true;
		}
		return false;
	}

	private void initUI() {
		this.setLayout(borderLayout);
		this.setBorder(new EtchedBorder(5));
		
		northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		northPanel.setBackground(Color.white);
		centerPanel  = new JPanel(new BorderLayout());
		centerPanel.setBackground(Color.white);
		sourthPanel = new JPanel();
		sourthPanel.setBackground(Color.white);
		eastPanel  = new JPanel();
		eastPanel.setBackground(Color.white);
		westPanel = new JPanel();
		westPanel.setBackground(Color.white);
		
		this.add(northPanel,BorderLayout.NORTH);
		this.add(centerPanel,BorderLayout.CENTER);
		this.add(sourthPanel,BorderLayout.SOUTH);
		this.add(eastPanel,BorderLayout.EAST);
		this.add(westPanel,BorderLayout.WEST);
		
		if( !"".equals(gregorianDay)){
			statusLabel = new JLabel();
			statusLabel.setFont(new Font("TimesRoman",Font.PLAIN,15));
			statusLabel.setVerticalAlignment(JLabel.CENTER);
			statusLabel.setHorizontalAlignment(JLabel.CENTER);
			statusLabel.setForeground(Color.white);	

			statusPanel = new JPanel(); 
			statusPanel.setPreferredSize(new Dimension(20,20));
			statusPanel.setSize(new Dimension(20,20));
			statusPanel.setBackground(Color.white);
			if( this.holiday.equals("1") ){
				statusLabel.setText("休");
				statusPanel.setBackground(Color.red);
			}else if( week && this.holiday.equals("0") ){
				statusLabel.setText("班");
				statusPanel.setBackground(Color.black);
			}else if( !week && this.holiday.equals("0") ){
				statusLabel.setText(" ");
				statusPanel.setBackground(Color.white);
			}
			
			statusPanel.add(statusLabel,BorderLayout.CENTER);
			northPanel.add(statusPanel);
			
			gregorianLabel = new JLabel();
			gregorianLabel.enableInputMethods(true);
			gregorianLabel.setHorizontalAlignment(JLabel.CENTER);
			if(week){
				gregorianLabel.setForeground(Color.red);
			}
			gregorianLabel.setText(gregorianDay);
			
			lunarLabel = new JLabel();
			lunarLabel.enableInputMethods(true);
			lunarLabel.setHorizontalAlignment(JLabel.CENTER);
			lunarLabel.setText(chinaDayString);
			
			centerPanel.add(gregorianLabel,BorderLayout.CENTER);
			centerPanel.add(lunarLabel,BorderLayout.SOUTH);
		}
		this.addMouseListener(mouseListener);
		this.setVisible(true);
		
	}
	
	private boolean updateHolidays(String holiday) {
		Map<String,Object> PO = new HashMap<String, Object>();
		PO.put("F_YEAR", String.valueOf(jMonthPanel.holidayManageDlg.getYear()));
		PO.put("F_MONTH", StringFunction.fillBeforeValue(
				String.valueOf(jMonthPanel.holidayManageDlg.getMonth())));
		PO.put("F_VDATE", String.valueOf(jMonthPanel.holidayManageDlg.getYear())
				+StringFunction.fillBeforeValue(String.valueOf(jMonthPanel.holidayManageDlg.getMonth()))
				+StringFunction.fillBeforeValue(this.gregorianDay));
		PO.put("F_JJR", holiday);
		JResponseObject RO = null;
		try {
			RO = (JResponseObject) HolidayService.updateHoliday(PO,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (RO == null) {
			JOptionPane.showMessageDialog(this, "保存错误！\r\n原因：RO为空", "提示",
					JOptionPane.ERROR_MESSAGE);
		} else if (RO.ErrorCode == 1) {
			JOptionPane.showMessageDialog(this, "保存成功！");
			return true;
		} else {
			JOptionPane.showMessageDialog(this, "保存错误！\r\n原因："
					+ RO.ErrorString, "提示", JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}
	
	class MyMouseListener extends MouseAdapter {
		
		public MyMouseListener(){
			super();
		}
		
		public void mouseClicked(MouseEvent evt) {
			if(evt.isMetaDown() || evt.getClickCount() == 2){
				System.out.println("changeHoliday()");
				changeHoliday();
			}
		}
	}
	
/*	*/
	/**
	 * 
	 * @Description:
	holiday:0 1
	week:y n
	0y 班        1 休
	0n ""  1 休
	1y 休        0 班
	1n 休        0 ""
	 * @auther: wutp 2017年5月19日
	 * @return void
	 */
	private void changeHoliday(){
		String temHoliday ;
		if( this.holiday.equals("0") ){
			temHoliday = "1";
			if(updateHolidays(temHoliday)){
				statusLabel.setText("休");
				statusPanel.setBackground(Color.red);
				this.holiday = temHoliday;
			}
		}else if( this.holiday.equals("1") && week ){
			temHoliday = "0";
			if(updateHolidays(temHoliday)){
				statusLabel.setText("班");
				statusPanel.setBackground(Color.black);
				this.holiday = temHoliday;
			}
		}else if( this.holiday.equals("1") && !week ){
			temHoliday = "0";
			if(updateHolidays(temHoliday)){
				statusLabel.setText(" ");
				statusPanel.setBackground(Color.white);
				this.holiday = temHoliday;
			}
		}
	}
	
}
