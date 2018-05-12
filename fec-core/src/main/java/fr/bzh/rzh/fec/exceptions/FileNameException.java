package fr.bzh.rzh.fec.exceptions;

/**
 * 
 * @author KHERBICHE L
 *
 */
@SuppressWarnings("serial")
public class FileNameException extends ApplicationException {
	
	
	public FileNameException(String filename) {
		super(filename);
		//logger.warn("=== File Name Exception ===");
		//logger.warn("=== File Name is "+filename+", must be valid ===");
		//logger.warn("=== message:"+this.getMessage());
		//logger.warn("=== cause:"+this.getCause());
		//logger.warn("=== localizedMessage:"+this.getLocalizedMessage());
		setMsg("[Error] ... File Name is "+filename+", must be valid");
	}	

}
