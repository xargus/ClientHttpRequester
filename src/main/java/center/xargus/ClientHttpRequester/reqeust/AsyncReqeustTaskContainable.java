package center.xargus.ClientHttpRequester.reqeust;

interface AsyncReqeustTaskContainable {
	public void enqueue(TaskCancelRunnable runnable);
	void cancel(String key);
}
