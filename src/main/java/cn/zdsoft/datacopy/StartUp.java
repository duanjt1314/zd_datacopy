package cn.zdsoft.datacopy;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.zdsoft.common.DirectoryUtil;
import cn.zdsoft.common.FileUtil;
import cn.zdsoft.common.PathUtil;
import cn.zdsoft.datacopy.config.Dest;
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
				// 初始化目录
				InitDir();

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
			if (filter.getDests().size() == 0) {
				LogHelper.getLogger().error("过滤器:" + filter.getName() + "没有输出目录,无法复制");
				return false;
			}
			// 定义需要复制的所有文件
			List<String> fileNames = new ArrayList<String>();
			if (filter.isBalanced()) {
				// 随机分配
				int index = new Random().nextInt(filter.getDests().size());
				Dest dest = filter.getDests().get(index);
				fileNames.add(PathUtil.Combine(dest.getPath(), file.getName(), dest.getAppend()));
				DirectoryUtil.CreateDir(dest.getPath());// 万一目录不存在则创建目录
			} else {
				// 依次复制
				for (Dest dest : filter.getDests()) {
					fileNames.add(PathUtil.Combine(dest.getPath(), file.getName(), dest.getAppend()));
					DirectoryUtil.CreateDir(dest.getPath());// 万一目录不存在则创建目录
				}
			}

			// 正式复制
			for (String fileName : fileNames) {
				FileUtil.CopyFile(file.getAbsolutePath(), fileName);
			}

			return true;
		} catch (Exception e) {
			LogHelper.getLogger().error("文件:" + file.getAbsolutePath() + "过滤器:" + filter.getName() + "复制失败", e);
			return false;
		}
	}

	/**
	 * 初始化相关目录(来源目录、错误目录、丢弃目录)
	 */
	private void InitDir() {
		// 来源目录
		for (String dir : Config.GetConfig().getDataCopyConfig().getInputDirs()) {
			DirectoryUtil.CreateDir(dir);
		}
		// 错误目录
		DirectoryUtil.CreateDir(Config.GetConfig().getDataCopyConfig().getFailedDir());
		// 丢弃目录
		DirectoryUtil.CreateDir(Config.GetConfig().getDataCopyConfig().getDiscardDir());
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
