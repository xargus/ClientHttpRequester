package center.xargus.ClientHttpRequester.resulthandle;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import center.xargus.ClientHttpRequester.parser.Parable;
import center.xargus.ClientHttpRequester.parser.ParserFactory;

class ResponseResultInvocationHandler<T> implements InvocationHandler {

	private Class resultClassType;
	
	public ResponseResultInvocationHandler(Class resultClassType) {
		this.resultClassType = resultClassType;
	}
	
	@Override
	public T invoke(Object proxy, Method method, Object[] args) throws Throwable {
		T result;
		InputStream inputStream = (InputStream)args[0];
		
		Parable<T> parable = getParser(resultClassType.getAnnotations());
		if (parable != null) {
			result = parable.parse(inputStream, resultClassType);
		} else {
			result = (T) new DefaultResponseResultTypeHandler().handle(inputStream);
		}
		
		return result;
	}
	
	private Parable<T> getParser(Annotation[] annotations) {
		Parable<T> result = null;
		for (Annotation annotation : annotations) {
			Parable<T> parable = ParserFactory.create(annotation.getClass());
			if (parable != null) {
				result = parable;
				break;
			}
		}
		
		return result;
	}

}
