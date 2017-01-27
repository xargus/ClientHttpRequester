package center.xargus.ClientHttpRequester;

import java.io.InputStream;
import java.util.List;

import center.xargus.ClientHttpRequester.interceptor.GzipDecompressInterceptor;
import center.xargus.ClientHttpRequester.interceptor.HttpResponseInterceptor;
import center.xargus.ClientHttpRequester.resulthandle.ResponseResultTypeProxyHandler;

public class ResponseWrapper<T> {
	private List<HttpResponseInterceptor> responseInterceptorList;
	private ResponseResultTypeHandler<T> responseResultTypeHandler;
	private Class<T> resultClassType;

	public ResponseWrapper(List<HttpResponseInterceptor> responseInterceptorList, ResponseResultTypeHandler<T> responseResultTypeHandler, Class<T> resultClassType) {
		this.responseInterceptorList = responseInterceptorList;
		this.responseResultTypeHandler = responseResultTypeHandler;
		this.resultClassType = resultClassType;
	}
	
	public Response<T> getResponse(Response<InputStream> response) throws Exception {
		responseInterceptorList.add(0, new GzipDecompressInterceptor());
		for (HttpResponseInterceptor interceptor : responseInterceptorList) {
			response = interceptor.intercept(response);
		}
		
		if (responseResultTypeHandler == null) {
			responseResultTypeHandler = ResponseResultTypeProxyHandler.create(resultClassType);
		}
		return Response.convertNewTypeResponse(responseResultTypeHandler.handle(response.getBody()), response);
	}
}
