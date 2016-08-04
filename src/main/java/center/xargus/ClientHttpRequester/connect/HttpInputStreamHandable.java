package center.xargus.ClientHttpRequester.connect;

import java.io.IOException;
import java.io.InputStream;

public interface HttpInputStreamHandable<T> {
	T handle(InputStream inputStream) throws IOException;
}
