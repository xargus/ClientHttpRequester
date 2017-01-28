package center.xargus.ClientHttpRequester;

import java.io.IOException;
import java.io.InputStream;

import center.xargus.ClientHttpRequester.exception.RequestMethodNotFoundException;
import center.xargus.ClientHttpRequester.exception.RequestUrlNotCorrectException;

public interface HttpRequestable {
	Response<InputStream> request(Request request) throws RequestMethodNotFoundException, RequestUrlNotCorrectException, IOException;
}
