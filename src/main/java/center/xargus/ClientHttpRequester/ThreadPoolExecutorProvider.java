package center.xargus.ClientHttpRequester;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorProvider {
	private static ThreadPoolExecutorProvider instance;
	private ThreadPoolExecutor threadPool;
	
	private ThreadPoolExecutorProvider() {
		threadPool = new ThreadPoolExecutor(3, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}
	
	public static synchronized ThreadPoolExecutorProvider getInstance() {
		if (instance == null) {
			instance = new ThreadPoolExecutorProvider();
		}
		
		return instance;
	}
	
	public void enqueue(Runnable runnable) {
		threadPool.execute(runnable);
	}
}
