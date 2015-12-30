package exception;

@SuppressWarnings("serial")
public class NotRentalException extends Exception {

	public NotRentalException() {
		super("傘を借りていません．");
	}
}