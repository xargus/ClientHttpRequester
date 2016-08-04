package center.xargus.ClientHttpRequester.test;

import static org.junit.Assert.*;

import org.junit.Test;

import center.xargus.ClientHttpRequester.RequestService;
import center.xargus.ClientHttpRequester.RequestService.Builder;
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
}
