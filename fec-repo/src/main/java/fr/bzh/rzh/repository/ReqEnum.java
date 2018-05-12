package fr.bzh.rzh.repository;

/**
 * 0xCAFEBABE
 * @author KHERBICHE L
 */
public enum ReqEnum {
	
	R1 ("SELECT debit,credit FROM FEC"),
	
	R1_1("SELECT SUM(debit), SUM(credit) FROM FEC"),
	
	R2 ("SELECT montant FROM FEC"),
	
	R3 ("SELECT ecritureLet, ABS(SUM(debit)), ABS(SUM(credit)) FROM FEC "
				+ "WHERE ecritureLet <> '' "
				+ "GROUP BY ecritureLet "
				+ "HAVING ABS(SUM(debit)) <> ABS(SUM(credit))"),
	
	R4 ("SELECT ecritureLet, ABS(SUM(montant)) FROM FEC "
				+ "WHERE ecritureLet <> '' "
				+ "GROUP BY ecritureLet "
				+ "HAVING ABS(SUM(montant)) <> 0.0"),
	
	R5 ("SELECT sens FROM FEC"),
	
	R6 ("SELECT index FROM FEC "
			+ "WHERE "
			+ "TRIM(journalCode) IS NULL OR "
			+ "TRIM(journalLib) IS NULL OR "
			+ "TRIM(ecritureNum) IS NULL OR "
			+ "TRIM(ecritureDate) IS NULL OR "
			+ "TRIM(compteNum) IS NULL OR "
			+ "TRIM(compteLib) IS NULL OR "
			+ "TRIM(pieceRef) IS NULL OR "
			+ "TRIM(pieceDate) IS NULL OR "
			+ "TRIM(ecritureLib) IS NULL OR "
			+ "TRIM(validDate) IS NULL "),
	
	R7("SELECT index, ecritureLet FROM FEC "
			+ "WHERE "
			+ "compAuxNum = '' AND "
			+ "ecritureLet <> '' "),
	
	R8("SELECT journalCode, SUM(debit), SUM(credit) FROM FEC "
			+ "GROUP BY journalCode "
			+ "HAVING ABS(SUM(debit)) <> ABS(SUM(credit))"),
	
	R9("SELECT journalCode, debit, credit FROM FEC"),
	
	R10("SELECT journalCode, montant, sens FROM FEC");
	
	private String request;
	
	
	ReqEnum(String req) {
		this.request = req;
	}
	
	
	public String request() {
		return request;
	}

}
