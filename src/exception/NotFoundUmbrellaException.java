package exception;

@SuppressWarnings("serial")
public class NotFoundUmbrellaException extends Exception {

	public NotFoundUmbrellaException() {
		super("傘が存在しません．");
	}
}