package fr.bzh.rzh.fec.model;

import fr.bzh.rzh.fec.exceptions.NumberColumnException;

/**
 * 
 * @author KHERBICHE L
 *
 */
@SuppressWarnings("serial")
public class FecLineMS extends FecLine {
	
	private String montant;
	private String sens;
	
	
	public FecLineMS() {
		super();
	}


	public String getMontant() {
		return montant;
	}


	public void setMontant(String montant) {
		this.montant = montant;
	}


	public String getSens() {
		return sens;
	}


	public void setSens(String sens) {
		this.sens = sens;
	}


	@Override public String 
	toString() {
		return super.toString() + " [montant=" + montant + ", sens=" + sens + "]";
	}
	
	
	public void 
	popByArray(String[] arr) throws NumberColumnException {
		
		super.popByArray(arr);
		montant         = arr[11];
		sens            = arr[12];	
	}

}
