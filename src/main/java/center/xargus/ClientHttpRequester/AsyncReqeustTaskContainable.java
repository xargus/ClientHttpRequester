package center.xargus.ClientHttpRequester;

import center.xargus.ClientHttpRequester.connect.TaskCancelRunnable;

public interface AsyncReqeustTaskContainable {
	public void enqueue(TaskCancelRunnable runnable);
	void cancel(String key);
}
