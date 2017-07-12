package cn.zdsoft.datacopy;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

import cn.zdsoft.common.FileUtil;
import cn.zdsoft.datacopy.util.LogHelper;

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
		
	}

	public static <T> T Convert(String str, Object defaultValue) {
		if (str.equals("")) {
			return (T)defaultValue;
		}
		System.out.println(defaultValue.getClass());		
		Class<T> clazz = (Class<T>) defaultValue.getClass();	
		System.out.println(clazz);
		return clazz.cast(str);
	}

}
