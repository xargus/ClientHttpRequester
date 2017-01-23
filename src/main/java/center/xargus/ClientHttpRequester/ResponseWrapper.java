package center.xargus.ClientHttpRequester;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import center.xargus.ClientHttpRequester.interceptor.GzipDecompressInterceptor;
import center.xargus.ClientHttpRequester.interceptor.HttpResponseInterceptor;
import center.xargus.ClientHttpRequester.resulthandle.ResponseResultTypeHandlerFactory;

public class ResponseWrapper<T> {
	private List<HttpResponseInterceptor> responseInterceptorList;
	private ResponseResultTypeHandler<T> responseResultTypeHandler;

	public ResponseWrapper(List<HttpResponseInterceptor> responseInterceptorList, ResponseResultTypeHandler<T> responseResultTypeHandler) {
		this.responseInterceptorList = responseInterceptorList;
		this.responseResultTypeHandler = responseResultTypeHandler;
	}
	
	public Response<T> getResponse(Response<InputStream> response) throws IOException {
		responseInterceptorList.add(0, new GzipDecompressInterceptor());
		for (HttpResponseInterceptor interceptor : responseInterceptorList) {
			response = interceptor.intercept(response);
		}
		
		if (responseResultTypeHandler == null) {
			responseResultTypeHandler = ResponseResultTypeHandlerFactory.create();
		}
		return Response.convertNewTypeResponse(responseResultTypeHandler.handle(response.getBody()), response);
	}
}
