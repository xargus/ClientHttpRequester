package center.xargus.ClientHttpRequester.test.priority;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import center.xargus.ClientHttpRequester.HttpRequestable;
import center.xargus.ClientHttpRequester.Request;
import center.xargus.ClientHttpRequester.Response;
import center.xargus.ClientHttpRequester.exception.RequestMethodNotFoundException;
import center.xargus.ClientHttpRequester.exception.RequestUrlNotCorrectException;

public class TestDummyHttpReqeuster implements HttpRequestable {

	@Override
	public Response<InputStream> request(Request request)
			throws RequestMethodNotFoundException, RequestUrlNotCorrectException, IOException {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new Response.Builder<InputStream>()
				.setBody(new ByteArrayInputStream(request.getDomain().getBytes()))
				.setResponseCode(200)
				.build();
	}

}
