package org.lys.demo.thread.n0002;
/**
 * @description: 军队线程 模拟作战双方的行为
 * @copyright: 福建骏华信息有限公司 (c)2016</p>
 * @createTime: 2016年4月12日上午11:19:18
 * @author：lys
 * @version：1.0
 */
public class ArmyRunnable implements Runnable {

	//volatitle保证了线程可以正确的读取其他线程写入的值
	//可见性 ref JMM,happens-before原则
	volatile boolean keepRunning=true;
	
	public void run() {
		while(keepRunning){
			//发动5连击
			for (int i = 0; i < 5; i++) {
				System.out.println(Thread.currentThread().getName()+"进攻对方["+i+"]");
				//让出了处理器时间，下次该谁进攻还不一定呢！
				Thread.yield();
			}
		}
	}

}
