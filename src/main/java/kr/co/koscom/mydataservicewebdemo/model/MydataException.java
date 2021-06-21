package kr.co.koscom.mydataservicewebdemo.model;

public class MydataException extends RuntimeException {

	private static final long serialVersionUID = 6491503842831618158L;

	public MydataException(String message, Throwable cause) {
		super(message, cause);
	}

	public MydataException(String message) {
		super(message);
	}
	
}
