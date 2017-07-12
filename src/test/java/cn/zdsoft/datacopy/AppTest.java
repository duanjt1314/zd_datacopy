package cn.zdsoft.datacopy;

import static org.junit.Assert.*;

import org.junit.Test;

import cn.zdsoft.datacopy.util.LogHelper;

public class AppTest {

	@Test
	public void testLog() {
		LogHelper.getLogger().info("成功了");
	}

}
