package center.xargus.ClientHttpRequester.exception;

import java.io.IOException;

public class RequestCanceledException extends IOException {
		public RequestCanceledException() {
			super(new IOException());
		}
}
