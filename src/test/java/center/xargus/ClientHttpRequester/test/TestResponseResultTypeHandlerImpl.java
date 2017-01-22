package center.xargus.ClientHttpRequester.test;

import java.io.IOException;
import java.io.InputStream;

import center.xargus.ClientHttpRequester.ResponseResultTypeHandler;
import center.xargus.ClientHttpRequester.exception.RequestCanceledException;

public class TestResponseResultTypeHandlerImpl implements ResponseResultTypeHandler<String>{

	@Override
	public String handle(InputStream response) throws RequestCanceledException {
		try {
			int len;
			byte[] buffered = new byte[1024];
			while ((len = response.read(buffered, 0, 1024)) != -1) {
				System.out.println("read : "+new String(buffered));
				Thread.sleep(100);
			}
		} catch (IOException e) {
			System.out.println("!!!!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("end");
		}
		return null;
	}

}
