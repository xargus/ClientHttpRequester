package center.xargus.ClientHttpRequester.connect;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import center.xargus.ClientHttpRequester.HttpRequestable;
import center.xargus.ClientHttpRequester.Request;
import center.xargus.ClientHttpRequester.RequestMethodType;
import center.xargus.ClientHttpRequester.Response;
import center.xargus.ClientHttpRequester.exception.RequestMethodNotFoundException;
import center.xargus.ClientHttpRequester.exception.RequestUrlNotCorrectException;
import center.xargus.ClientHttpRequester.utils.IOUtils;

public class HttpReqeustWorker implements HttpRequestable {
	private Request request;
	
	public HttpReqeustWorker(Request request) {
		this.request = request;
	}
	
	@Override
	public Response<InputStream> request() throws RequestMethodNotFoundException, RequestUrlNotCorrectException, IOException {
		if (request.getMethodType() == null) {
			throw new RequestMethodNotFoundException();
		}
		
		if (request.getDomain() == null) {
			throw new RequestUrlNotCorrectException();
		}
		
		InputStream connectionInputStream = null;
		OutputStream connectionOutputStream = null;
		Map<String,List<String>> headerFields = null;
		int responseCode = -1;
		String responseMessage = null;
		
		HttpURLConnection conn = null;
		
		try {
			URL url = new URL(request.getDomain());
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.setRequestMethod(request.getMethodType().toString());
			setRequestHeader(conn, request.getHeaderFields());
			
			if (request.getMethodType() == RequestMethodType.POST) {
				conn.setDoOutput(true);
				connectionOutputStream = conn.getOutputStream();
				writePostData(connectionOutputStream, request.getPostStream());
			}
			
			responseCode = conn.getResponseCode();
			responseMessage = conn.getResponseMessage();
			headerFields  = conn.getHeaderFields();
			connectionInputStream = conn.getInputStream();
		} catch (MalformedURLException e) {
			throw new RequestUrlNotCorrectException(e);
		} catch (IOException e) {
			throw e;
		} finally {
			if (responseCode == -1 || responseCode != HttpURLConnection.HTTP_OK) {
				IOUtils.closeQuietly(connectionInputStream);
				IOUtils.closeQuietly(connectionOutputStream);
				IOUtils.closeQuietly(conn);
			}
		}
		
		return new Response.Builder<InputStream>()
				.setBody(new ConnectionCancelableStream(conn, connectionInputStream, connectionOutputStream))
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
	
	private void writePostData(OutputStream outputStream, InputStream inputStream) throws IOException {
		if (outputStream != null && inputStream != null) {
			BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
			byte[] buffered = new byte[1024];
			int len;
			while ((len = bufferedInputStream.read(buffered, 0, 1024)) != -1) {
				bufferedOutputStream.write(buffered, 0, len);
			}
			outputStream.flush();
			
			IOUtils.closeQuietly(bufferedInputStream);
		}
	}
}
