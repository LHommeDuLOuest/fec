package fr.bzh.rzh.service.fec.mail;

import java.net.URI;
import java.util.List;
import java.util.concurrent.Future;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;

/**
 * @author KHERBICHE L
 */
public class MailClient {
	
	private static final Log logger = LogFactory.getLog(MailClient.class);

	public void sendMail(List<String> list) {
		
		HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("fecats?mail_", "_passfec?_ats_mail");
		ClientConfig config = new ClientConfig()
				.register(JacksonFeature.class)
				.register(feature);
	    Client client = ClientBuilder.newClient(config); //Client client = ClientBuilder.newClient();
	    WebTarget service = client.target(getBaseURI()); //WebTarget service = client.target("http://localhost:8089").path("fec/rest/mail");
	    
	    /*
	    Future<Response> futureResp = service.path("rest").path("asyncTest")
	    		.request()
	    		.accept(MediaType.APPLICATION_JSON_TYPE)
	    		.header("asbel", "henter")
	    		.async()
	    		.get();
	    try {
			logger.info("Async response.get().getStatus(): " + futureResp.get().getStatus());
			logger.info("Async response.get().getEntity: "+futureResp.get().getEntity());
			logger.info("Async response.get().toString(): "+futureResp.get().toString());
			logger.info("Async readEntity: "+futureResp.get().readEntity(String.class));
			for(String str : futureResp.get().getAllowedMethods()) {
				logger.info("AllowedMethod: "+str);
			}
			for(String str : futureResp.get().getHeaders().keySet()) {
				logger.info("Header: "+str+ "="+futureResp.get().getHeaders().get(str).toString());
			}
			
		} catch (InterruptedException | ExecutionException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		*/
	   
	   
	    Future<Response> response = service.path("rest").path("mail")
	    		.request()
	    		.accept(MediaType.APPLICATION_JSON_TYPE)
	    		.async()
	    		.post(Entity.entity(list, MediaType.APPLICATION_JSON));
	    /*
	    try {
			logger.info("response.get().getStatus(): " + response.get().getStatus());
			logger.info("response.get().getEntity: "+response.get().getEntity());
			logger.info("response.get().toString(): "+response.get().toString());
			logger.info("readEntity: "+response.get().readEntity(String.class));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	    */
	    /*
	    Future<Response> response1 = service.path("rest").path("test")
	    		.request()
	    		.accept(MediaType.APPLICATION_JSON_TYPE)
	    		.async()
	    		.get();
		
		try {
			if (response1.get().getStatus() != 200) {
			  logger.info("Failed : HTTP error code : " + response1.get().getStatus());
			}
			logger.info("response1.get().getStatus(): " + response1.get().getStatus());
			logger.info("response1.get().getEntity: "+response1.get().getEntity());
			logger.info("response1.get().toString(): "+response1.get().toString());
			logger.info("readEntity: "+response1.get().readEntity(String.class));
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
          */
	    //logger.info(service.getUri().toString());

	}
	
	private static URI getBaseURI() {
	    return UriBuilder.fromUri("http://localhost:8089/").build();
	  }

}
