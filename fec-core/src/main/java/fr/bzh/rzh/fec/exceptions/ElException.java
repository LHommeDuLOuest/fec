package fr.bzh.rzh.fec.exceptions;

/**
 * 
 * @author KHERBICHE L
 *
 */
@SuppressWarnings("serial")
public class ElException extends ApplicationException {
	
	
	public ElException(String el) {
		super(el);
		//logger.warn("=== Ecriture Lettrage is:"+el+" ===");
		setMsg("[INFO]... Ecriture Lettrage is not properly balanced "+el);
	}

}
