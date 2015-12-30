package exception;

@SuppressWarnings("serial")
public class NotEnoughPointException extends Exception {

	public NotEnoughPointException() {
		super("所持ポイントが足りません．");
	}
}
