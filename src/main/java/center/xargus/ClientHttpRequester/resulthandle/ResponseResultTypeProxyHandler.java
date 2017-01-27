package center.xargus.ClientHttpRequester.resulthandle;

import java.io.InputStream;
import java.lang.annotation.Annotation;

import center.xargus.ClientHttpRequester.ResponseResultTypeHandler;
import center.xargus.ClientHttpRequester.parser.Parable;
import center.xargus.ClientHttpRequester.parser.ParserFactory;

public class ResponseResultTypeProxyHandler<T> implements ResponseResultTypeHandler<T> {

	private Class<T> resultClassType;

	public static <T> ResponseResultTypeHandler<T> create(Class<T> resultClassType) {
		return new ResponseResultTypeProxyHandler<>(resultClassType);
	}
	
	private ResponseResultTypeProxyHandler(Class<T> resultClassType) {
		this.resultClassType = resultClassType;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public T handle(InputStream response) throws Exception {
		T result;
		
		Parable<T> parable = getParser(resultClassType.getAnnotations());
		if (parable != null) {
			result = parable.parse(response, resultClassType);
		} else {
			result = (T) new DefaultResponseResultTypeHandler().handle(response);
		}
		
		return result;
	}

	private Parable<T> getParser(Annotation[] annotations) {
		Parable<T> result = null;
		for (Annotation annotation : annotations) {
			Parable<T> parable = ParserFactory.create(annotation.annotationType());
			if (parable != null) {
				result = parable;
				break;
			}
		}
		
		return result;
	}
}
