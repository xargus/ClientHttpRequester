package center.xargus.ClientHttpRequester;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import center.xargus.ClientHttpRequester.utils.TextUtils;

public class Request {
	public static final int PRIORITY_NORMAL = 1;
	public static final int PRIORITY_HIGH = 0;
	
	private final String domain;
	private final InputStream postStream;
	private final RequestMethodType methodType;
	private final Map<String,String> headerFields;
	private final int priority;
	
	private Request(Builder builder) {
		this.domain = builder.domain;
		this.postStream = builder.postStream;
		this.methodType = builder.methodType;
		this.headerFields = builder.headerFields;
		this.priority = builder.priority;
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

	public int getPriority() {
		return priority;
	}

	public static class Builder {
		private String domain;
		private InputStream postStream;
		private RequestMethodType methodType;
		private Map<String,String> headerFields;
		private int priority;
		
		public Builder(){
			headerFields = new HashMap<>();
			priority = Request.PRIORITY_NORMAL;
		}
		
		public Builder(Request request) {
			this.domain = request.domain;
			this.postStream = request.postStream;
			this.methodType = request.methodType;
			this.headerFields = request.headerFields;
			this.priority = request.priority;
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
			this.headerFields.put(key, vaule);
			return this;
		}
		
		public Builder setPriority(int priority) {
			this.priority = priority;
			return this;
		}
		
		public Request build() {
			return new Request(this);
		}
	}
}
