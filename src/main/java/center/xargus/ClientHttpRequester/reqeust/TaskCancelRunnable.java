package center.xargus.ClientHttpRequester.reqeust;

interface TaskCancelRunnable extends Runnable, Cancelable{
	String getKey();
}
