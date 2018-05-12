package fr.bzh.rzh.fec.exceptions;

/**
 * 
 * @author KHERBICHE L
 *
 */
@SuppressWarnings("serial")
public class NumberColumnException extends ApplicationException {
	
	public NumberColumnException(String nb) {
		super(nb);
		//logger.error("=== NumberColumnException ===");
		//logger.error("=== Number of columns="+nb+" ,must be between [16, 18] ===");
		setMsg("*** Number of columns="+nb+" ,must be between [16, 18] ***");
	}

}
