package center.xargus.ClientHttpRequester.reqeust;

interface AsyncReqeustTaskContainable {
	public void enqueue(TaskCancelCallable runnable, int priority);
	void cancel(String key);
}
