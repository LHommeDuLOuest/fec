package fr.bzh.rzh.service.fec.rules;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.bzh.rzh.fec.exceptions.ApplicationException;

/**
 * 
 * @author KHERBICHE L
 *
 */
//@Configurable(preConstruction=true)
//@Component
//@Scope(value = "prototype", proxyMode = ScopedProxyMode.INTERFACES)
public interface IRules extends Serializable  {
	
	static final Log logger = LogFactory.getLog(IRules.class);
	
	public Object tryRule(Object obj) throws ApplicationException;

}
