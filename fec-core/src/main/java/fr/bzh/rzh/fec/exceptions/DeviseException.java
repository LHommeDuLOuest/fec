package fr.bzh.rzh.fec.exceptions;

/**
 * @author KHERBICHE L
 */
@SuppressWarnings("serial")
public class DeviseException extends ApplicationException {

	public DeviseException(String msg) {
		super(msg);
		setMsg("[Error]...\n"+msg);
	}

}
