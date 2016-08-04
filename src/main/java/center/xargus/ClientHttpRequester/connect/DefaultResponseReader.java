package center.xargus.ClientHttpRequester.connect;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DefaultResponseReader implements HttpInputStreamHandable<InputStream> {

	@Override
	public InputStream handle(InputStream inputStream) throws IOException {
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int bufferedLength = 1024;
		byte[] buffer = new byte[bufferedLength];
		int length;
		while ((length = inputStream.read(buffer, 0, bufferedLength)) > 0) {
			outputStream.write(buffer, 0, length);
		}
		
		byte[] result = outputStream.toByteArray();
		outputStream.close();
		
//		System.out.println("DefaultResponseReader, "+new String(result));
		
		return new ByteArrayInputStream(result);
	}
}
