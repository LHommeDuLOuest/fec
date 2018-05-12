package fr.bzh.rzh.fec.exceptions;

/**
 * 
 * @author KHERBICHE L
 *
 */
@SuppressWarnings("serial")
public class DebitCreditException extends ApplicationException {
	
	
	public DebitCreditException(String msg) {
		super(msg);
		//logger.warn("=== Debit must equal Credit ===");
		setMsg("[Warn] ... Debit must equal Credit" + msg);
	}

}
