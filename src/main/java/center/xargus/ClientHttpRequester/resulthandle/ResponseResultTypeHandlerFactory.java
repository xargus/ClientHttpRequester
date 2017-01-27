package center.xargus.ClientHttpRequester.resulthandle;

import java.lang.reflect.Proxy;

import center.xargus.ClientHttpRequester.ResponseResultTypeHandler;

class ResponseResultTypeHandlerFactory {
 
	@SuppressWarnings("unchecked")
	public static <T> ResponseResultTypeHandler<T> create() {
		DummyResponseResultTypeHandler<T> dummy = new DummyResponseResultTypeHandler<>();
		return (ResponseResultTypeHandler<T>)Proxy.newProxyInstance(dummy.getClass().getClassLoader(), 
				dummy.getClass().getInterfaces(), 
				new ResponseResultInvocationHandler<T>(dummy.getGenericClass()));
	}
}
