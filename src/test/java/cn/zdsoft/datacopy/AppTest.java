package cn.zdsoft.datacopy;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Random;

import org.junit.Test;

import cn.zdsoft.datacopy.util.LogHelper;

public class AppTest {

	@Test
	public void testLog() {
		LogHelper.getLogger().info("成功了");
	}
	
	@Test
	public void test(){
		for (int i = 0; i < 10; i++) {
			System.out.println(new Random().nextInt(10));
		}
	}
	
	/**
	 * 移动文件测试
	 */
	@Test
	public void testMoveFile(){
		File file=new File("f:\\result.txt");		
		file.renameTo(new File("F:\\迅雷下载\\abc.txt"));
	}
	
	@Test
	public void testDeleteFile(){
		File file=new File("F:\\Switch2015\\DataCopyService\\Logs\\20170523154716_WifiTerminalInfoLog_000001_000823.gz");
		boolean con=file.delete();
		assertTrue(con);
	}
	
	

}
