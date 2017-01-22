package center.xargus.ClientHttpRequester;

import java.io.InputStream;

import center.xargus.ClientHttpRequester.exception.RequestCanceledException;

public interface ResponseResultTypeHandler<T> {
	T handle(InputStream response) throws RequestCanceledException;
}
