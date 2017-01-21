package center.xargus.ClientHttpRequester.connect;

public interface TaskCancelRunnable extends Runnable, StreamCancelable{
	String getKey();
}
