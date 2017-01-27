package center.xargus.ClientHttpRequester.reqeust;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class AsyncReqeustTaskContainer implements AsyncReqeustTaskContainable {
	private static AsyncReqeustTaskContainer instance;
	private ThreadPoolExecutor threadPool;
	
	private Map<String, WeakReference<Cancelable>> cancelableMap;
	
	private AsyncReqeustTaskContainer() {
		threadPool = new ThreadPoolExecutor(3, 4, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		cancelableMap = new HashMap<>();
	}
	
	static synchronized AsyncReqeustTaskContainer getInstance() {
		if (instance == null) {
			instance = new AsyncReqeustTaskContainer();
		}
		
		return instance;
	}
	
	@Override
	public void enqueue(TaskCancelRunnable runnable) {
		cancelableMap.put(runnable.getKey(), new WeakReference<Cancelable>(runnable));
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
