package center.xargus.ClientHttpRequester.reqeust;

public interface Cancelable {
	void cancel();
	boolean isCanceled();
}
