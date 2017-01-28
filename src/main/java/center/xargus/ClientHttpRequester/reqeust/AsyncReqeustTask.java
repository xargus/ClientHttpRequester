package center.xargus.ClientHttpRequester.reqeust;

import java.io.InputStream;
import java.net.HttpURLConnection;

import center.xargus.ClientHttpRequester.RequestClientListener;
import center.xargus.ClientHttpRequester.HttpRequestable;
import center.xargus.ClientHttpRequester.Request;
import center.xargus.ClientHttpRequester.Response;
import center.xargus.ClientHttpRequester.exception.RequestCanceledException;

class AsyncReqeustTask<K> implements TaskCancelCallable<Void> {
	private Request request;
	private ResponseWrapper<K> responseWrapper;
	private RequestClientListener<K> listener;
	private Cancelable cancelable;
	private AsyncReqeustTaskContainable taskRemovable;
	private Object lockObj;
	private HttpRequestable httpRequestable;
	
	AsyncReqeustTask(Request request, 
			HttpRequestable httpRequestable, 
			ResponseWrapper<K> responseWrapper, 
			RequestClientListener<K> listener, 
			AsyncReqeustTaskContainable taskRemovable) {
		this.request = request;
		this.httpRequestable = httpRequestable;
		this.responseWrapper = responseWrapper;
		this.listener = listener;
		this.taskRemovable = taskRemovable;
		this.lockObj = new Object();
		
		cancelable = new Cancelable() {
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
	public Void call() {
		if (cancelable.isCanceled()) {
			return null;
		}
		
		try {
			Response<InputStream> response = httpRequestable.request(request);
			if (response.getResponseCode() != HttpURLConnection.HTTP_OK) {	
				listener.onFailRequest(response, null);
				return null;
			}
			
			if (response.getBody() instanceof Cancelable) {
				setCancelable((Cancelable)response.getBody());
			}
			
			Response<K> resultResponse = responseWrapper.getResponse(response);
			if (!cancelable.isCanceled()) {
				listener.onCompletedRequest(resultResponse);
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
	
	private void setCancelable(Cancelable streamCancelable) {
		synchronized (lockObj) {
			if (cancelable.isCanceled()) {
				streamCancelable.cancel();
			}
			
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
