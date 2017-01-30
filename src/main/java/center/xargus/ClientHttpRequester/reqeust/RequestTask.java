package center.xargus.ClientHttpRequester.reqeust;

import java.io.InputStream;
import java.net.HttpURLConnection;

import center.xargus.ClientHttpRequester.HttpRequestable;
import center.xargus.ClientHttpRequester.Request;
import center.xargus.ClientHttpRequester.RequestClient;
import center.xargus.ClientHttpRequester.Response;

class RequestTask<K> implements TaskCancelCallable<Response<K>> {
	private Request request;
	private ResponseWrapper<K> responseWrapper;
	private Cancelable cancelable;
	private Object lockObj;
	private HttpRequestable httpRequestable;
	
	public static <K> RequestTask<K> create(Request request, RequestClient<K> requestClient) {
		return new RequestTask<>(request, 
				requestClient.getHttpRequestable(), 
				new ResponseWrapper<>(requestClient.getResponseInterceptorList(), 
						requestClient.getResponseResultTypeHandler(), 
						requestClient.getResultClassType()));
	}
	
	private RequestTask(Request request, 
			HttpRequestable httpRequestable, 
			ResponseWrapper<K> responseWrapper) {
		this.request = request;
		this.httpRequestable = httpRequestable;
		this.responseWrapper = responseWrapper;
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
	
	@SuppressWarnings("static-access")
	@Override
	public Response<K> call() throws Exception {
		if (cancelable.isCanceled()) {
			return null;
		}

		Response<InputStream> response = httpRequestable.request(request);
		if (response.getResponseCode() != HttpURLConnection.HTTP_OK) {
			Response<K> failResponse = response.convertNewTypeResponse(null, response);
			return failResponse;
		}

		if (response.getBody() instanceof Cancelable) {
			setCancelable((Cancelable) response.getBody());
		}

		if (!cancelable.isCanceled()) {
			Response<K> resultResponse = responseWrapper.getResponse(response);
			return resultResponse;
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
