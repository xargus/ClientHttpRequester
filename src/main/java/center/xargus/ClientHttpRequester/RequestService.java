package center.xargus.ClientHttpRequester;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import center.xargus.ClientHttpRequester.connect.DefaultResponseReader;
import center.xargus.ClientHttpRequester.connect.HttpReqeustWorker;
import center.xargus.ClientHttpRequester.connect.Request;
import center.xargus.ClientHttpRequester.connect.Response;
import center.xargus.ClientHttpRequester.interceptor.HttpResponseInterceptor;

public class RequestService<T> {
	private List<HttpResponseInterceptor> responseInterceptorList;
	private ResponseResultTypeHandler<T> responseResultTypeHandler;

	public Response<T> request(Request request) {
		request = request.newBuilder()
				.addHeader("Accept-Encoding", "gzip")
				.build();
		
		HttpReqeustWorker<InputStream> worker = new HttpReqeustWorker<>(request.getHeaderFields(), new DefaultResponseReader());
		Response<InputStream> response = worker.request(request.getDomain(), request.getParam(), request.getMethodType());
		
		return new ResponseWrapper<T>(responseInterceptorList, responseResultTypeHandler).getResponse(response);
	}
	
	public void enqueue(Request request, ClientHttpRequesterListener<T> listener) {
		request = request.newBuilder()
				.addHeader("Accept-Encoding", "gzip")
				.build();
		
		AsyncReqeuster<T> asyncReqeuster = new AsyncReqeuster<T>(request, new ResponseWrapper<T>(responseInterceptorList, responseResultTypeHandler), listener);
		ThreadPoolExecutorProvider.getInstance().enqueue(asyncReqeuster);
	}
	
	private RequestService(Builder<T> builder) {
		this.responseInterceptorList = builder.responseInterceptorList;
		this.responseResultTypeHandler = builder.responseResultTypeHandler;
	}
	
	public static class Builder<T> {
		private List<HttpResponseInterceptor> responseInterceptorList;
		private ResponseResultTypeHandler<T> responseResultTypeHandler;
		
		@SuppressWarnings("unchecked")
		public Builder() {
			responseInterceptorList = new ArrayList<>();
			responseResultTypeHandler = (ResponseResultTypeHandler<T>) new DefaultResponseResultTypeHandler();
		}
		
		public Builder<T> addHttpResponseInterceptor(HttpResponseInterceptor interceptor) {
			responseInterceptorList.add(interceptor);
			return this;
		}
		
		public Builder<T> addResponseResultTypeHandler(ResponseResultTypeHandler<T> reponseResultTypeHandler) {
			this.responseResultTypeHandler = reponseResultTypeHandler;
			return this;
		}
		
		public RequestService<T> build() {
			return new RequestService<T>(this);
		}
	}
	
	private class AsyncReqeuster<K> implements Runnable {
		private Request request;
		private ResponseWrapper<K> responseWrapper;
		private ClientHttpRequesterListener<K> listener;
		public AsyncReqeuster(Request request, ResponseWrapper<K> responseWrapper, ClientHttpRequesterListener<K> listener) {
			this.request = request;
			this.responseWrapper = responseWrapper;
			this.listener = listener;
		}
		@Override
		public void run() {
			HttpReqeustWorker<InputStream> worker = new HttpReqeustWorker<>(request.getHeaderFields(), new DefaultResponseReader());
			Response<InputStream> response = worker.request(request.getDomain(), request.getParam(), request.getMethodType());
			
			listener.onCompletedRequest(responseWrapper.getResponse(response));
		}
		
	}
}
