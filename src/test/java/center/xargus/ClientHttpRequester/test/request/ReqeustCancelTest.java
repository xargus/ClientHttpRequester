package center.xargus.ClientHttpRequester.test.request;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

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
		
		final CountDownLatch latch = new CountDownLatch(1);
		requestService.enqueue(request, new ClientHttpRequesterListener<String>() {
			
			@Override
			public void onCompletedRequest(Response<String> response) {
				System.out.println("completed");
				latch.countDown();
			}

			@Override
			public void onFailRequest(Response<InputStream> response, Exception e) {
				System.out.println("fail");
				latch.countDown();
			}
		});
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} finally {
			RequestService.cacnel("http://www.naver.com/");
		}
		
		
		try {
			latch.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
