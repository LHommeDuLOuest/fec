package fr.atos.rennes.fec.mail.resources;

import java.io.IOException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.atos.rennes.fec.mail.MangerMailHelper;

/**
 * @author KHERBICHE L
 */
@Path("/rest")
public class MailResource {

	private static final Log logger = LogFactory.getLog(MailResource.class);

	@RolesAllowed("ADMIN")
	@Path("/mail")
	@POST
	@Produces("application/json")
	@Consumes(MediaType.APPLICATION_JSON)
	public void newMail(@Suspended final AsyncResponse asyncResponse, String obj) throws IOException {
		logger.info("=== URI: /rest/mail");
		logger.info("=== POST Content: "+obj);
		//return Response.status(Status.OK).entity("was:sent").build();
		new Thread(new Runnable() {
			@Override
			public void run() {
				new MangerMailHelper().sendMail("ihh");
				logger.info("mail was sent");
                asyncResponse.resume("mail was sent");
			}
		}).start();
	}

	@RolesAllowed("ADMIN")
	@Path("/test")
	@GET
	@Produces("application/json")
	public void testJer(@Suspended final AsyncResponse asyncResponse) {
		logger.info("=== URI: /rest/test");
		String[] str = {"fec","atos","rennes"};
	    //return Response.status(Status.OK).entity(str).build();
		new Thread(new Runnable() {
			@Override
			public void run() {
                asyncResponse.resume(str);
			}
		}).start();
	}
	
	@RolesAllowed("ADMIN")
	@Path("/asyncTest")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public void asyncTest(@Suspended final AsyncResponse asyncResponse) {
		logger.info("=== URI: /rest/asyncTest");
		logger.info("Async server side");
		new Thread(new Runnable() {
			@Override
			public void run() {
				String result = "loop end";
                asyncResponse.resume(result);
			}
		}).start();
	}

}
