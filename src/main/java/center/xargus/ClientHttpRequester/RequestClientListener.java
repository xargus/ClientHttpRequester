package center.xargus.ClientHttpRequester;

import java.io.InputStream;

public interface RequestClientListener<T> {
	void onCompletedRequest(Response<T> response);
	void onFailRequest(Response<InputStream> response, Exception e);
}
