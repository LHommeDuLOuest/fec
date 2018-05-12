package fr.bzh.rzh.fec.model;

import fr.bzh.rzh.fec.exceptions.NumberColumnException;

/**
 * 0xCAFEBABE
 * @author L KHERBICHE
 */
@SuppressWarnings("serial")
public abstract class FecLine extends BaseObject {
	
	protected static final int MAX_NMBR_ITEM = 22;
	protected static final int MIN_NMBR_ITEM = 16;
	
	//TODO : column name -> in ENUM 
	private String journalCode;   // 0
	private String journalLib;    // 1
	private String ecritureNum;   // 2    think to change into  long
	private String ecritureDate;  // 3
	private String compteNum;     // 4
	private String compteLib;     // 5
	private String compAuxNum;    // 6
	private String compAuxLib;    // 7
	private String pieceRef;      // 8
	private String pieceDate;     // 9
	private String ecritureLib;   // 10
	                              // 11, 12
	private String ecritureLet;   // 13
	private String dateLet;       // 14
	private String validDate;     // 15 
	private String montantDevise; // 16
	private String idevise;       // 17
	
	
	public FecLine() {
		super();
	}
	
	public String getJournalCode() {
		return journalCode;
	}

	public void setJournalCode(String journalCode) {
		this.journalCode = journalCode;
	}

	public String getJournalLib() {
		return journalLib;
	}

	public void setJournalLib(String journalLib) {
		this.journalLib = journalLib;
	}

	public String getEcritureNum() {
		return ecritureNum;
	}

	public void setEcritureNum(String ecritureNum) {
		this.ecritureNum = ecritureNum;
	}

	public String getEcritureDate() {
		return ecritureDate;
	}

	public void setEcritureDate(String ecritureDate) {
		this.ecritureDate = ecritureDate;
	}

	public String getCompteNum() {
		return compteNum;
	}

	public void setCompteNum(String compteNum) {
		this.compteNum = compteNum;
	}

	public String getCompteLib() {
		return compteLib;
	}

	public void setCompteLib(String compteLib) {
		this.compteLib = compteLib;
	}

	public String getCompAuxNum() {
		return compAuxNum;
	}

	public void setCompAuxNum(String compAuxNum) {
		this.compAuxNum = compAuxNum;
	}

	public String getCompAuxLib() {
		return compAuxLib;
	}

	public void setCompAuxLib(String compAuxLib) {
		this.compAuxLib = compAuxLib;
	}

	public String getPieceRef() {
		return pieceRef;
	}

	public void setPieceRef(String pieceRef) {
		this.pieceRef = pieceRef;
	}

	public String getPieceDate() {
		return pieceDate;
	}

	public void setPieceDate(String pieceDate) {
		this.pieceDate = pieceDate;
	}

	public String getEcritureLib() {
		return ecritureLib;
	}

	public void setEcritureLib(String ecritureLib) {
		this.ecritureLib = ecritureLib;
	}

	public String getEcritureLet() {
		return ecritureLet;
	}

	public void setEcritureLet(String ecritureLet) {
		this.ecritureLet = ecritureLet;
	}

	public String getDateLet() {
		return dateLet;
	}

	public void setDateLet(String dateLet) {
		this.dateLet = dateLet;
	}

	public String getValidDate() {
		return validDate;
	}

	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}

	public String getMontantDevise() {
		return montantDevise;
	}

	public void setMontantDevise(String montantDevise) {
		this.montantDevise = montantDevise;
	}

	public String getIdevise() {
		return idevise;
	}

	public void setIdevise(String idevise) {
		this.idevise = idevise;
	}
	
	
	/**
	 * 
	 * @param arr is a split of fec line
	 * TODO: -Thrown an Exception when MIN<=number(words)<=18
	 *       - Determinate the value of MIN
	 */
	public void 
	popByArray(String[] arr) throws NumberColumnException {
		
		if(arr!=null && MIN_NMBR_ITEM <= arr.length && arr.length <= MAX_NMBR_ITEM) {
			journalCode   = arr[0] ;
			journalLib    = arr[1] ;
			ecritureNum   = arr[2] ;
			ecritureDate  = arr[3] ;
			compteNum     = arr[4] ;
			compteLib     = arr[5] ;
			compAuxNum    = arr[6] ;
			compAuxLib    = arr[7] ; 
			pieceRef      = arr[8] ; 
			pieceDate     = arr[9] ;
			ecritureLib   = arr[10];
			ecritureLet   = arr[13];
			dateLet       = arr[14];
			validDate     = arr[15];
			montantDevise = 16 < arr.length ? arr[16] : "";
			idevise       = 16 < arr.length ? arr[17] : "";	
			
		} else {
			for(String str: arr) {
				System.out.println("-+"+str);
			}
			throw new NumberColumnException(String.valueOf(arr.length));
		}
	}
	
	
	@Override public String 
	toString() {
		return "FecLine [journalCode=" + journalCode + ", journalLib=" + journalLib + ", ecritureNum=" + ecritureNum
				+ ", ecritureDate=" + ecritureDate + ", compteNum=" + compteNum + ", compteLib=" + compteLib
				+ ", compAuxNum=" + compAuxNum + ", compAuxLib=" + compAuxLib + ", pieceRef=" + pieceRef
				+ ", pieceDate=" + pieceDate + ", ecritureLib=" + ecritureLib + ", ecritureLet=" + ecritureLet + ", dateLet=" + dateLet + ", validDate=" + validDate
				+ ", montantDevise=" + montantDevise + ", idevise=" + idevise + ", getClass()=" + getClass() + "]";
	}

	@Override public boolean 
	equals(Object o) {
		return false;
	}

	@Override public int 
	hashCode() {
		return 0;
	}

}
