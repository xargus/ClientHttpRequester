package center.xargus.ClientHttpRequester.exception;

public class RequestUrlNotCorrectException extends Exception {

	public RequestUrlNotCorrectException() {
		super(new Exception());
	}
	
	public RequestUrlNotCorrectException(Exception e) {
		super(e);
	}
}
