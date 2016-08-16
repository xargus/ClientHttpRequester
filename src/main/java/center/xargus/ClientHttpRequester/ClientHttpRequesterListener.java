package center.xargus.ClientHttpRequester;

import center.xargus.ClientHttpRequester.connect.Response;

public interface ClientHttpRequesterListener<T> {
	void onCompletedRequest(Response<T> response);
}
