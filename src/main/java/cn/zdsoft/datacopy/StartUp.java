package cn.zdsoft.datacopy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.zdsoft.common.FileUtil;
import cn.zdsoft.common.PathUtil;
import cn.zdsoft.datacopy.config.Filter;
import cn.zdsoft.datacopy.util.Config;
import cn.zdsoft.datacopy.util.LogHelper;

public class StartUp extends Thread {
	/**
	 * 程序自动运行中
	 */
	private boolean running = false;

	/**
	 * 重写run方法，系统启动时就会自动调用该方法
	 */
	@Override
	public void run() {
		while (running) {
			try {
				List<String> inputs = Config.GetConfig().getDataCopyConfig().getInputDirs();
				// 循环目录
				for (String input : inputs) {
					File[] files = FileUtil.GetFiles(input);
					// 循环文件
					for (File file : files) {
						try {
							boolean isSuccess = true;// 是否处理成功
							boolean isDest = false;// 是否被处理

							// 循环过滤器
							for (Filter filter : Config.GetConfig().getDataCopyConfig().getFilters()) {
								for (String search : filter.getSearchPatterns()) {
									if (file.getName().contains(search)) {
										// 有一个筛选条件满足即可
										isDest = true;
										boolean result = CopyFile(file, filter);
										LogHelper.getLogger().info("文件:" + file.getAbsolutePath() + " 被过滤器" + file.getName() + "处理");
										if (!result) {
											isSuccess = false;
											LogHelper.getLogger().error("文件:" + file.getAbsolutePath() + " 被过滤器" + file.getName() + "处理失败");
										}
										break;
									}
								}
							}

							// 是否被处理,未处理的移动到丢弃目录
							if (isDest == false) {
								String fileName = PathUtil.Combine(Config.GetConfig().getDataCopyConfig().getDiscardDir(), file.getName());
								file.renameTo(new File(fileName));
								break;
							}
							// 处理失败的文件移动到失败目录
							if (isSuccess == false) {
								String fileName = PathUtil.Combine(Config.GetConfig().getDataCopyConfig().getFailedDir(), file.getName());
								file.renameTo(new File(fileName));
								break;
							} else {
								// 处理成功,直接删除
								file.delete();
							}

						} catch (Exception e) {
							LogHelper.getLogger().error("处理文件:" + file.getAbsolutePath() + "出现了无法识别的异常", e);
						}

					}
				}

			} catch (Exception e) {
				LogHelper.getLogger().info("复制程序出现无法识别的异常", e);
			} finally {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					LogHelper.getLogger().error("程序暂停1秒钟失败", e);
				}
			}
		}
		LogHelper.getLogger().info("程序停止.");
	}

	/**
	 * 文件复制
	 * 
	 * @param file
	 *            待复制的文件
	 * @param filter
	 *            过滤器
	 * @return
	 */
	private boolean CopyFile(File file, Filter filter) {
		try {
			List<String> dirs=new ArrayList<String>();
			if(filter.isBalanced()){
				//随机分配
				
			}else{
				//依次复制
			}

			return true;
		} catch (Exception e) {
			LogHelper.getLogger().error("文件:" + file.getAbsolutePath() +"过滤器:"+filter.getName()+ "复制失败", e);
			return false;
		}
	}

	/**
	 * 启动
	 */
	public void Start() {
		// 读取配置文件
		Config.GetConfig();

		// 启动,会自动调用run方法
		running = true;
		start();
	}

	/**
	 * 停止
	 */
	public void Stop() {
		LogHelper.getLogger().info("收到停止指令,程序停止中.");
		running = false;
	}
}
