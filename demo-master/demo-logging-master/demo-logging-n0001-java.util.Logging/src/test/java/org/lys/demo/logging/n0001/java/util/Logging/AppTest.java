package org.lys.demo.logging.n0001.java.util.Logging;

import java.io.IOException;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
	@Test
	public void testApp() {
		// 程序的其它处理
		// 使用Logger的静态方法获得一个匿名Logger
		Logger logger1 = Logger.getAnonymousLogger();
		// 记录消息
		logger1.log(Level.INFO, "第一条日志记录");
		// 程序的其它处理
	}

	@Test
	public void testMemoryHandler() {
		MemoryHandlerTest mt = new MemoryHandlerTest();
		int trigger = (int) (Math.random() * 100);
		for (int i = 1; i < 100; i++) {
			// 在MemoryHandler中缓存日志记录
			mt.logger.log(Level.INFO, "日志记录" + i);
			if (i == trigger) {
				// 触发事件成立，显式调用MemoryHandler的
				// push方法触发目标Handler输出日志记录到
				// my.log文件中
				mt.mhandler.push();
				break;
			}
		}
	}

	@Test
	public void testFileHandler() throws SecurityException, IOException {
		// 创建一个拥有3个日志文件，每个容量为1Mb的文件处理器
		String pattern = "my%g.log";
		int limit = 1000000; // 1 Mb
		int numLogFiles = 3;
		FileHandler fh = new FileHandler(pattern, limit, numLogFiles);
	}

	@Test
	public void testLevel() {
		// 使用Logger的静态方法获得一个匿名Logger
		Logger logger1 = Logger.getAnonymousLogger();
		// 设置Logger对象记录的最低日志消息级别
		logger1.setLevel(Level.FINER);
		// 记录消息
		logger1.severe("SEVERE级消息");
		logger1.warning("WARNING级消息");
		logger1.config("CONFIG级消息");
		logger1.info("INFO级消息");
		logger1.fine("FINE级消息");
		logger1.finer("FINER级消息");
		logger1.finest("FINEST级消息");

	}

	@Test
	public void testFormatter() {
		// 创建记录器
		Logger log1 = Logger.getLogger("MyLogger");
		// 创建记录处理器
		Handler mh = new ConsoleHandler();
		// 为记录处理器设置Formatter
		mh.setFormatter(new MyFormatter());
		// 为记录器添加记录处理器
		log1.addHandler(mh);
		// 禁止消息处理将日志消息上传给父级处理器
		log1.setUseParentHandlers(false);
		// 记录消息
		log1.severe("消息1");
		log1.warning("消息2");
		log1.info("消息3");
		log1.config("消息4");

	}

}

class MyFormatter extends Formatter {
	public String format(LogRecord rec) {
		StringBuffer buf = new StringBuffer(1000);
		buf.append(new Date().toLocaleString()); // 时间
		buf.append(' ');
		buf.append(rec.getLevel()); // 消息级别
		buf.append(' ');
		buf.append(rec.getMillis()); // 作为消息ID
		buf.append(' ');
		buf.append(formatMessage(rec));// 格式化日志记录数据
		buf.append('\n'); // 换行
		return buf.toString();
	}
}
