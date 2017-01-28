package center.xargus.ClientHttpRequester.parser;

import java.io.InputStream;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class XmlParser<T> implements Parable<T>{

	@Override
	public T parse(InputStream inputStream, Class<T> classType) throws Exception {
		Serializer serializer = new Persister();
		return serializer.read(classType, inputStream);
	}

}
