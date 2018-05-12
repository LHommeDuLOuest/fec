package fr.bzh.rzh.service.aspect;

import java.util.ArrayList;
import java.util.List;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

/**
 * @author KHERBICHE L
 */

@Component
public class AspectRuleHelperBean implements IArhb {
	
	//private static final Log logger = LogFactory.getLog(AspectRuleHelperBean.class);	
	private List<String> list = new ArrayList<String>();
	
	
	public List<String> getList() {
		return list;
	}
	
	public void addElement(String str) {
		//logger.info("=== addElement:"+str+" ===");
		list.add(str);
	}

}
