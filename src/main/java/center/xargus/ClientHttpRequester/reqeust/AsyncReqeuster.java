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
		AsyncReqeustTask<T> task = new AsyncReqeustTask<T>(RequestTask.create(request, service),
				listener,
				AsyncReqeustTaskContainer.getInstance());
		AsyncReqeustTaskContainer.getInstance().enqueue(task, request.getPriority());
	}

	public void cancel(String url) {
		AsyncReqeustTaskContainer.getInstance().cancel(url);
	}
}
