package center.xargus.ClientHttpRequester.connect;

public interface StreamCancelable {
	void cancel();
	boolean isCanceled();
}
