package fr.bzh.rzh.fec.model;

import fr.bzh.rzh.fec.exceptions.NumberColumnException;

/**
 * 
 * @author KHERBICHE L
 *
 */
@SuppressWarnings("serial")
public class FecLineDC extends FecLine {
	
	private String debit;
	private String credit;
	
	
	public FecLineDC() {
		super();
	}
	
	public String getDebit() {
		return debit;
	}

	public void setDebit(String debit) {
		this.debit = debit;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}
	
	@Override public String 
	toString() {
		return super.toString() +" [debit=" + debit +", credit=" + credit + "]";
	}
	
	public void 
	popByArray(String[] arr) throws NumberColumnException {
		
		super.popByArray(arr);
		debit         = arr[11];
		credit        = arr[12];	
	}

}
