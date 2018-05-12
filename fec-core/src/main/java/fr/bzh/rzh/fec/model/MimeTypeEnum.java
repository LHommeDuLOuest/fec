package fr.bzh.rzh.fec.model;

/**
 * 
 * @author A667119
 *
 */
public enum MimeTypeEnum {
	
	TEXT("text/plain"),
	CSV("text/csv"),
	OTHER("other");
	
	private String mime;
	
	MimeTypeEnum(String mime) {
		this.mime = mime;
	}
	
	public String mime() {
		return this.mime;
	}
}
