package fr.bzh.rzh.service.handler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.bzh.rzh.fec.model.FecObjectStream;
import fr.bzh.rzh.fec.model.MimeTypeEnum;

/**
 * 
 * @author A667119 KHERBICHE L
 *
 */
public class FecSceHandler { 
	
	private static final Log logger = LogFactory.getLog(FecSceHandler.class);
	
	
	/*
	 * Create & return FecObject by Path
	 */
	public FecObjectStream createFecObject(String path) throws IOException {
	
		logger.info("== createFecObject(String path) call == ");
		
		Supplier<Stream<String>> streamSupplier = () -> {
			try {
				return Files.lines(Paths.get(path), Charset.forName("Cp1252"));
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("== Stream is NULL == ");
				return null;
			}
		};

		FecObjectStream fecObject = new FecObjectStream();
		fecObject.setStreamSupplier(streamSupplier);
		fecObject.setMimeType(getMimeType(path));
		
		return fecObject;
	}
	
	
	/*
	 * get Mime Type of file by Path
	 */
	private MimeTypeEnum getMimeType(String absPath) {
		
		try {
			if( Files.probeContentType(Paths.get(absPath)) == MimeTypeEnum.TEXT.mime() ) {
				logger.info("== mimeType is == "+ MimeTypeEnum.TEXT.mime());
				return MimeTypeEnum.TEXT;
			} else {
				if( Files.probeContentType(Paths.get(absPath)) == MimeTypeEnum.CSV.mime() ) {
					logger.info("== mimeType is == "+ MimeTypeEnum.CSV.mime());
					return MimeTypeEnum.CSV;
				} else {
					logger.info("== mimeType is == "+ MimeTypeEnum.OTHER.mime());
					return MimeTypeEnum.OTHER;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("== mimeType is NULL ==");
			return null;
		}
	}
}
