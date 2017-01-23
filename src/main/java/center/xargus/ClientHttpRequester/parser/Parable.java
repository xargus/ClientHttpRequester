package center.xargus.ClientHttpRequester.parser;

import java.io.InputStream;

public interface Parable<T> {
	T parse(InputStream inputStream);
}
