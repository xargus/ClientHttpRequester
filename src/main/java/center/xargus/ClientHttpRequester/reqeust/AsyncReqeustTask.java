package center.xargus.ClientHttpRequester.reqeust;

import java.net.HttpURLConnection;

import center.xargus.ClientHttpRequester.RequestClientListener;
import center.xargus.ClientHttpRequester.Response;
import center.xargus.ClientHttpRequester.exception.RequestCanceledException;

class AsyncReqeustTask<K> implements TaskCancelCallable<Void> {
	private RequestClientListener<K> listener;
	private AsyncReqeustTaskContainable taskRemovable;
	private RequestTask<K> requestTask;
	
	AsyncReqeustTask(RequestTask<K> requestTask, 
			RequestClientListener<K> listener, 
			AsyncReqeustTaskContainable taskRemovable) {
		this.requestTask = requestTask;
		this.listener = listener;
		this.taskRemovable = taskRemovable;
	}
	
	@Override
	public Void call() {
		try {
			Response<K> result = requestTask.call();
			if (result == null || result.getResponseCode() != HttpURLConnection.HTTP_OK) {
				listener.onFailRequest(result, null);
			} else {
				listener.onCompletedRequest(result);
			}
		} catch (RequestCanceledException exception) {
			System.out.println("reqeust cancel exception : "+getKey());
		} catch (Exception e) {
			listener.onFailRequest(null, e);
		} finally {
			taskRemovable.cancel(getKey());
			System.out.println("end task : "+getKey());
		}
		
		return null;
	}
	
	@Override
	public void cancel() {
		requestTask.cancel();
	}
	@Override
	public boolean isCanceled() {
		return requestTask.isCanceled();
	}
	@Override
	public String getKey() {
		return requestTask.getKey();
	}
}
