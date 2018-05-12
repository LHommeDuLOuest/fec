package fr.bzh.rzh.service.handler;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.bzh.rzh.fec.model.FecObjectStream;

public class Launcher {

	private static final Log logger = LogFactory.getLog(Launcher.class);
	private static int count = 0;
			
	public static void main(String[] args) throws IOException {
		
		//String path = "C:\\\\Users\\\\A667119\\\\Desktop\\\\performance\\\\1 million et demi de lignes - prem ligne.txt";
		//String path = "C:\\Users\\A667119\\Desktop\\NETTI\\809127038FEC20151231.txt";
		//String path = args[0];
		String path = "/opt/mavenVersion/apache-maven-3.5.0/README.txt";
		
		Instant start = Instant.now();
		FecSceHandler fsh = new FecSceHandler();
		FecObjectStream fo = fsh.createFecObject(path);
		
		logger.info("== Mime == "+fo.getMimeType().toString());
		
		fo.getFileStream().forEach(stream -> {
			logger.info("== Stream == "+count+" "+stream);
			count++;
		});
		Instant end = Instant.now();
		logger.info("== Duration == "+Duration.between(start, end));
	}

}
