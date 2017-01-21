package center.xargus.ClientHttpRequester.connect;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import center.xargus.ClientHttpRequester.utils.IOUtils;

public class ConnectionCancelableStream extends InputStream implements StreamCancelable {

	private HttpURLConnection httpURLConnection;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	private volatile boolean isCanceled;

	public ConnectionCancelableStream(HttpURLConnection httpURLConnection, InputStream inputStream, OutputStream outputStream) {
		this.httpURLConnection = httpURLConnection;
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		
		isCanceled = false;
	}
	
	@Override
	public int read() throws IOException {
		byte[] bytes = new byte[1];
		int len = read(bytes);
		
		if (len == -1) {
			return 0xff;
		} else {
			return bytes[0] & 0xff;
		}
	}

	@Override
	public int read(byte[] b) throws IOException {
		return read(b, 0, b.length);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if (isCanceled) {
			closeStream();
			return -1;
		}
		
		try {
			int length = inputStream.read(b, off, len);
			if (length == -1) {
				closeStream();
			}
			
			return length;
		} catch (IOException e) {
			closeStream();
			throw e;
		}
	}

	@Override
	public int available() throws IOException {
		if (isCanceled) {
			closeStream();
			return -1;
		} else {
			return inputStream.available();
		}
	}

	private void closeStream() {
		IOUtils.closeQuietly(inputStream);
		IOUtils.closeQuietly(outputStream);
		IOUtils.closeQuietly(httpURLConnection);
		
		System.out.println("connection close");
	}
	
	@Override
	public void cancel() {
		isCanceled = true;
		System.out.println("cancel!!");
	}

	@Override
	public boolean isCanceled() {
		return isCanceled;
	}
}
