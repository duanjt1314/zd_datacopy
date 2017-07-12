package cn.zdsoft.datacopy;

import cn.zdsoft.common.StringUtil;
import cn.zdsoft.datacopy.util.Config;

/**
 * 系统启动类
 * 
 * @author Administrator
 *
 */
public class App {

	/**
	 * 主方法
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		 String json =
		 StringUtil.GetJsonString(Config.GetConfig().getDataCopyConfig());
		 System.out.println(json);
		
//		String a = "_WifiTerminalInfoLog_|_WifiOnlineLog_|_WifiActiveLog_";
//		String[] arr = a.split("\\|");
//		System.out.println(StringUtil.GetJsonString(arr));
	}

}
