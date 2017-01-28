package center.xargus.ClientHttpRequester.reqeust;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

class AsyncReqeustTaskContainer implements AsyncReqeustTaskContainable {
	private final int CORE_THREAD_SIZE = 1;
	private final int MAX_THREAD_SIZE = 1;
	
	private static AsyncReqeustTaskContainer instance;
	private PriorityThreadPoolExecutor threadPool;
	
	private Map<String, WeakReference<Cancelable>> cancelableMap;
	
	private AsyncReqeustTaskContainer() {
		threadPool = new PriorityThreadPoolExecutor(CORE_THREAD_SIZE, MAX_THREAD_SIZE, 60, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());
		cancelableMap = new HashMap<>();
	}
	
	static synchronized AsyncReqeustTaskContainer getInstance() {
		if (instance == null) {
			instance = new AsyncReqeustTaskContainer();
		}
		
		return instance;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void enqueue(TaskCancelCallable callable, int priority) {
		cancelableMap.put(callable.getKey(), new WeakReference<Cancelable>(callable));
		threadPool.submit(callable, priority);
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
