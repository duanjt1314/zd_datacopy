package cn.zdsoft.datacopy.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 具体的过滤器对象，一个过滤器就代表一种文件复制方式
 * 
 * @author 段江涛
 * @date 2017-07-10
 */
public class Filter {
	/************* 构造函数 *************/
	/**
	 * 构造函数,初始化集合对象
	 */
	public Filter() {
		this.dests=new ArrayList<Dest>();
		this.searchPatterns=new String[]{};
	}	
	
	/************* 字段 *************/
	/**
	 * 过滤器名称
	 */
	private String name;

	/**
	 * 是否写复制日志
	 */
	private boolean writeCopyLog;
	/**
	 * 是否平均分配到输出目录
	 */
	private boolean isBalanced;
	/**
	 * 搜索关键字集合，将采用indexOf>-1匹配
	 */
	private String[] searchPatterns;
	/**
	 * 输出路径集合
	 */
	private List<Dest> dests;

	/*********** 方法 ***********/

	/**
	 * @return 获取 过滤器名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param 设置
	 *            过滤器名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 获取 是否写复制日志
	 */
	public boolean isWriteCopyLog() {
		return writeCopyLog;
	}

	/**
	 * 设置是否写复制日志
	 * 
	 * @param writeCopyLog
	 */
	public void setWriteCopyLog(boolean writeCopyLog) {
		this.writeCopyLog = writeCopyLog;
	}

	/**
	 * @return 获取 是否平均分配到输出目录
	 */
	public boolean isBalanced() {
		return isBalanced;
	}

	/**
	 * 设置 是否平均分配到输出目录
	 * 
	 * @param
	 */
	public void setBalanced(boolean isBalanced) {
		this.isBalanced = isBalanced;
	}

	/**
	 * @return 获取搜索关键字集合
	 */
	public String[] getSearchPatterns() {
		return searchPatterns;
	}

	/**
	 * 设置 搜索关键字集合
	 * 
	 * @param searchPatterns
	 */
	public void setSearchPatterns(String[] searchPatterns) {
		this.searchPatterns = searchPatterns;
	}

	/**
	 * @return 获取 输出路径集合
	 */
	public List<Dest> getDests() {
		return dests;
	}

	/**
	 * 设置 输出路径集合
	 * 
	 * @param dists
	 */
	public void setDests(List<Dest> dests) {
		this.dests = dests;
	}

}
