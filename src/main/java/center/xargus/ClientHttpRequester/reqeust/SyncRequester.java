package center.xargus.ClientHttpRequester.reqeust;

import java.io.IOException;
import java.io.InputStream;

import center.xargus.ClientHttpRequester.Request;
import center.xargus.ClientHttpRequester.RequestClient;
import center.xargus.ClientHttpRequester.Response;
import center.xargus.ClientHttpRequester.exception.RequestMethodNotFoundException;
import center.xargus.ClientHttpRequester.exception.RequestUrlNotCorrectException;

public class SyncRequester<T> {

	private RequestClient<T> service;
	private Cancelable cancelable;

	public SyncRequester(RequestClient<T> service) {
		this.service = service;
	}
	
	public Response<T> request(Request request) throws RequestMethodNotFoundException, RequestUrlNotCorrectException, IOException, Exception {
		Response<InputStream> response = service.getHttpRequestable().request(request);
		if (response.getBody() instanceof Cancelable) {
			cancelable = (Cancelable) response.getBody();
		}
		
		ResponseWrapper<T> responseWrapper = new ResponseWrapper<T>(service.getResponseInterceptorList(), 
				service.getResponseResultTypeHandler(), 
				service.getResultClassType());
		return responseWrapper.getResponse(response);
	}
	
	public void cancel() {
		if (cancelable != null) {
			cancelable.cancel();
		}
	}
}
