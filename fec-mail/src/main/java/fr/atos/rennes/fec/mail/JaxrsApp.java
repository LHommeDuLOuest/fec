package fr.atos.rennes.fec.mail;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.wadl.internal.WadlResource;

import fr.atos.rennes.fec.mail.resources.MailResource;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
/**
 * @author KHERBICHE L
 */
@ApplicationPath("/")
public class JaxrsApp extends ResourceConfig {
	
	public JaxrsApp() {
		configureSwager();
		registerEndPoints();
		//packages("fr.atos.rennes.fec.mail.resources");
	}
	
	private void registerEndPoints() {
		
        register(WadlResource.class);
        register(JacksonFeature.class);
        register(CORSFilter_Sec.class);
        register(MailResource.class);
	}
	/* http://URI:8089/application.wadl*/
	private void configureSwager() {
		
        register(ApiListingResource.class);
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:8089");
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage("fr.atos.rennes.fec.mail.resources");
        beanConfig.setPrettyPrint(true);
        beanConfig.setScan(true);
	}

}
