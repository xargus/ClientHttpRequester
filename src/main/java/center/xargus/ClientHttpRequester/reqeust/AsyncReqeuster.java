package center.xargus.ClientHttpRequester.reqeust;

import center.xargus.ClientHttpRequester.RequestClientListener;
import center.xargus.ClientHttpRequester.Request;
import center.xargus.ClientHttpRequester.RequestClient;

public class AsyncReqeuster<T> {

	private RequestClient<T> service;
	public AsyncReqeuster(RequestClient<T> service) {
		this.service = service;
	}
	
	public void request(Request request, RequestClientListener<T> listener) {
		AsyncReqeustTask<T> task = new AsyncReqeustTask<T>(request,
				service.getHttpRequestable(),
				new ResponseWrapper<T>(service.getResponseInterceptorList(), service.getResponseResultTypeHandler(), service.getResultClassType()), 
				listener,
				AsyncReqeustTaskContainer.getInstance());
		AsyncReqeustTaskContainer.getInstance().enqueue(task, request.getPriority());
	}

	public void cancel(String url) {
		AsyncReqeustTaskContainer.getInstance().cancel(url);
	}
}
