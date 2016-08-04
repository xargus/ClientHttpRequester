package center.xargus.ClientHttpRequester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DefaultResponseResultTypeHandler implements ResponseResultTypeHandler<String> {

	@Override
	public String handle(InputStream response) {
		InputStreamReader inReader = new InputStreamReader(response);
		BufferedReader reader = new BufferedReader(inReader);
		
		try {
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			
			return builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
