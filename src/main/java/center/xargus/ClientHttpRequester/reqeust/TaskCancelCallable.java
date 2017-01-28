package center.xargus.ClientHttpRequester.reqeust;

import java.util.concurrent.Callable;

interface TaskCancelCallable<V> extends Callable<V>, Cancelable{
	String getKey();
}
