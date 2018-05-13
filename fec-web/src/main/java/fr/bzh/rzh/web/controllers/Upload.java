package fr.bzh.rzh.web.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import fr.bzh.rzh.fec.exceptions.ApplicationException;
import fr.bzh.rzh.fec.exceptions.MimeTypeException;
import fr.bzh.rzh.fec.exceptions.SeparatorException;
import fr.bzh.rzh.service.IManager;
import fr.bzh.rzh.service.aspect.IArhb;

/**
 * 
 * @author KHERBICHE L
 *
 */
@RestController
@RequestMapping(value = "/api")
@CrossOrigin(origins = {"http://localhost:4200"})
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Upload {
	
	private static final Log logger = LogFactory.getLog(Upload.class);
	
	@Autowired(required = true)
	private  IManager managerImpl;
	
	@Autowired(required = true)
	private IArhb aspectRuleHelperBean;
	
	
	@RequestMapping(value = "/test", method = RequestMethod.GET, produces="application/json")
	public String[] getUploadForm() {
		logger.info("== URI: /api/test ==");
		String[] str = {"fec","atos","rennes"};
		return str;
	}
	
	@PostMapping(value ="file",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> justTest(/*@RequestHeader(value="Accept") String accept,
			                          @RequestHeader(value="Accept-Language") String acceptLanguage,
			                          @RequestHeader(value="User-Agent") String userAgent,
			                          @RequestHeader(value="Host") String host,
			                          @RequestHeader(value="Accept-Encoding") String acceptEncoding,
			                          @RequestHeader(value="Accept-Charset") String acceptCharset,
			                          @RequestHeader(value="Keep-Alive") String keepAlive,*/
			                          //@RequestBody(required = true) MultipartFile file,
			                          @RequestPart(value = "uploadFile", required = true)  final MultipartFile file,
			                          UriComponentsBuilder builder) throws IOException {
		
		logger.info("== URI: /api/file ==");
		logger.info("== builder:"+builder.toUriString());
		/*
		logger.info("== accept:"+accept);
		logger.info("== acceptLanguage:"+acceptLanguage);
		logger.info("== userAgent:"+userAgent);
		logger.info("== host:"+host);
		logger.info("== acceptEncoding:"+acceptEncoding);
		logger.info("== acceptCharset:"+acceptCharset);
		*/
		/*
		ResponseEntity<String> entity = template.getForEntity("http://example.com", String.class);
		String body = entity.getBody();
		MediaType contentType = entity.getHeaders().getContentType();
		HttpStatus statusCode = entity.getStatusCode();
		*/
		String rootPath = System.getProperty("catalina.home");
		File fileSaveDir = new File(rootPath + File.separator + "tempDir");
		if(!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
		}
		String fileName = file.getOriginalFilename();
		
		File serverFile = new File(fileSaveDir.getAbsolutePath() + File.separator + fileName);
		byte[] bytes = file.getBytes();
		BufferedOutputStream buffer = new BufferedOutputStream(new FileOutputStream (serverFile)); 
		buffer.write(bytes);
		buffer.close();
		
		logger.info("==serverFile=="+serverFile.getAbsolutePath());
		
		try {
			managerImpl.filetoFecObj(serverFile.getAbsolutePath());
		} catch (MimeTypeException e) {
			String msg = e.getMessage();
			logger.info("=== error msg === "+msg);

		} catch (SeparatorException e) {
			String msg = e.getMessage();
			logger.info("=== error msg === "+msg);

		} catch (ApplicationException e) {
			logger.info("=== e.getClass() === "+e.getClass());
		}
		
		managerImpl.doProcess(serverFile.getAbsolutePath());
		
		/**
		for(String str : aspectRuleHelperBean.getList()) {
			logger.info("=== AspectRuleHelperBean content:"+str+" ===");
		}
		*/
		managerImpl.clean();
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/file").buildAndExpand(file.getOriginalFilename()).toUri());
		
		return new ResponseEntity<List<String>>(aspectRuleHelperBean.getList(), HttpStatus.OK);
		//return aspectRuleHelperBean.getList();
	}

}
