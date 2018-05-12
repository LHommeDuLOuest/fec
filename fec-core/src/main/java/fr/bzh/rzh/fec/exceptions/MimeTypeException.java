package fr.bzh.rzh.fec.exceptions;

/**
 * 
 * @author KHERBICHE L
 *
 */
@SuppressWarnings("serial")
public class MimeTypeException extends ApplicationException {

	
	public MimeTypeException(String mimetype) {
		super(mimetype);
		//logger.warn("=== Mime Type Exception ===");
		//logger.warn("=== Mime Type is "+mimetype+", must be text/plain or text/csv ===");
		setMsg("*** Mime Type is "+mimetype+", must be text/plain or text/csv ***");
	}
}
