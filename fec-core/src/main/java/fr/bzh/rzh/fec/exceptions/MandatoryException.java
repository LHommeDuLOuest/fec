package fr.bzh.rzh.fec.exceptions;

/**
 * @author KHERBICHE L
 */
@SuppressWarnings("serial")
public class MandatoryException extends ApplicationException {

	public MandatoryException(String message) {
		super(message);
		setMsg("[Error]...\n"+message);
	}

}
