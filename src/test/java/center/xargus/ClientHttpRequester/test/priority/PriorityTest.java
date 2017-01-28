package center.xargus.ClientHttpRequester.test.priority;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import center.xargus.ClientHttpRequester.Request;
import center.xargus.ClientHttpRequester.RequestClient;
import center.xargus.ClientHttpRequester.RequestClientListener;
import center.xargus.ClientHttpRequester.RequestMethodType;
import center.xargus.ClientHttpRequester.Response;

public class PriorityTest implements RequestClientListener<String>{

	private CountDownLatch latch;

	//AsyncReqeustTaskContainer의 CORE_THREAD_SIZE, MAX_THREAD_SIZE size 1로 수정해야함.
	@Test
	public void priorityTest() {
		Request request1 = new Request.Builder()
				.setDomain("http://1111/")
				.setRequestMethodType(RequestMethodType.GET)
				.build();
		Request request2 = new Request.Builder()
				.setDomain("http://2222/")
				.setRequestMethodType(RequestMethodType.GET)
				.build();
		Request request3 = new Request.Builder()
				.setDomain("http://3333/")
				.setRequestMethodType(RequestMethodType.GET)
				.setPriority(Request.PRIORITY_HIGH)
				.build();
		
		RequestClient<String> requestService = 
				new RequestClient.Builder<String>(String.class)
				.setHttpReqeuster(new TestDummyHttpReqeuster())
				.build();
		
		latch = new CountDownLatch(3);
		requestService.enqueue(request1, this);
		requestService.enqueue(request2, this);
		requestService.enqueue(request3, this);
		
		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCompletedRequest(Response<String> response) {
		System.out.println("success : "+response.getBody());
		latch.countDown();
	}

	@Override
	public void onFailRequest(Response<InputStream> response, Exception e) {
		e.printStackTrace();
		latch.countDown();
	}
}
