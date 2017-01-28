package center.xargus.ClientHttpRequester.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class IOUtils {

	public static void closeQuietly(InputStream inputStream) {
		try {
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void closeQuietly(OutputStream outputStream) {
		try {
			if (outputStream != null) {
				outputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void closeQuietly(HttpURLConnection httpURLConnection) {
		if (httpURLConnection != null) {
			httpURLConnection.disconnect();
		}
	}
}
