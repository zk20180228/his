package cn.honry.inner.system.threadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 创建一个定长线程池(用于执行统计的预处理)
 * @author user
 *
 */
public class MyThreadPool {

	private static int i = Runtime.getRuntime().availableProcessors();
	private static ExecutorService thread = Executors.newFixedThreadPool(i);
	private MyThreadPool(){
	}
	
	public static ExecutorService getThreadPool(){
		return thread;
	}
	
}
