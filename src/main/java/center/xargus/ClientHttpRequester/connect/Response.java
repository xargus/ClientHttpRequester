package center.xargus.ClientHttpRequester.connect;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Response<T>{
	public static final String CONTENT_ENCODING_KEY = "Content-Encoding";
	
	private T body;
	private Map<String,List<String>> headerFields;
	private int responseCode;
	private String responseMessage;
	
	public static <K,F> Response<K> convertNewTypeResponse(K body, Response<F> response) {
		return new Response.Builder<K>()
				.setBody(body)
				.setResponseCode(response.getResponseCode())
				.setResponseMessage(response.getResponseMessage())
				.setHeaderFields(response.getHeaderFields())
				.build();
	}
	
	public Builder<T> newBuilder() {
		return new Builder<>(this);
	}
	
	public T getBody() {
		return body;
	}
	
	public Map<String,List<String>> getHeaderFields() {
		return headerFields;
	}
	
	public String getResponseMessage() {
		return responseMessage;
	}
	
	public String getContentEncoding() {
		if (headerFields != null) {
			return getHeaderValueWithStringFormat(headerFields.get(CONTENT_ENCODING_KEY));
		}
		
		return null;
	}
	
	public String getContentType() {
		if (headerFields != null) {
			return getHeaderValueWithStringFormat(headerFields.get("Content-Type"));
		}
		
		return null;
	}

	public int getResponseCode() {
		return responseCode;
	}
	
	private String getHeaderValueWithStringFormat(List<String> headerValues) {
		if (headerValues == null) {
			return null;
		}
		
		StringBuilder builder = new StringBuilder();
		Iterator<String> it = headerValues.iterator();
	    if (it.hasNext()) {
	        builder.append(it.next());

	        while (it.hasNext()) {
	            builder.append(", ")
	                   .append(it.next());
	        }
	    }
	    
	    return builder.toString();
	}

	private Response(Builder<T> builder) {
		this.body = builder.body;
		this.headerFields = builder.headerFields;
		this.responseCode = builder.responseCode;
		this.responseMessage = builder.responseMessage;
	}
	
	public static class Builder<T> {
		private T body;
		private Map<String,List<String>> headerFields;
		private int responseCode;
		private String responseMessage;
		
		public Builder() {
		}
		
		public Builder(Response<T> response) {
			this.body = response.getBody();
			this.headerFields = response.headerFields;
			this.responseCode = response.responseCode;
			this.responseMessage = response.responseMessage;
		}
		
		public Builder<T> setBody(T body) {
			this.body = body;
			return this;
		}
		
		public Builder<T> setHeaderFields(Map<String,List<String>> fields) {
			this.headerFields = fields;
			return this;
		}
		
		public Builder<T> setResponseCode(int responseCode) {
			this.responseCode = responseCode;
			return this;
		}
		
		public Builder<T> setResponseMessage(String message) {
			this.responseMessage = message;
			return this;
		}
		
		public Response<T> build() {
			return new Response<T>(this);
		}
	}
}
