package center.xargus.ClientHttpRequester.test;

import java.io.InputStream;

import org.junit.Test;

import center.xargus.ClientHttpRequester.ClientHttpRequesterListener;
import center.xargus.ClientHttpRequester.Request;
import center.xargus.ClientHttpRequester.RequestMethodType;
import center.xargus.ClientHttpRequester.RequestService;
import center.xargus.ClientHttpRequester.Response;

public class ReqeustCancelTest {

	@Test
	public void asyncRequestCancelTest() {
		Request request = new Request.Builder()
				.setDomain("http://www.naver.com/")
				.setRequestMethodType(RequestMethodType.GET)
				.build();
		
		RequestService<String> requestService = 
				new RequestService.Builder<String>()
				.addResponseResultTypeHandler(new TestResponseResultTypeHandlerImpl())
				.build();
		
		requestService.enqueue(request, new ClientHttpRequesterListener<String>() {
			
			@Override
			public void onCompletedRequest(Response<String> response) {
			}

			@Override
			public void onFailRequest(Response<InputStream> response, Exception e) {
			}
		});
		
		try {
			Thread.sleep(500);
			RequestService.cacnel("http://www.naver.com/");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
