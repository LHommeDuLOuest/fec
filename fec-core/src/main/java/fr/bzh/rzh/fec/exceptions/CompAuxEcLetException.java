package fr.bzh.rzh.fec.exceptions;

/**
 * @author KHERBICHE L
 */
@SuppressWarnings("serial")
public class CompAuxEcLetException extends ApplicationException {

	public CompAuxEcLetException(String message) {
		super(message);
		setMsg("[Warn]...\n"+message);
	}

}
