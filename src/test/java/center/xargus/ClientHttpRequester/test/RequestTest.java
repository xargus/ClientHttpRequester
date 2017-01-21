package center.xargus.ClientHttpRequester.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import center.xargus.ClientHttpRequester.ClientHttpRequesterListener;
import center.xargus.ClientHttpRequester.Request;
import center.xargus.ClientHttpRequester.RequestMethodType;
import center.xargus.ClientHttpRequester.RequestService;
import center.xargus.ClientHttpRequester.Response;
import center.xargus.ClientHttpRequester.exception.RequestMethodNotFoundException;
import center.xargus.ClientHttpRequester.exception.RequestUrlNotCorrectException;

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
		Response<String> response;
		
		try {
			response = requestService.request(request);
			
			System.out.println(response.getBody());
			assertEquals(response.getResponseCode(),200);
		} catch (RequestMethodNotFoundException e) {
			e.printStackTrace();
		} catch (RequestUrlNotCorrectException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

			@Override
			public void onFailRequest(Response<InputStream> response, Exception e) {
				System.out.println("fail");
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
