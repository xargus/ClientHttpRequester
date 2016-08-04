package center.xargus.ClientHttpRequester;

import java.io.InputStream;

public interface ResponseResultTypeHandler<T> {
	T handle(InputStream response);
}
