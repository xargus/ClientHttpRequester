package center.xargus.ClientHttpRequester.reqeust;

import java.io.IOException;

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
		RequestTask<T> requestTask = RequestTask.create(request, service);
		cancelable = requestTask;
		return requestTask.call();
	}
	
	public void cancel() {
		if (cancelable != null) {
			cancelable.cancel();
		}
	}
}
