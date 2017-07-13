package cn.zdsoft.datacopy;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.zdsoft.common.DateUtil;
import cn.zdsoft.common.DirectoryUtil;
import cn.zdsoft.common.FileUtil;
import cn.zdsoft.common.PathUtil;
import cn.zdsoft.common.StringUtil;
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
							LogHelper.getLogger().info("准备处理文件:" + file.getAbsolutePath());
							boolean isSuccess = true;// 是否处理成功
							boolean isDest = false;// 是否被处理

							// 循环过滤器
							for (Filter filter : Config.GetConfig().getDataCopyConfig().getFilters()) {
								for (String search : filter.getSearchPatterns()) {
									if (file.getName().contains(search)) {
										// 有一个筛选条件满足即可
										isDest = true;
										boolean result = CopyFile(file, filter);
										LogHelper.getLogger().info("文件:" + file.getAbsolutePath() + " 被过滤器" + filter.getName() + "处理");
										if (!result) {
											isSuccess = false;
											LogHelper.getLogger().error("文件:" + file.getAbsolutePath() + " 被过滤器" + filter.getName() + "处理失败");
										}
										break;
									}
								}
							}

							// 是否被处理,未处理的移动到丢弃目录
							if (isDest == false) {
								String fileName = PathUtil.Combine(Config.GetConfig().getDataCopyConfig().getDiscardDir(), file.getName());
								file.renameTo(new File(fileName));
								LogHelper.getLogger().info("文件：" + file.getAbsolutePath() + "没有需要处理的过滤器,直接移动到丢弃目录");
								break;
							}
							// 处理失败的文件移动到失败目录
							if (isSuccess == false) {
								String fileName = PathUtil.Combine(Config.GetConfig().getDataCopyConfig().getFailedDir(), file.getName());
								file.renameTo(new File(fileName));
								LogHelper.getLogger().info("文件：" + file.getAbsolutePath() + "处理失败,移动到失败目录");
								break;
							} else {
								// 处理成功,直接删除
								FileUtil.DeleteFile(file.getAbsolutePath());
								LogHelper.getLogger().info("文件：" + file.getAbsolutePath() + "处理成功,直接删除");
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
				fileNames.add(PathUtil.Combine(dest.getPath(), file.getName() + dest.getAppend()));
				DirectoryUtil.CreateDir(dest.getPath());// 万一目录不存在则创建目录
			} else {
				// 依次复制
				for (Dest dest : filter.getDests()) {
					fileNames.add(PathUtil.Combine(dest.getPath(), file.getName() + dest.getAppend()));
					DirectoryUtil.CreateDir(dest.getPath());// 万一目录不存在则创建目录
				}
			}

			// 正式复制
			String content = "";
			for (String fileName : fileNames) {
				FileUtil.CopyFile(file.getAbsolutePath(), fileName);
				content += file.getAbsolutePath() + "\t" + fileName + System.lineSeparator();
			}
			WriteLog(content, filter.getName());

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
	 * 写入复制日志
	 * 
	 * @param content
	 *            需要写入的内容
	 * @param filterName
	 *            写入的过滤器的名称
	 */
	private void WriteLog(String content, String filterName) {
		try {
			String logDir = Config.GetConfig().getDataCopyConfig().getCopyLogDir();
			if (logDir.isEmpty()) {
				LogHelper.getLogger().error("日志输出目录未定义");
				return;
			}
			String path = PathUtil.Combine(logDir, filterName);
			DirectoryUtil.CreateDir(path);
			String fileName = PathUtil.Combine(path, DateUtil.Format(new Date(), "yyyy-MM-dd") + ".txt");
			FileWriter fw = new FileWriter(fileName, true);
			fw.write(content);
			fw.close();
		} catch (Exception e) {
			LogHelper.getLogger().error("写入日志出现异常", e);
		}
	}

	/**
	 * 启动
	 */
	public void Start() {
		// 读取配置文件
		LogHelper.getLogger().info("加载配置文件,结果输出：" + StringUtil.GetJsonString(Config.GetConfig()));

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
