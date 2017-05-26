package com.vito.holiday.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * @Description:  在线获取节假日
 * 节假日API bitefu对于本应用场景有一个无法解决的问题 所以只支持easybots
 * 1、bitefu： http://tool.bitefu.net/jiari/
 * 2、easybots： http://www.easybots.cn/holiday_api.net
 * @author wutp 2017年5月17日
 * @version 1.0
 */
public class HolidayWebTool {
	
	public final static String SERVER_EASYBOTS = "EASYBOTS";
	public final static String SERVER_BITTEFU = "BITTEFU";
	public final static String URL_EASYBOTS_CHECK = "http://www.easybots.cn/api/holiday.php?d=";
	public final static String URL_EASYBOTS_GET = "http://www.easybots.cn/api/holiday.php?m=";
	public final static String URL_BITTEFU = "http://tool.bitefu.net/jiari/?d=";
	
    /**
	 * @Description:
	 * @auther: wutp 2017年5月16日
	 * @param serverType
	 * @param httpArg
	 * @return
	 * @return Map<String,Object>
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> request(String httpUrl) {
		Map<String, Object> map = null;
		BufferedReader reader = null;
		String result = null;
		StringBuffer sbf = new StringBuffer();

		try {
			URL url = new URL(httpUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			InputStream is = connection.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sbf.append(strRead);
				sbf.append("\r\n");
			}
			reader.close();
			result = sbf.toString();
			map = (Map<String, Object>) JSON.parse(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
     * @Description:参数
     * @auther: wutp 2017年5月17日
     * @param httpArg
     * @return
     * @return String
     */
    public static String requestCheck(String serverType,String httpArg) {
    	String result = null;
    	if(serverType.equals(SERVER_EASYBOTS)){
    		result = requestCheckByEasybots(httpArg);
    	}else if(serverType.equals(SERVER_BITTEFU)){
    		result = requestCheckBybitefu(httpArg);
    	}
		return result;
    }
    
    private static String requestCheckByEasybots(String httpArg) {
       
        return null;
    }
    
    private static String requestCheckBybitefu(String httpArg) {
    	 return null;
    }
    
    /**
     * @Description:参数
     * @auther: wutp 2017年5月17日
     * @param httpArg
     * @return
     * @return String
     */
    public static ArrayList<String> requestGetMonth(String serverType,String httpArg) {
    	ArrayList<String> list = null;
    	if(serverType.equals(SERVER_EASYBOTS)){
    		list = requestGetMonthByEasybots(httpArg);
    	}else if(serverType.equals(SERVER_BITTEFU)){
    		list = requestGetMonthByBitefu(httpArg);
    	}
        return list;
    }
    
    private static ArrayList<String> requestGetMonthByEasybots(String httpArg) {

        try {
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static ArrayList<String> requestGetMonthByBitefu(String httpArg) {
    	 try {
             
         } catch (Exception e) {
             e.printStackTrace();
         }
         return null;
    }
    
    /**
     * @Description:参数
     * @auther: wutp 2017年5月17日
     * @param httpArg
     * @return
     * @return String
     */
    public static ArrayList<String[]> requestGetYear(String serverType, String year) {
    	ArrayList<String[]> list = null;
    	if(serverType.equals(SERVER_EASYBOTS)){
    		list = requestGetYearByEasybots(year);
    	}else if(serverType.equals(SERVER_BITTEFU)){
    		list = requestGetYearByBitefu(year);
    	}
        return list;
    }
    
    private static ArrayList<String[]> requestGetYearByEasybots(String year) {
    	ArrayList<String[]> list = null;
    	String httpArg = "";
    	String month ;
    	for(int i=1;i<13;i++){
    		month = String.valueOf(i).length() == 1 ? "0"+i : ""+i ;
    		httpArg += year+month+",";
    	}
    	if(httpArg.length() > 0){
    		httpArg = httpArg.substring(0, httpArg.length()-1);
    	}	
        String httpUrl = URL_EASYBOTS_GET+httpArg;
        try {
        	Map<String,Object> map = request(httpUrl);
            list = map2ListByEasybots(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    private static ArrayList<String[]> requestGetYearByBitefu(String year) {
    	ArrayList<String[]> list = null;
    	
        String httpUrl=URL_BITTEFU+year;
        try {
			Map<String,Object> map = request(httpUrl);
            list = map2ListByBitefu(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    private static ArrayList<String[]> map2ListByEasybots(Map<String,Object> map){
    	ArrayList<String[]> list = new ArrayList<String[]>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if(entry.getValue() != null && entry.getValue().toString().length() > 1){
				String[] jjr = entry.getValue().toString().split(",");
				for(int i=0;i<jjr.length;i++){
					list.add(string2Array(entry.getKey(),jjr[i]));
				}
			}
		}
		return list;
    }
    
    /**
     * @Description:装换成日期格式
     * @auther: wutp 2017年5月18日
     * @param key
     * @param value
     * @return
     * @return String[]
     */
    private static String[] string2Array(String key,String value){
    	String[] date = new String[2];
    	if(value.length() ==9){
    		if(value.startsWith("{")){
    			value = value.replace("{", "");
    		}else{
    			value = value.replace("}", "");
    		}
    	}
    	value = value.replaceAll("\"", "");
    	date = value.split(":");
    	date[0] = key+date[0];
    	date[1] = "2".equals(date[1]) ? "1" : date[1] ;
    	return date;
    }
    
    private static ArrayList<String[]> map2ListByBitefu(Map<String,Object> map){
    	ArrayList<String[]> list = new ArrayList<String[]>();
    	for (Object v : map.values()) {
    		System.out.println("value= " + v.toString());
    		String[] jjr = v.toString().split(",");
			for(int i=0;i<jjr.length;i++){
				list.add(new String[]{
					jjr[i],"2"
				});
			}
        }
		return list;
    }
    
    public static void main(String[] args) {
//    	String[] date = string2Array("201611","{\"19\":\"1\"");
//    	System.out.println(date[0]+"-"+date[1]);
        //判断今天是否是工作日 周末 还是节假日
//        SimpleDateFormat f=new SimpleDateFormat("yyyyMMdd");
//        String httpArg=f.format(new Date());
//        System.out.println(httpArg);
        ArrayList<String[]> jsonResult = requestGetYear(SERVER_EASYBOTS,"2017");
        String[] date = null;
        System.out.println("共有"+jsonResult.size()+"天节假日！");
        for(int i=0;i<jsonResult.size();i++){
        	date = (String[]) jsonResult.get(i);
        	System.out.println(date[0]+":"+date[1]);
        }
       
        //System.out.println(jsonResult);
    }

}
