package center.xargus.ClientHttpRequester.test.response;

import java.io.IOException;
import java.io.InputStream;

import center.xargus.ClientHttpRequester.HttpRequestable;
import center.xargus.ClientHttpRequester.Request;
import center.xargus.ClientHttpRequester.Response;
import center.xargus.ClientHttpRequester.exception.RequestMethodNotFoundException;
import center.xargus.ClientHttpRequester.exception.RequestUrlNotCorrectException;

public class DummyHttpRequester implements HttpRequestable {

	@Override
	public Response<InputStream> request(Request request)
			throws RequestMethodNotFoundException, RequestUrlNotCorrectException, IOException {
		return new Response.Builder<InputStream>().build();
	}

}
