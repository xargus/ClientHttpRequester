package center.xargus.ClientHttpRequester;

public interface RequestClientListener<T> {
	void onCompletedRequest(Response<T> response);
	void onFailRequest(Response<T> response, Exception e);
}
