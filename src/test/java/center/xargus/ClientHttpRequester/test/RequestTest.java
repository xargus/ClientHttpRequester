package center.xargus.ClientHttpRequester.test;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import center.xargus.ClientHttpRequester.ClientHttpRequesterListener;
import center.xargus.ClientHttpRequester.RequestService;
import center.xargus.ClientHttpRequester.connect.Request;
import center.xargus.ClientHttpRequester.connect.RequestMethodType;
import center.xargus.ClientHttpRequester.connect.Response;

public class RequestTest {
	
	@Test
	public void reqeustTest() {
		Request request = new Request.Builder()
				.setDomain("http://www.naver.com/")
				.setRequestMethodType(RequestMethodType.GET)
				.build();
		
		RequestService<String> requestService = 
				new RequestService.Builder<String>()
				.build();
		Response<String> response = requestService.request(request);
		
		System.out.println(response.getBody());
		
		assertEquals(response.getResponseCode(),200);
	}
	
	@Test
	public void asyncRequestTest() {
		Request request = new Request.Builder()
				.setDomain("http://www.naver.com/")
				.setRequestMethodType(RequestMethodType.GET)
				.build();
		
		RequestService<String> requestService = 
				new RequestService.Builder<String>()
				.build();
		
		final CountDownLatch latch = new CountDownLatch(1);
		requestService.enqueue(request, new ClientHttpRequesterListener<String>() {
			
			@Override
			public void onCompletedRequest(Response<String> response) {
				System.out.println(response.getBody());
				assertEquals(response.getResponseCode(),200);
				latch.countDown();
			}
		});
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
