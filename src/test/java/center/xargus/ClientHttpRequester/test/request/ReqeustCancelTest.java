package center.xargus.ClientHttpRequester.test.request;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import center.xargus.ClientHttpRequester.Request;
import center.xargus.ClientHttpRequester.RequestClient;
import center.xargus.ClientHttpRequester.RequestClientListener;
import center.xargus.ClientHttpRequester.RequestMethodType;
import center.xargus.ClientHttpRequester.Response;

public class ReqeustCancelTest {

	@Test
	public void asyncRequestCancelTest() {
		Request request = new Request.Builder()
				.setDomain("http://www.naver.com/")
				.setRequestMethodType(RequestMethodType.GET)
				.build();
		
		RequestClient<String> requestService = 
				new RequestClient.Builder<String>(String.class)
				.setResponseResultTypeHandler(new TestCancelResponseResultHandlerImpl())
				.build();
		
		final CountDownLatch latch = new CountDownLatch(1);
		requestService.enqueue(request, new RequestClientListener<String>() {
			
			@Override
			public void onCompletedRequest(Response<String> response) {
				System.out.println("completed");
				latch.countDown();
			}

			@Override
			public void onFailRequest(Response<String> response, Exception e) {
				System.out.println("fail");
				latch.countDown();
			}
		});
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} finally {
			requestService.cacnel("http://www.naver.com/");
		}
		
		
		try {
			latch.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
