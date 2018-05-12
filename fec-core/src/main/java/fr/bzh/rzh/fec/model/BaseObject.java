package fr.bzh.rzh.fec.model;

import java.io.Serializable;

/**
 * 
 * @author A667119 KHERBICHE L
 *
 */
public abstract class BaseObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Override
	public abstract String toString ();
	@Override
	public abstract boolean equals (Object o);
	@Override
	public abstract int hashCode ();
	
}
