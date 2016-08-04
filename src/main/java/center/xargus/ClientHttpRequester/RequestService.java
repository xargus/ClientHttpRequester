package center.xargus.ClientHttpRequester;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import center.xargus.ClientHttpRequester.connect.DefaultResponseReader;
import center.xargus.ClientHttpRequester.connect.HttpReqeustWorker;
import center.xargus.ClientHttpRequester.connect.Request;
import center.xargus.ClientHttpRequester.connect.Response;
import center.xargus.ClientHttpRequester.interceptor.GzipDecompressInterceptor;
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
		
		responseInterceptorList.add(0, new GzipDecompressInterceptor());
		for (HttpResponseInterceptor interceptor : responseInterceptorList) {
			response = interceptor.intercept(response);
		}
		
		return Response.convertNewTypeResponse(responseResultTypeHandler.handle(response.getBody()), response);
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
}
