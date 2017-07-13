package cn.zdsoft.datacopy.config;

/**
 * 输出目录对象
 * @author 段江涛
 * @date 2017-07-10
 */
public class Dest {
	/**
	 * 输出目录路径
	 */
	private String path;
	/**
	 * 文件名追加内容
	 */
	private String append;
	
	
	/**
	 * @return 获取 输出目录路径
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param 设置 输出目录路径
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * @return 获取 文件名追加内容
	 */
	public String getAppend() {
		return append;
	}
	/**
	 * @param 设置 文件名追加内容
	 */
	public void setAppend(String append) {
		this.append = append;
	}
	
	
}
