package com.vito.holiday.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.vito.holiday.service.HolidayService;
import com.vito.holiday.util.CalendarDO;
import com.vito.holiday.util.HolidayWebTool;
import com.vito.holiday.util.JResponseObject;
import com.vito.holiday.util.StringFunction;

/**
 * @Description: 节假日管理-主界面
 * @author wutp 2017年5月15日
 * @version 1.0
 */
public class HolidayManageDlg extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Border border1;
	JPanel northPanel = new JPanel();
	JPanel centerPanel = new JPanel();
	JPanel southPanel = new JPanel();
	JPanel pNorth1 = new JPanel();
	JPanel pNorth2 = new JPanel();
	JPanel pNorth3 = new JPanel();
	JComboBox<String> yearComboBox;
	JComboBox<String> monthComboBox; 
	private boolean yearComboBoxIgnore = false;
	private boolean monthComboBoxIgnore = false;
	JButton previousMonthBtn, nextMonthBtn,  backTodayBtn; 
	JLabel showMessage = new JLabel("", JLabel.CENTER);
	JMonthPanel monthPanel;
	
	private int year = 2017;
	private int month = 5;
	
	private CalendarDO calendarDO;
	private Calendar calendar = Calendar.getInstance();
	private String[] days;
	public String SERVER_TYPE = "";

	public HolidayManageDlg() {		
		try{
			initDate(true);
			jbInit("节假日管理");
			pack();
			openCalendar(true);
			changeComboBox();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jbInit(String title) throws Exception{
		this.setTitle(title);
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().setPreferredSize(new Dimension(600, 600));
		this.getContentPane().setSize(600, 600);
		
		buildMenu();
		
		this.getContentPane().add(northPanel, BorderLayout.NORTH);
		this.getContentPane().add(centerPanel, BorderLayout.CENTER);
		this.getContentPane().add(southPanel, BorderLayout.SOUTH);
		
		border1 = BorderFactory.createLineBorder(new Color(172, 172, 172));
		centerPanel.setBorder(border1);
		centerPanel.setLayout(new BorderLayout());
		
		yearComboBox = new JComboBox<String>();
		yearComboBox.setPreferredSize(new Dimension(70, 22));
		yearComboBox.setMinimumSize(new Dimension(70,23));//设置按钮大小最小值
		
		for(int i=1901;i<2100;i++){
			yearComboBox.addItem(String.valueOf(i));
		}
				
		monthComboBox = new JComboBox<String>();
		monthComboBox.setPreferredSize(new Dimension(50, 22));
		monthComboBox.setMinimumSize(new Dimension(50,23));//设置按钮大小最小值
		String item = "";
		for(int i=1;i<13;i++){
			if(String.valueOf(i).length() == 1){
				item = "0"+i;
			}else{
				item = ""+i;
			}
			if( !"".equals(item) ){
				monthComboBox.addItem(item);
			}
		}
		
		previousMonthBtn = new JButton("上月");
		previousMonthBtn.setMinimumSize(new Dimension(40,23));//设置按钮大小最小值
		previousMonthBtn.setPreferredSize(new Dimension(40,23));//设置按钮大小
		previousMonthBtn.setMargin(new Insets(2,2,2,2));//设置按钮边框和标签之间的空白
		previousMonthBtn.setText("上月");//设置文本
		previousMonthBtn.setMnemonic('P');//设置快捷键
		
		nextMonthBtn = new JButton();
		nextMonthBtn.setMinimumSize(new Dimension(40,23));//设置按钮大小最小值
		nextMonthBtn.setPreferredSize(new Dimension(40,23));//设置按钮大小
		nextMonthBtn.setMargin(new Insets(2,2,2,2));//设置按钮边框和标签之间的空白
		nextMonthBtn.setText("下月");//设置文本
		nextMonthBtn.setMnemonic('N');//设置快捷键
		
		backTodayBtn = new JButton();
		backTodayBtn.setMinimumSize(new Dimension(90,23));//设置按钮大小最小值
		backTodayBtn.setPreferredSize(new Dimension(90,23));//设置按钮大小
		backTodayBtn.setMargin(new Insets(2,2,2,2));//设置按钮边框和标签之间的空白
		backTodayBtn.setText("返回今天");//设置文本
		backTodayBtn.setMnemonic('T');//设置快捷键

		northPanel.setLayout(new GridLayout(1,3,1,1));
	
		northPanel.add(pNorth1);
		northPanel.add(pNorth2);
		northPanel.add(pNorth3);
		
		pNorth1.add(yearComboBox,BorderLayout.WEST);
		
		pNorth2.setLayout(new FlowLayout(FlowLayout.CENTER));
		pNorth2.add(previousMonthBtn);
		pNorth2.add(monthComboBox);
		pNorth2.add(nextMonthBtn);
		
		pNorth3.add(backTodayBtn,BorderLayout.EAST);
		
		southPanel.add(showMessage);
		showMessage.setText("日历：" + getDate());
		// JScrollPane scrollPane = new JScrollPane();
		// scrollPane.add(pCenter);

		backTodayBtn.addActionListener(this);
		nextMonthBtn.addActionListener(this);
		previousMonthBtn.addActionListener(this);
		
		yearComboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				if(e.getStateChange() == ItemEvent.SELECTED){ 
					if(yearComboBoxIgnore){
						yearComboBoxIgnore = false;
						return;
					}
		            String year = (String) e.getItem(); 
	                System.out.println("---ItemEvent performed:" + e.paramString() + "---");  
		            boolean result = itemStateChangedAction("YEAR",year);
		            if(result){
		            	initDate(false);
		            	try{  
			            	openCalendar(false);
			            }catch(Exception ex){  
			               ex.printStackTrace();   
			            }
		            }
		        }  
			}
		});
		
		monthComboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED){ 
					if(monthComboBoxIgnore){
						monthComboBoxIgnore = false;
						return;
					}
					String month = (String) e.getItem();
					System.out.println("---ItemEvent performed:"+ e.paramString() + "---");
					boolean result = itemStateChangedAction("MONTH", month);
					if (result) {
						initDate(false);
						try {
							openCalendar(false);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
		        }  
			}
		});
	}
	
	private void initDate(boolean isFirst) {
		if(isFirst){
			this.year = calendar.get(Calendar.YEAR);
			this.month = calendar.get(Calendar.MONTH) + 1;
			this.calendarDO = new CalendarDO();
		}
		calendarDO.setYear(year);
		calendarDO.setMonth(month);
		days = calendarDO.getCalendar();
	}

	private void buildMenu() {
		// 菜单设置
		JMenuBar menuBar = new JMenuBar();

		JMenu operateMenu = new JMenu("操作");
		JMenuItem nowSyncMenuItem = new JMenuItem("立即同步");
		JCheckBoxMenuItem autoAsyMenuItem = new JCheckBoxMenuItem("自动同步");
		operateMenu.add(nowSyncMenuItem);
		//operateMenu.add(autoAsyMenuItem);
		menuBar.add(operateMenu);

		JMenu settingMenu = new JMenu("设置");
		JCheckBoxMenuItem menuItem1 = new JCheckBoxMenuItem("bitefu");
		JCheckBoxMenuItem menuItem2 = new JCheckBoxMenuItem("easybots");
		//settingMenu.add(menuItem1);
		settingMenu.add(menuItem2);
		menuBar.add(settingMenu);

		this.setJMenuBar(menuBar);

		nowSyncMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent paramActionEvent) {
				try {
					nowSyncHoliday();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		autoAsyMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*JOptionPane.showMessageDialog(this, "暂时不支持自动同步！",
						"节假日管理", JOptionPane.ERROR_MESSAGE);*/
			}
		});

		menuItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean b = ((JCheckBoxMenuItem) e.getSource()).getState();
				if (b) {
					SERVER_TYPE = HolidayWebTool.SERVER_BITTEFU;
				} else {
					SERVER_TYPE = "";
				}
			}
		});

		menuItem2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean b = ((JCheckBoxMenuItem) e.getSource()).getState();
				if (b) {
					SERVER_TYPE = HolidayWebTool.SERVER_EASYBOTS;
				} else {
					SERVER_TYPE = "";
				}
			}
		});
	}

	private void openCalendar(boolean isFirst) throws Exception{
		monthPanel = new JMonthPanel(this,days,getDate(),getHolidays());
		if(monthPanel != null){
			this.centerPanel.removeAll();
			this.centerPanel.add(monthPanel,BorderLayout.CENTER);
		}
		
		if(!isFirst){
			centerPanel.setVisible(false);
			centerPanel.setVisible(true);
		}
	}
	
	private boolean itemStateChangedAction(String type,String value){
		int number = 0;
		if(value == null && "".equals(value)){
			return false;
        }
		try{
			number = Integer.valueOf(value);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		if(type.equals("YEAR")){
			this.year = number;
			showMessage.setText("日历：" + getDate());
			return true;
		}else if(type.equals("MONTH")){
			this.month = number;
			showMessage.setText("日历：" + getDate());
			return true;
		}
		return false;
	}
	
	private void changeComboBox(){
		yearComboBoxIgnore = true;
		yearComboBox.setSelectedItem(String.valueOf(year));
		monthComboBoxIgnore = true;
		monthComboBox.setSelectedItem(StringFunction.fillBeforeValue(String.valueOf(month)));
		
	}

	private void nowSyncHoliday() throws Exception{
		if("".equals(SERVER_TYPE)){
			JOptionPane.showMessageDialog(this, "请先选择网络源！");
			return;
		}
		
		Map<String,Object> PO = new HashMap<String, Object>();
		String sql = "select count(*) from PSJJR where F_YEAR = ?";
		boolean deleteDate = false;
		String[] paramter = new String[1];
		paramter[0] = String.valueOf(this.year);
		
		PO.put("CMDTEXT", sql);
		JResponseObject RO = (JResponseObject) HolidayService.getCountBySql(PO,paramter);
		if (RO == null) {
			JOptionPane.showMessageDialog(this, "读取错误！\r\n原因：RO为空", "提示",
					JOptionPane.ERROR_MESSAGE);
			return ;
		} else if (RO.ErrorCode == 1) {
			Integer count = (Integer) RO.ResponseObject;
			if(count != 0){
				int result = JOptionPane.showConfirmDialog(this,
						"已存在 "+this.year+"年的数据"+count+" 条，是否删除数据在同步？", "系统提示", JOptionPane.YES_NO_CANCEL_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					deleteDate = true;
				} else if (result == JOptionPane.NO_OPTION) {
					deleteDate = false;
				} else if (result == JOptionPane.CANCEL_OPTION) {
					return ;
				}
			}
		} else {
			JOptionPane.showMessageDialog(this, "读取错误！\r\n原因："
					+ RO.ErrorString, "提示", JOptionPane.ERROR_MESSAGE);
			return ;
		}
		
		ArrayList<String[]> jsonResult = HolidayWebTool.requestGetYear(SERVER_TYPE,String.valueOf(year));
		
		if(deleteDate){
			sql = " delete from PSJJR where  F_YEAR = ? ";
			PO.put("F_YEAR",String.valueOf(this.year));
		}else{
			sql = "";
		}
		PO.put("DELETESQL", sql);
		RO = (JResponseObject) HolidayService.syncHoliday(PO,jsonResult);
		if (RO == null) {
			JOptionPane.showMessageDialog(this, "同步错误！\r\n原因：RO为空", "提示",
					JOptionPane.ERROR_MESSAGE);
		} else if (RO.ErrorCode == 1) {
			JOptionPane.showMessageDialog(this, "同步成功！");
			openCalendar(false);
		} else {
			JOptionPane.showMessageDialog(this, "同步错误！\r\n原因："
					+ RO.ErrorString, "提示", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public String getDate(){
		return this.year + "年" + StringFunction.fillBeforeValue(String.valueOf(month)) + "月";
	}
	
	@SuppressWarnings("unchecked")
	private  HashMap<String,String> getHolidays() {
		HashMap<String,String> holidays = null;
		Map<String,Object> PO = new HashMap<String, Object>();
		PO.put("F_YEAR", String.valueOf(this.year));
		PO.put("F_MONTH", StringFunction.fillBeforeValue(String.valueOf(month)));
		JResponseObject RO = null;
		try {
			RO = (JResponseObject) HolidayService.listHoliday(PO,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (RO == null) {
			JOptionPane.showMessageDialog(this, "读取错误！\r\n原因：RO为空", "提示",
					JOptionPane.ERROR_MESSAGE);
		} else if (RO.ErrorCode == 1) {
			holidays = (HashMap<String, String>) RO.ResponseObject;
		} else {
			JOptionPane.showMessageDialog(this, "读取错误！\r\n原因："
					+ RO.ErrorString, "提示", JOptionPane.ERROR_MESSAGE);
		}
		return holidays;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == nextMonthBtn) {
			month = month + 1;
			if (month > 12){
				month = 1;
				year += 1;
			}
			initDate(false);
			changeComboBox();
			try {
				openCalendar(false);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == previousMonthBtn) {
			month = month - 1;
			if (month < 1){
				month = 12;
				year -= 1;
			}
			initDate(false);
			changeComboBox();
			try {
				openCalendar(false);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == backTodayBtn){
			try {
				initDate(true);
				changeComboBox();
				openCalendar(false);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		showMessage.setText("日历：" + getDate());
	}
	
	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}
	public static void main(String args[]) {
		HolidayManageDlg dlg = new HolidayManageDlg();	
		dlg.show(true);
	}

}
