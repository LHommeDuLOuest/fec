package fr.bzh.rzh.service;

import java.io.Serializable;

import fr.bzh.rzh.fec.exceptions.ApplicationException;

/**
 * 
 * @author A667119 KHERBICHE L
 *
 */
public interface IManager extends Serializable {
	
	public void doProcess(String path) ;
	public void storeFile(String path);
	public void readFile(String path) throws InterruptedException;
	public void filetoFecObj(String path) throws ApplicationException;
	public void clean();

}
