package fr.bzh.rzh.fec.exceptions;

/**
 * @author KHERBICHE L
 */
@SuppressWarnings("serial")
public class EOLException extends ApplicationException {

	public EOLException(String message) {
		super(message);
		setMsg("[Error]... "+message);
	}

}
