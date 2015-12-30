package exception;

@SuppressWarnings("serial")
public class NotOwnerException extends Exception {

	public NotOwnerException() {
		super("傘の所有者ではありません．");
	}
}