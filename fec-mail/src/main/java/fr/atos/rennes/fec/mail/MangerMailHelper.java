package fr.atos.rennes.fec.mail;

import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author KHERBICHE L
 */
public class MangerMailHelper {
	
	private static final Log logger = LogFactory.getLog(MangerMailHelper.class);
	private Properties properties = new Properties();;
		
	public void sendMail(Object obj) {
		
		try {
			InputStream in = getClass().getClassLoader().getResourceAsStream("mail.properties");
			properties.load(in);
			logger.info("notify.from="+properties.getProperty("fec.notify.from"));
			
		} catch (Exception e1) {
			logger.info(e1.getMessage());
			e1.printStackTrace();
		}
		
		Session session = Session.getDefaultInstance(properties,  new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getProperty("fec.notify.from").toString(),
                		properties.getProperty("fec.email.pwd").toString());
            }
        });
        
		try {
			
			for (int j=0;j<2;j++) {
				
			    StringBuilder message = new StringBuilder();
			    message.append("<html><head>");
			    message.append("</head><body><h1>Nouveau Status Fec App</h1><br/>");
			    message.append("<h2>Bonjour,</h2><br/>");
			    message.append("<div class=\"datagrid\" style=\"font: normal 12px/150% Arial, Helvetica, sans-serif; background: #fff; overflow: hidden; border: 1px solid #8C8C8C; -webkit-border-radius: 3px; -moz-border-radius: 3px; border-radius: 3px; \"><table style=\"border-collapse: collapse; text-align: left; width: 100%;\">");
			    message.append("<thead><tr style=\"background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #8C8C8C), color-stop(1, #7D7D7D) );background:-moz-linear-gradient( center top, #8C8C8C 5%, #7D7D7D 100% );filter:progid:DXImageTransform.Microsoft.gradient(startColorstr='#8C8C8C', endColorstr='#7D7D7D');background-color:#8C8C8C; color:#FFFFFF; font-size: 15px; font-weight: bold; border-left: 1px solid #A3A3A3;\"><th>Message</th><th>Date</th><th>status</th></tr></thead><tbody>");
			    message.append("<tr><td").append(" class=\"alt\"").append("><b>");
			    message.append("message");
			    message.append("</b></td>");
			    message.append("<td><a href=\"");
			    message.append("message");
			    message.append("\">");
			    message.append("Date");
			    message.append("</a></td>");
			    message.append("<td>");
			    message.append("<span><b><font color=\"").append("green")
				       .append("\">");
			    for (int i=0; i<2; i++) {
				    message.append("status");
				    message.append("<br />");
			    }
			    message.append("</font>");
			    message.append("</b></span></td></tr>");
			    message.append("</tbody><tfoot></tfoot></table></div></body></html>");
			    MimeMessage msg = new MimeMessage(session);
			    msg.setFrom(new InternetAddress(properties.getProperty("fec.notify.from")));
			    msg.addRecipient(Message.RecipientType.TO, new InternetAddress("sithsothseth@yahoo.fr"));
			    msg.setSubject("[Fec-Notif][" + InetAddress.getLocalHost().getHostName() + "]");
			    msg.setText(message.toString(), "utf-8", "html");
			    logger.info("************************************** before send");
			    Transport.send(msg);
			    logger.info("************************************** after send");
			}
			
		} catch (Exception e) {
			logger.debug(e);
		}

}

}
