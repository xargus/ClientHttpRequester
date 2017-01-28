package center.xargus.ClientHttpRequester.interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import center.xargus.ClientHttpRequester.Response;

public class GzipDecompressInterceptor implements HttpResponseInterceptor {

	public static final String GZIP_CONTENT_ENCODING = "gzip";
	
	@Override
	public Response<InputStream> intercept(Response<InputStream> response) {
		String contentEncoding = response.getContentEncoding();
		
		System.out.println("GzipDecompressInterceptor, Response Content-Encoding : "+contentEncoding);
		if (response.getResponseCode() != HttpURLConnection.HTTP_OK || contentEncoding == null || !contentEncoding.contains(GZIP_CONTENT_ENCODING)) {
			return response;
		}
		
		InputStream inputStream = response.getBody();
		
		try {
			Map<String,List<String>> headerFields = response.getHeaderFields();
			Map<String,List<String>> newHeaderFields = new HashMap<>();
			copyHeader(headerFields, newHeaderFields);
			
			newHeaderFields.remove(Response.CONTENT_ENCODING_KEY);
			
			return response.newBuilder()
					.setBody(new GZIPInputStream(inputStream))
					.setHeaderFields(Collections.unmodifiableMap(newHeaderFields))
					.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	private void copyHeader(Map<String,List<String>> src, Map<String,List<String>> dest) {
		for (Map.Entry<String, List<String>> entry : src.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }

            dest.put(entry.getKey(), entry.getValue());
		}
	}

}
