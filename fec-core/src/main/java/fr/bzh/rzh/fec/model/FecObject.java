package fr.bzh.rzh.fec.model;

/**
 * 
 * @author KHERBICHE L
 *
 */
@SuppressWarnings("serial")
public class FecObject extends BaseObject {
	
	private MimeTypeEnum mimeType;
	private SeparatorEnum separator;
	private String filePath;
	
	
	public MimeTypeEnum getMimeType() {
		return mimeType;
	}

	public void setMimeType(MimeTypeEnum mimeType) {
		this.mimeType = mimeType;
	}
	
	public SeparatorEnum getSeparator() {
		return separator;
	}

	public void setSeparator(SeparatorEnum separator) {
		this.separator = separator;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override public String 
	toString() {
		return "mimeType: "+mimeType
		+"separator: "+separator
		+"filePath: "+filePath;
	}

	@Override
	public boolean equals(Object o) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

}
