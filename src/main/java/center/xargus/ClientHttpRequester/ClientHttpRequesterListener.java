package center.xargus.ClientHttpRequester;

import java.io.InputStream;

public interface ClientHttpRequesterListener<T> {
	void onCompletedRequest(Response<T> response);
	void onFailRequest(Response<InputStream> response, Exception e);
}
