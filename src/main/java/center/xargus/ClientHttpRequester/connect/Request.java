package center.xargus.ClientHttpRequester.connect;

import java.util.HashMap;
import java.util.Map;

public class Request {
	private String domain;
	private String param;
	private RequestMethodType methodType;
	private Map<String,String> headerFields;
	
	private Request(Builder builder) {
		this.domain = builder.domain;
		this.param = builder.param;
		this.methodType = builder.methodType;
		this.headerFields = builder.headerFields;
	}
	
	public Builder newBuilder() {
		return new Builder(this);
	}
	
	public String getDomain() {
		return domain;
	}

	public String getParam() {
		return param;
	}

	public RequestMethodType getMethodType() {
		return methodType;
	}

	public Map<String, String> getHeaderFields() {
		return headerFields;
	}

	public static class Builder {
		private String domain;
		private String param;
		private RequestMethodType methodType;
		private Map<String,String> headerFields;
		
		public Builder(){
			headerFields = new HashMap<>();
		}
		
		public Builder(Request request) {
			this.domain = request.domain;
			this.param = request.param;
			this.methodType = request.methodType;
			this.headerFields = request.headerFields;
		}
		
		public Builder setDomain(String domain) {
			this.domain = domain;
			return this;
		}
		
		public Builder setParam(String param) {
			this.param = param;
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
