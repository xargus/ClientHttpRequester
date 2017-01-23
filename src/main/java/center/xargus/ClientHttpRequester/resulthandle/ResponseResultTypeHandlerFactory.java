package center.xargus.ClientHttpRequester.resulthandle;

import java.lang.reflect.Proxy;

import center.xargus.ClientHttpRequester.ResponseResultTypeHandler;

public class ResponseResultTypeHandlerFactory {
 
	@SuppressWarnings("unchecked")
	public static <T> ResponseResultTypeHandler<T> create() {
		DummyResponseResultTypeHandler<T> dummy = new DummyResponseResultTypeHandler<>();
		System.out.println(dummy.getGenericClass()+"");
		return (ResponseResultTypeHandler<T>)Proxy.newProxyInstance(dummy.getClass().getClassLoader(), 
				dummy.getClass().getInterfaces(), 
				new ResponseResultInvocationHandler<T>(dummy.getGenericClass()));
	}
}
