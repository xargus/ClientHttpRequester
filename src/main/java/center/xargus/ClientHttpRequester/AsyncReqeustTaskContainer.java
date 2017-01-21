package center.xargus.ClientHttpRequester;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import center.xargus.ClientHttpRequester.connect.StreamCancelable;
import center.xargus.ClientHttpRequester.connect.TaskCancelRunnable;

class AsyncReqeustTaskContainer implements AsyncReqeustTaskContainable {
	private static AsyncReqeustTaskContainer instance;
	private ThreadPoolExecutor threadPool;
	
	private Map<String, WeakReference<StreamCancelable>> cancelableMap;
	
	private AsyncReqeustTaskContainer() {
		threadPool = new ThreadPoolExecutor(3, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		cancelableMap = new HashMap<>();
	}
	
	public static synchronized AsyncReqeustTaskContainer getInstance() {
		if (instance == null) {
			instance = new AsyncReqeustTaskContainer();
		}
		
		return instance;
	}
	
	@Override
	public void enqueue(TaskCancelRunnable runnable) {
		cancelableMap.put(runnable.getKey(), new WeakReference<StreamCancelable>(runnable));
		threadPool.execute(runnable);
	}
	
	@Override
	public void cancel(String key) {
		if (cancelableMap.get(key) != null) {
			if (cancelableMap.get(key).get() != null) {
				cancelableMap.get(key).get().cancel();
			}
			
			cancelableMap.remove(key);
		}
	}
}
