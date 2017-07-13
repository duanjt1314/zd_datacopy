package cn.zdsoft.datacopy.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.zdsoft.common.PathUtil;
import cn.zdsoft.common.XmlUtil;
import cn.zdsoft.datacopy.config.*;

/**
 * 系统配置对象，将记录系统相关的所有配置信息
 * 
 * @author 段江涛
 * @date 2017-07-10
 */
public class Config {
	/**
	 * 数据复制配置，对应config/config.xml内部数据
	 */
	private DataCopyConfig dataCopyConfig;
	/**
	 * 定义局部静态变量
	 */
	private static Config _config = null;

	/**
	 * 获取数据复制配置
	 * 
	 * @return
	 */
	public DataCopyConfig getDataCopyConfig() {
		return this.dataCopyConfig;
	}

	/**
	 * 获取配置对象，方便静态调用
	 * 
	 * @return
	 */
	public static Config GetConfig() {
		if (_config == null) {
			_config = new Config();
			_config.LoadConfig();
		}
		return _config;
	}

	/**
	 * 加载配置文件
	 */
	private void LoadConfig() {
		String fileName = PathUtil.Combine(Helper.GetAppDir(), "config", "config.xml");
		File file = new File(fileName);
		if (file.exists() && file.isFile()) {
			try {
				// 解析xml文件
				SAXReader reader = new SAXReader();
				Document document = reader.read(file);
				Element ele = document.getRootElement();

				dataCopyConfig = new DataCopyConfig();
				Element eleInputs = XmlUtil.GetXmlElement(ele, "Inputs");
				List<String> inputs = new ArrayList<String>();
				for (Object item : eleInputs.elements("Input")) {
					Element eleInput = (Element) item;
					inputs.add(eleInput.getText());
				}

				dataCopyConfig.setInputDirs(inputs);
				dataCopyConfig.setFailedDir(XmlUtil.GetXmlElement(ele, "Failed").getText());
				dataCopyConfig.setDiscardDir(XmlUtil.GetXmlElement(ele, "Discard").getText());
				dataCopyConfig.setCopyLogDir(XmlUtil.GetXmlElement(ele, "CopyLog", ""));

				List<Filter> filters=LoadFilters(XmlUtil.GetXmlElement(ele, "Filters"));
				dataCopyConfig.setFilters(filters);

			} catch (Exception e) {
				LogHelper.getLogger().error("xml配置文件解析错误,路径：" + fileName, e);
			}
		} else {
			LogHelper.getLogger().error("配置文件:" + fileName + " 没有找到,请配置正确路径");
		}
	}

	/**
	 * 加载过滤器，循环加载
	 * 
	 * @param ele
	 *            过滤器节点DataCopyConfig>Filters
	 * @throws Exception
	 */
	private List<Filter> LoadFilters(Element ele) throws Exception {
		List<Filter> filters = new ArrayList<Filter>();
		for (Object item : ele.elements("Filter")) {
			Filter filter = new Filter();
			Element eleFilter = (Element) item;
			filter.setName(XmlUtil.GetXmlElement(eleFilter, "Name").getText());
			filter.setWriteCopyLog(XmlUtil.GetXmlElement(eleFilter, "WriteCopyLog", false));
			filter.setBalanced(XmlUtil.GetXmlElement(eleFilter, "IsBalanced", false));
			
			//搜索选项
			String searchPatterns=XmlUtil.GetXmlElement(eleFilter, "SearchPatterns").getText();
			filter.setSearchPatterns(searchPatterns.split("\\|"));
			
			//输出目录
			List<Dest> dests= LoadDest(XmlUtil.GetXmlElement(eleFilter, "Dests"));
			filter.setDests(dests);
			
			filters.add(filter);
		}
		return filters;
	}
	
	/**
	 * 加载输出目录
	 * @param ele
	 */
	private List<Dest> LoadDest(Element ele){
		List<Dest> dests = new ArrayList<Dest>();
		for (Object item : ele.elements("Dest")) {
			Dest dest = new Dest();
			Element eleFilter = (Element) item;
			dest.setPath(eleFilter.getText());
			dest.setAppend(XmlUtil.GetXmlAttr(eleFilter, "Append", ""));
			dests.add(dest);
		}
		return dests;
	}
}
