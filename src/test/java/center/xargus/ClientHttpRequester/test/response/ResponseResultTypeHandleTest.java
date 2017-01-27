package center.xargus.ClientHttpRequester.test.response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;
import org.simpleframework.xml.core.Persister;

import com.google.gson.Gson;

import center.xargus.ClientHttpRequester.Request;
import center.xargus.ClientHttpRequester.RequestMethodType;
import center.xargus.ClientHttpRequester.RequestClient;
import center.xargus.ClientHttpRequester.Response;
import center.xargus.ClientHttpRequester.ResponseResultTypeHandler;
import center.xargus.ClientHttpRequester.exception.RequestMethodNotFoundException;
import center.xargus.ClientHttpRequester.exception.RequestUrlNotCorrectException;
import center.xargus.ClientHttpRequester.resulthandle.ResponseResultTypeProxyHandler;

public class ResponseResultTypeHandleTest {

	@Test
	public void responseJsonResultHandle() {
		try {
			ResponseResultTypeHandler<TestJsonDummyModel> proxyHandler = ResponseResultTypeProxyHandler.create(TestJsonDummyModel.class);
			TestJsonDummyModel model = new TestJsonDummyModel();
			model.setResult("Hello Json");
			String testJsonString = new Gson().toJson(model);
			
			System.out.println(proxyHandler.handle(new ByteArrayInputStream(testJsonString.getBytes())).getResult());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void responseXmlResultHandle() {
		try {
			ResponseResultTypeHandler<TestXmlDummyModel> proxyHandler = ResponseResultTypeProxyHandler.create(TestXmlDummyModel.class);
			TestXmlDummyModel model = new TestXmlDummyModel();
			model.setResult("Hello Xml");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			new Persister().write(model, outputStream);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
			
			System.out.println(proxyHandler.handle(inputStream).getResult());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void bitmapResponseHandler() {
		Request request = new Request.Builder()
				.setDomain("http://www.naver.com/")
				.setRequestMethodType(RequestMethodType.GET)
				.build();
		
		RequestClient<Bitmap> requestService = 
				new RequestClient.Builder<Bitmap>(Bitmap.class)
				.setResponseResultTypeHandler(new BitmapResponseHandler())
				.setHttpReqeuster(new DummyHttpRequester())
				.build();
		Response<Bitmap> response;
		
		try {
			response = requestService.request(request);
			
			System.out.println(response.getBody().toString());
		} catch (RequestMethodNotFoundException e) {
			e.printStackTrace();
		} catch (RequestUrlNotCorrectException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
