package center.xargus.ClientHttpRequester;

import java.io.InputStream;
import java.net.HttpURLConnection;

import center.xargus.ClientHttpRequester.connect.HttpReqeustWorker;
import center.xargus.ClientHttpRequester.connect.ResponseWrapper;
import center.xargus.ClientHttpRequester.connect.StreamCancelable;
import center.xargus.ClientHttpRequester.connect.TaskCancelRunnable;

public class AsyncReqeustTask<K> implements TaskCancelRunnable {
	private Request request;
	private ResponseWrapper<K> responseWrapper;
	private ClientHttpRequesterListener<K> listener;
	private StreamCancelable cancelable;
	private AsyncReqeustTaskContainable taskRemovable;
	
	public AsyncReqeustTask(Request request, ResponseWrapper<K> responseWrapper, ClientHttpRequesterListener<K> listener, AsyncReqeustTaskContainable taskRemovable) {
		this.request = request;
		this.responseWrapper = responseWrapper;
		this.listener = listener;
		this.taskRemovable = taskRemovable;
		
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
				if (!cancelable.isCanceled()) {
					cancelable = (StreamCancelable) response.getBody();
				} else {
					return;
				}
			}
			
			if (response.getResponseCode() != HttpURLConnection.HTTP_OK) {	
				listener.onFailRequest(response, null);
				return;
			}
			
			Response<K> resultResponse = responseWrapper.getResponse(response);
			if (!cancelable.isCanceled()) {
				listener.onCompletedRequest(resultResponse);
			}
		} catch (Exception e) {
			listener.onFailRequest(null, e);
		} finally {
			taskRemovable.cancel(getKey());
		}
	}
	
	@Override
	public void cancel() {
		cancelable.cancel();
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
