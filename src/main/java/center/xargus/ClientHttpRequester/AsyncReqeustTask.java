package center.xargus.ClientHttpRequester;

import java.io.InputStream;
import java.net.HttpURLConnection;

import center.xargus.ClientHttpRequester.connect.HttpReqeustWorker;
import center.xargus.ClientHttpRequester.connect.StreamCancelable;
import center.xargus.ClientHttpRequester.connect.TaskCancelRunnable;
import center.xargus.ClientHttpRequester.exception.RequestCanceledException;

public class AsyncReqeustTask<K> implements TaskCancelRunnable {
	private Request request;
	private ResponseWrapper<K> responseWrapper;
	private ClientHttpRequesterListener<K> listener;
	private StreamCancelable cancelable;
	private AsyncReqeustTaskContainable taskRemovable;
	private Object lockObj;
	
	public AsyncReqeustTask(Request request, ResponseWrapper<K> responseWrapper, ClientHttpRequesterListener<K> listener, AsyncReqeustTaskContainable taskRemovable) {
		this.request = request;
		this.responseWrapper = responseWrapper;
		this.listener = listener;
		this.taskRemovable = taskRemovable;
		this.lockObj = new Object();
		
		cancelable = new StreamCancelable() {
			private volatile boolean isCanceled = false;
			
			@Override
			public void cancel() {
				isCanceled = true;
			}

			@Override
			public boolean isCanceled() {
				return isCanceled;
			}
		};
	}
	
	@Override
	public void run() {
		if (cancelable.isCanceled()) {
			return;
		}
		
		try {
			HttpRequestable worker = new HttpReqeustWorker(request);
			Response<InputStream> response = worker.request();
			if (response.getBody() instanceof StreamCancelable) {
				setCancelable((StreamCancelable)response.getBody());
			}
			
			if (response.getResponseCode() != HttpURLConnection.HTTP_OK) {	
				listener.onFailRequest(response, null);
				return;
			}
			
			Response<K> resultResponse = responseWrapper.getResponse(response);
			if (!cancelable.isCanceled()) {
				listener.onCompletedRequest(resultResponse);
			}
		} catch (RequestCanceledException exception) {
			System.out.println("reqeust cancel exception : "+getKey());
			listener.onFailRequest(null, exception);
		} catch (Exception e) {
			listener.onFailRequest(null, e);
		} finally {
			taskRemovable.cancel(getKey());
			System.out.println("end task : "+getKey());
		}
	}
	
	private void setCancelable(StreamCancelable streamCancelable) {
		synchronized (lockObj) {
			cancelable = streamCancelable;
		}
	}
	
	@Override
	public void cancel() {
		synchronized (lockObj) {
			cancelable.cancel();
		}
	}
	@Override
	public boolean isCanceled() {
		return cancelable.isCanceled();
	}
	@Override
	public String getKey() {
		return request.getDomain();
	}
}
