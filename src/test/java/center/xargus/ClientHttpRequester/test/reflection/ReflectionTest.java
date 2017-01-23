package center.xargus.ClientHttpRequester.test.reflection;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Test;

import center.xargus.ClientHttpRequester.ResponseResultTypeHandler;
import center.xargus.ClientHttpRequester.resulthandle.ResponseResultTypeHandlerFactory;

public class ReflectionTest {

	@Test
	public void reflectionTest() {
		ResponseResultTypeHandler<TestDummyModel> responseResultTypeHandler = ResponseResultTypeHandlerFactory.create();
		try {
			responseResultTypeHandler.handle(new ByteArrayInputStream("!!".getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
