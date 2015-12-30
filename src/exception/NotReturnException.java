package exception;

@SuppressWarnings("serial")
public class NotReturnException extends Exception {

	public NotReturnException() {
		super("この傘は借りられています．");
	}
}