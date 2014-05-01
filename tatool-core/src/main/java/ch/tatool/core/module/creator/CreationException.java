package ch.tatool.core.module.creator;

public class CreationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public String errorMessage;

	public CreationException() {
		
	}

	public CreationException(String msg) {
		errorMessage = msg;
	}
}
