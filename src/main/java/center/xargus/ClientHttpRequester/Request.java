package center.xargus.ClientHttpRequester;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import center.xargus.ClientHttpRequester.utils.TextUtils;

public class Request {
	private final String domain;
	private final InputStream postStream;
	private final RequestMethodType methodType;
	private final Map<String,String> headerFields;
	
	private Request(Builder builder) {
		this.domain = builder.domain;
		this.postStream = builder.postStream;
		this.methodType = builder.methodType;
		this.headerFields = builder.headerFields;
	}
	
	public Builder newBuilder() {
		return new Builder(this);
	}
	
	public String getDomain() {
		return domain;
	}

	public InputStream getPostStream() {
		return postStream;
	}

	public RequestMethodType getMethodType() {
		return methodType;
	}

	public Map<String, String> getHeaderFields() {
		return headerFields;
	}

	public static class Builder {
		private String domain;
		private InputStream postStream;
		private RequestMethodType methodType;
		private Map<String,String> headerFields;
		
		public Builder(){
			headerFields = new HashMap<>();
		}
		
		public Builder(Request request) {
			this.domain = request.domain;
			this.postStream = request.postStream;
			this.methodType = request.methodType;
			this.headerFields = request.headerFields;
		}
		
		public Builder setDomain(String domain) {
			this.domain = domain;
			return this;
		}
		
		public Builder setPostBody(String body) {
			if (!TextUtils.isEmpty(body)) {
				setPostBody(new ByteArrayInputStream(body.getBytes()));
			}
			
			return this;
		}
		
		public Builder setPostBody(InputStream inputStream) {
			this.postStream = inputStream;
			return this;
		}
		
		public Builder setRequestMethodType(RequestMethodType methodType) {
			this.methodType = methodType;
			return this;
		}
		
		public Builder addHeader(String key, String vaule) {
			headerFields.put(key, vaule);
			return this;
		}
		
		public Request build() {
			return new Request(this);
		}
	}
}
