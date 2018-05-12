package fr.bzh.rzh.fec.model;

/**
 * 
 * @author A667119 KHERBICHE L
 *
 */
public enum SeparatorEnum {
	
	TABULATION ("\\t"),
	PIPE("\\|"),
	COMMA(";"),
	OTHER("other");
	
	private String separator;
	
	SeparatorEnum(String sep) {
		this.separator = sep;
	}
	
	public String separator() {
		return separator;
	}

}
