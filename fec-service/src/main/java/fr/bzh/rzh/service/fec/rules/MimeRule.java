package fr.bzh.rzh.service.fec.rules;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import fr.bzh.rzh.fec.exceptions.MimeTypeException;
import fr.bzh.rzh.fec.model.MimeTypeEnum;

/**
 * 0xCAFEBABE
 * @author KHERBICHE L
 * Test 63 Ctrl mime type
 */
public class MimeRule implements IRules {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Object tryRule(Object obj_) throws MimeTypeException {
		
		String _abs_Path_ = (String)obj_;
		
		try {
			if (Files.probeContentType(Paths.get(_abs_Path_)).equals(MimeTypeEnum.TEXT.mime())) {
				//logger.info("== mimeType is == " + MimeTypeEnum.TEXT.mime());
				return MimeTypeEnum.TEXT;
			} else {
				if (Files.probeContentType(Paths.get(_abs_Path_)).equals(MimeTypeEnum.CSV.mime())) {
					//logger.info("== mimeType is == " + MimeTypeEnum.CSV.mime());
					return MimeTypeEnum.CSV;
				} else {
					//logger.debug("== mimeType is == " + MimeTypeEnum.OTHER.mime());
					throw new MimeTypeException(Files.probeContentType(Paths.get(_abs_Path_)));
					// return MimeTypeEnum.OTHER;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("== mimeType is NULL ==");
			return null;
		}
	}

}
