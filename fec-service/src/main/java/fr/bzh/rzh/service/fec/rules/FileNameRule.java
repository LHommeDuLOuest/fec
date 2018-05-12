package fr.bzh.rzh.service.fec.rules;

import java.io.File;
import java.util.regex.Pattern;

import fr.bzh.rzh.fec.exceptions.FileNameException;

/**
 * 0xCAFEBABE
 * @author KHERBICHE L
 * Test 1 Ctrl the name of a file
 */
//@Component
//@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
//@Configurable(preConstruction=true)
//@Component
//@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)

public class FileNameRule implements IRules {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Object tryRule(Object obj_) throws FileNameException {
		
		String path_ = (String)obj_;
		String filename_ = path_.split(File.separator)[4]; /** TODO calculate the split index */ 
		//String filename_ = path_.split("\\\\")[8];/** windows */
		String reg = "^[0-9]{9}FEC(?<date>[0-9]{4}(0[0-9]|1[0-2])([0-2][0-9]|3[0-1]))(_(0[1-9]|1[0-2]|[1-4]T|[1-2]S))?(\\.[a-zA-Z0-9]*)?$";

		if (Pattern.matches(reg, filename_)) {
			//logger.info("=== file name is valid ===");
		} else {
			//logger.debug("=== file name isn't valid ===");
			throw new FileNameException(filename_);
		}
		return filename_;
	}

}
