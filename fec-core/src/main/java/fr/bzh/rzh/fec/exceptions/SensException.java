package fr.bzh.rzh.fec.exceptions;

/**
 * 
 * @author KHERBICHE L
 *
 */
@SuppressWarnings("serial")
public class SensException extends ApplicationException {

	public SensException(String message) {
		super(message);
		//logger.warn("=== sens sign must be {'D', 'C'} or {'+', '-', ''} ===");
		setMsg("[Error] ... sens sign must be {'D', 'C'} or {'+', '-', ''}");
	}

}
