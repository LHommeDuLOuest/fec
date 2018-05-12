package fr.bzh.rzh.fec.exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author KHERBICHE L
 *
 */
@SuppressWarnings("serial")
public abstract class ApplicationException extends Exception {

	protected
	static final Log logger = LogFactory.getLog(ApplicationException.class);
	private String msg;
	
	public ApplicationException(String message) {}
	
	public void setMsg(String msg) {
		this.msg=msg;
	}
	
	public String getMsg() {
		return msg;
	}
	

}
