package center.xargus.ClientHttpRequester.resulthandle;

import java.io.InputStream;

import center.xargus.ClientHttpRequester.ResponseResultTypeHandler;

class DummyResponseResultTypeHandler<T> implements ResponseResultTypeHandler<T>{

	public Class getGenericClass(T...t) {
		return t.getClass().getComponentType();
	}
	
	@Override
	public T handle(InputStream response) throws Exception {
		return null;
	}

}
