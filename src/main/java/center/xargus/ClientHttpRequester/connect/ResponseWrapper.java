package center.xargus.ClientHttpRequester.connect;

import java.io.InputStream;
import java.util.List;

import center.xargus.ClientHttpRequester.Response;
import center.xargus.ClientHttpRequester.ResponseResultTypeHandler;
import center.xargus.ClientHttpRequester.interceptor.GzipDecompressInterceptor;
import center.xargus.ClientHttpRequester.interceptor.HttpResponseInterceptor;

public class ResponseWrapper<T> {
	private List<HttpResponseInterceptor> responseInterceptorList;
	private ResponseResultTypeHandler<T> responseResultTypeHandler;

	public ResponseWrapper(List<HttpResponseInterceptor> responseInterceptorList, ResponseResultTypeHandler<T> responseResultTypeHandler) {
		this.responseInterceptorList = responseInterceptorList;
		this.responseResultTypeHandler = responseResultTypeHandler;
	}
	
	public Response<T> getResponse(Response<InputStream> response) {
		responseInterceptorList.add(0, new GzipDecompressInterceptor());
		for (HttpResponseInterceptor interceptor : responseInterceptorList) {
			response = interceptor.intercept(response);
		}
		
		return Response.convertNewTypeResponse(responseResultTypeHandler.handle(response.getBody()), response);
	}
}
