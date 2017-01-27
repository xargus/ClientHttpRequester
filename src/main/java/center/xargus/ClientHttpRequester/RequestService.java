package center.xargus.ClientHttpRequester;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import center.xargus.ClientHttpRequester.connect.HttpReqeustWorker;
import center.xargus.ClientHttpRequester.exception.RequestMethodNotFoundException;
import center.xargus.ClientHttpRequester.exception.RequestUrlNotCorrectException;
import center.xargus.ClientHttpRequester.interceptor.HttpResponseInterceptor;

public class RequestService<T> {
	private List<HttpResponseInterceptor> responseInterceptorList;
	private ResponseResultTypeHandler<T> responseResultTypeHandler;
	private Class<T> resultClassType;
	private HttpRequestable httpRequestable;

	public Response<T> request(Request request) throws RequestMethodNotFoundException, RequestUrlNotCorrectException, Exception {
		request = request.newBuilder()
				.addHeader("Accept-Encoding", "gzip")
				.build();
		
		Response<InputStream> response = httpRequestable.request(request);
		
		return new ResponseWrapper<T>(responseInterceptorList, responseResultTypeHandler, resultClassType).getResponse(response);
	}
	
	public void enqueue(Request request, ClientHttpRequesterListener<T> listener) {
		request = request.newBuilder()
				.addHeader("Accept-Encoding", "gzip")
				.build();
		
		AsyncReqeustTask<T> task = new AsyncReqeustTask<T>(request,
				httpRequestable,
				new ResponseWrapper<T>(responseInterceptorList, responseResultTypeHandler, resultClassType), 
				listener, 
				AsyncReqeustTaskContainer.getInstance());
		AsyncReqeustTaskContainer.getInstance().enqueue(task);
	}
	
	public static void cacnel(String key) {
		AsyncReqeustTaskContainer.getInstance().cancel(key);
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
