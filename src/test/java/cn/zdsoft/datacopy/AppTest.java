package cn.zdsoft.datacopy;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
			

}
