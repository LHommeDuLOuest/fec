package fr.bzh.rzh.fec.exceptions;

/**
 * 
 * @author KHERBICHE L
 *
 */
@SuppressWarnings("serial")
public class HeaderException extends ApplicationException {

	public HeaderException(String message) {
		super(message);
		//logger.warn("=== "+message);
		setMsg("[Error] ... "+message);
	}

}
