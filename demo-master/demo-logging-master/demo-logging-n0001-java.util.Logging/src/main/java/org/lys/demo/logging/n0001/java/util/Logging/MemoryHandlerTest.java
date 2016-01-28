package org.lys.demo.logging.n0001.java.util.Logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.MemoryHandler;

public class MemoryHandlerTest {
	FileHandler fhandler;
	Logger logger;
	MemoryHandler mhandler;

	MemoryHandlerTest() {
		try {
			// 构造名为my.log的日志记录文件
			fhandler = new FileHandler("my.log");
			int numRec = 5;
			// 构造一个5个日志记录的MemoryHandler，
			// 其目标Handler为一个FileHandler
			mhandler = new MemoryHandler(fhandler, numRec, Level.OFF);
			// 构造一个记录器
			logger = Logger.getLogger("com.mycompany");
			// 为记录器添加一个MemoryHandler
			logger.addHandler(mhandler);
		} catch (IOException e) {
		}
	}

}
