package center.xargus.ClientHttpRequester.connect;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HttpReqeustWorker<T> {
	private HttpURLConnection conn;
	private HttpInputStreamHandable<T> handable;
	private Map<String,String> requestHeaderFields;
	
	public HttpReqeustWorker() {
		this(null,null);
	}
	
	public HttpReqeustWorker(HttpInputStreamHandable<T> handable) {
		this(null,handable);
	}
	
	public HttpReqeustWorker(Map<String,String> headerFields, HttpInputStreamHandable<T> handable) {
		this.handable = handable;
		this.requestHeaderFields = headerFields;
	}
	
	public Response<T> request(String domain, String param, RequestMethodType methodType) {
		if (methodType == null) {
			methodType = RequestMethodType.GET;
		}
		
		T handleStreamResult = null;
		Map<String,List<String>> headerFields = null;
		int responseCode = -1;
		String responseMessage = null;
		
		try {
			URL url = new URL(getFullUrl(domain, param, methodType));
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod(methodType.toString());
			setRequestHeader(conn, requestHeaderFields);
			
			if (methodType == RequestMethodType.POST) {
				putPostParam(conn, param);
			}
			
			responseCode = conn.getResponseCode();
			responseMessage = conn.getResponseMessage();
			headerFields  = conn.getHeaderFields();
			if (handable != null) {
				handleStreamResult = handable.handle(conn.getInputStream());
			}
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		
		return new Response.Builder<T>()
				.setBody(handleStreamResult)
				.setResponseCode(responseCode)
				.setResponseMessage(responseMessage)
				.setHeaderFields(headerFields).build();
	}
	
	private void setRequestHeader(HttpURLConnection conn, Map<String,String> headers) {
		if (headers == null) {
			return;
		}
		
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			if (entry.getKey() == null) {
				continue;
			}
			
			conn.setRequestProperty(entry.getKey(), entry.getValue());
		}
	}
	
	private void putPostParam(HttpURLConnection conn, String param) throws IOException {
		if (conn != null && param != null && param.length() > 0) {
			OutputStream outputStream = conn.getOutputStream();
			outputStream.write(param.getBytes());
			outputStream.flush();
			outputStream.close();
		}
	}
	
	private String getFullUrl(String url, String param, RequestMethodType method) {
		String fullUrl = url;
		if (param != null && method != null && method == RequestMethodType.GET) {
			fullUrl += "?"+param;
		}
		
		return fullUrl;
	}
}
