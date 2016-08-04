package center.xargus.ClientHttpRequester.interceptor;

import java.io.InputStream;

import center.xargus.ClientHttpRequester.connect.Response;

public interface HttpResponseInterceptor {
	Response<InputStream> intercept(Response<InputStream> response);
}
