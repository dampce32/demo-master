package org.lys.demo.thread.n0002;
/**
 * @description: 大戏舞台
 * @copyright: 福建骏华信息有限公司 (c)2016</p>
 * @createTime: 2016年4月12日下午12:37:43
 * @author：lys
 * @version：1.0
 */
public class Stage extends Thread {

	
	@Override
	public void run() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		ArmyRunnable armyTaskOfSuiDynasty=new ArmyRunnable();
		ArmyRunnable armyTaskOfRevolt =new ArmyRunnable();
		
		Thread armyOfSuiDynasty = new Thread(armyTaskOfSuiDynasty,"隋军");
		Thread armyOfRevolt = new Thread(armyTaskOfRevolt,"农民起义军");
		//启动线程，让军队开始作战
		armyOfSuiDynasty.start();
		armyOfRevolt.start();
		
		//舞台线程休眠，大家专心观看军队厮杀
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("正当双方激战正酣，半路杀出了个程咬金");
		
		Thread mrCheng = new KeyPersonThread();
		mrCheng.setName("程咬金");
		
		System.out.println("程咬金的理想就是结束战争，使百姓安居乐业");
		
		//军队停止作战
		armyTaskOfSuiDynasty.keepRunning= false;
		armyTaskOfRevolt.keepRunning= false;
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		/*
		 * 历史大戏留给关键人物
		 */
		mrCheng.start();
		//万众瞩目，所有线程线程等待程先生完成历史使命
		try {
			mrCheng.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Stage().start();
	}
}
