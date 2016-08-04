package center.xargus.ClientHttpRequester.interceptor;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import center.xargus.ClientHttpRequester.connect.Response;

public class GzipDecompressInterceptor implements HttpResponseInterceptor {

	@Override
	public Response<InputStream> intercept(Response<InputStream> response) {
		String contentType = response.getContentEncoding();
		
		System.out.println("GzipDecompressInterceptor, Response Content-Encoding : "+contentType);
		if (response.getResponseCode() != 200 || contentType == null || !contentType.contains("gzip")) {
			return response;
		}
		
		InputStream inputStream = response.getBody();
		
		try {
			GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(gzipInputStream);
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len;
			while ((len = bufferedInputStream.read(buffer)) >0 ) {
				outputStream.write(buffer, 0, len);
			}
			
			byte[] result = outputStream.toByteArray();
			
			bufferedInputStream.close();
			gzipInputStream.close();
			outputStream.close();
			
			return response.newBuilder()
					.setBody(new ByteArrayInputStream(result))
					.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}

}
