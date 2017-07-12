package cn.zdsoft.datacopy.config;

import java.util.List;

/**
 * 数据复制顶级配置类
 * @author Administrator
 *
 */
public class DataCopyConfig {
	/**
	 * 输入目录
	 */
	private List<String> inputDirs;
	/**
	 * 失败目录
	 */
	private String failedDir;
	/**
	 * 丢弃目录
	 */
	private String discardDir;
	/**
	 * 过滤器集合
	 */
	private List<Filter> filters;
	
	/**
	 * @return 获取 输入目录
	 */
	public List<String> getInputDirs() {
		return inputDirs;
	}
	/**
	 * @param 设置 输入目录
	 */
	public void setInputDirs(List<String> inputDirs) {
		this.inputDirs = inputDirs;
	}
	/**
	 * @return 获取 失败目录
	 */
	public String getFailedDir() {
		return failedDir;
	}
	/**
	 * @param 设置 失败目录
	 */
	public void setFailedDir(String failedDir) {
		this.failedDir = failedDir;
	}
	/**
	 * @return 获取 丢弃目录
	 */
	public String getDiscardDir() {
		return discardDir;
	}
	/**
	 * @param 设置 丢弃目录
	 */
	public void setDiscardDir(String discardDir) {
		this.discardDir = discardDir;
	}
	/**
	 * @return 获取 过滤器
	 */
	public List<Filter> getFilters() {
		return filters;
	}
	/**
	 * @param 设置 过滤器
	 */
	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}
	
	
	
	 
}
