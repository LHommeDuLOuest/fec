package fr.bzh.rzh.web.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

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
@Controller
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Home {
	
	private static final Log logger = LogFactory.getLog(Home.class);
	
	//@Autowired
	//private Provider<ManagerImpl> provider;
	
	@Autowired(required = true)
	private  IManager managerImpl;
	
	@Autowired(required = true)
	private IArhb aspectRuleHelperBean;
	
	//private IManager createIManagerInstance() {
	//	managerImpl = provider.get();
	//	return managerImpl;
	//}
	
	
	@RequestMapping(value ={"","/","/home"}, method = RequestMethod.GET)
	public String welcome() throws InterruptedException {
		logger.info("== URI: /home ==");
		//managerImpl.readFile("");
		return "index";
	}
	
	@RequestMapping(value ={"/upload"}, method = RequestMethod.GET)
	public String getUploadForm() {
		logger.info("== URI: /upload ==");
		return "test";
	}
	
	@RequestMapping(value = "/home/upload", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) throws IOException {

		logger.info("== URI: /home/upload ==");
		//createIManagerInstance();//
		//logger.info("== managerImpl =="+managerImpl);
		
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
		
		//managerImpl.storeFile(fileName);
		try {
			managerImpl.filetoFecObj(serverFile.getAbsolutePath());
		} catch (MimeTypeException e) {
			String msg = e.getMessage();
			model.addAttribute("error", msg);
			logger.info("=== error msg === "+msg);
			return "error";
		} catch (SeparatorException e) {
			String msg = e.getMessage();
			model.addAttribute("error", msg);
			logger.info("=== error msg === "+msg);
			return "error";
		} catch (ApplicationException e) {
			logger.info("=== e.getClass() === "+e.getClass());
		}
		
		managerImpl.doProcess(serverFile.getAbsolutePath());
		
		/**for(String str : aspectRuleHelperBean.getList()) {
			logger.info("=== AspectRuleHelperBean content:"+str+" ===");
		}*/
		
		managerImpl.clean();
		
        return "test";
    }
}
