package center.xargus.ClientHttpRequester;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import center.xargus.ClientHttpRequester.exception.RequestMethodNotFoundException;
import center.xargus.ClientHttpRequester.exception.RequestUrlNotCorrectException;
import center.xargus.ClientHttpRequester.interceptor.HttpResponseInterceptor;
import center.xargus.ClientHttpRequester.reqeust.AsyncReqeuster;
import center.xargus.ClientHttpRequester.reqeust.HttpReqeustWorker;
import center.xargus.ClientHttpRequester.reqeust.SyncRequester;

public class RequestService<T> {
	private List<HttpResponseInterceptor> responseInterceptorList;
	private ResponseResultTypeHandler<T> responseResultTypeHandler;
	private Class<T> resultClassType;
	private HttpRequestable httpRequestable;
	private AsyncReqeuster<T> asyncReqeuster;
	private SyncRequester<T> syncRequester;

	public Response<T> request(Request request) throws RequestMethodNotFoundException, RequestUrlNotCorrectException, IOException, Exception {
		request = request.newBuilder()
				.addHeader("Accept-Encoding", "gzip")
				.build();
		
		if (syncRequester == null) {
			syncRequester = new SyncRequester<>(this);
		}
		return syncRequester.request(request);
	}
	
	public void enqueue(Request request, ClientHttpRequesterListener<T> listener) {
		request = request.newBuilder()
				.addHeader("Accept-Encoding", "gzip")
				.build();
		
		if (asyncReqeuster == null) {
			asyncReqeuster = new AsyncReqeuster<>(this);
		}
		asyncReqeuster.request(request, listener);
	}
	
	public void cacnel(String url) {
		if (asyncReqeuster != null) {
			asyncReqeuster.cancel(url);
		}
	}
	
	public void cancel() {
		if (syncRequester != null) {
			syncRequester.cancel();
		}
	}
	
	public List<HttpResponseInterceptor> getResponseInterceptorList() {
		return responseInterceptorList;
	}

	public ResponseResultTypeHandler<T> getResponseResultTypeHandler() {
		return responseResultTypeHandler;
	}

	public Class<T> getResultClassType() {
		return resultClassType;
	}

	public HttpRequestable getHttpRequestable() {
		return httpRequestable;
	}

	private RequestService(Builder<T> builder) {
		this.responseInterceptorList = builder.responseInterceptorList;
		this.responseResultTypeHandler = builder.responseResultTypeHandler;
		this.resultClassType = builder.resultClassType;
		this.httpRequestable = builder.httpRequestable;
	}
	
	public static class Builder<T> {
		private List<HttpResponseInterceptor> responseInterceptorList;
		private ResponseResultTypeHandler<T> responseResultTypeHandler;
		private Class<T> resultClassType;
		private HttpRequestable httpRequestable;
		
		public Builder(Class<T> resultClassType) {
			this.resultClassType = resultClassType;
			this.responseInterceptorList = new ArrayList<>();
			this.httpRequestable = new HttpReqeustWorker();
		}
		
		public Builder<T> addHttpResponseInterceptor(HttpResponseInterceptor interceptor) {
			responseInterceptorList.add(interceptor);
			return this;
		}
		
		public Builder<T> setResponseResultTypeHandler(ResponseResultTypeHandler<T> reponseResultTypeHandler) {
			this.responseResultTypeHandler = reponseResultTypeHandler;
			return this;
		}
		
		public Builder<T> setHttpReqeuster(HttpRequestable httpRequestable) {
			this.httpRequestable = httpRequestable;
			return this;
		}
		
		public RequestService<T> build() {
			return new RequestService<T>(this);
		}
	}
}
