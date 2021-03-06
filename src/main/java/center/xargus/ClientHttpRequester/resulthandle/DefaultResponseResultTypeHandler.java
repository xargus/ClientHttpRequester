package center.xargus.ClientHttpRequester.resulthandle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import center.xargus.ClientHttpRequester.ResponseResultTypeHandler;

class DefaultResponseResultTypeHandler implements ResponseResultTypeHandler<String> {

	@Override
	public String handle(InputStream response) throws Exception {
		InputStreamReader inReader = new InputStreamReader(response);
		BufferedReader reader = new BufferedReader(inReader);
		StringBuilder builder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}

		return builder.toString();

	}

}
