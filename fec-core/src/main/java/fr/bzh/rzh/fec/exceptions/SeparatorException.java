package fr.bzh.rzh.fec.exceptions;

/**
 * 
 * @author KHERBICHE L
 *
 */
@SuppressWarnings("serial")
public class SeparatorException extends ApplicationException {
	
	public SeparatorException(String separator) {
		super(separator);
		//logger.warn("=== Separator Exception===");
		//logger.warn("=== Separator is invalid:"+separator+" must be tabulation or pipe ===");
		setMsg("*** Separator is invalid:"+separator+" must be tabulation or pipe ***");
	}
}
