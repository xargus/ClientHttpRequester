package center.xargus.ClientHttpRequester.test.response;

import java.io.InputStream;

import center.xargus.ClientHttpRequester.ResponseResultTypeHandler;

public class BitmapResponseHandler implements ResponseResultTypeHandler<Bitmap>{

	@Override
	public Bitmap handle(InputStream response) throws Exception {
		response.close();
		return new Bitmap();
	}

}
