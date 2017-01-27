package center.xargus.ClientHttpRequester.parser;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;

public class JsonParser<T> implements Parable<T>{

	@Override
	public T parse(InputStream inputStream, Class<T> classType) throws Exception {
		return new Gson().fromJson(new InputStreamReader(inputStream), classType);
	}

}
