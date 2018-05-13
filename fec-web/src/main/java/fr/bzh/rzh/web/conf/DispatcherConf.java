package fr.bzh.rzh.web.conf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import fr.bzh.rzh.service.aspect.AspectRuleHelperBean;
import fr.bzh.rzh.service.aspect.IArhb;

/**
 * 
 * @author KHERBICHE L
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan({"fr.atos.rennes.web.*", "fr.atos.rennes.service.conf"})
public class DispatcherConf extends WebMvcConfigurerAdapter {
	
	private static final Log logger = LogFactory.getLog(DispatcherConf.class);
	@Autowired
	@Qualifier("viewResolver")
	private ViewResolver viewResolver;
	
	
	public void configureViewResolevers(ViewResolverRegistry registry) {
		logger.info("==setting view resolver==");
		registry.viewResolver(viewResolver);
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		logger.info("==add resource handlers==");
		registry.addResourceHandler("/static/**").addResourceLocations("/static/");
	}
	
	
	@Bean
	@DependsOn("viewResolver")
	public ViewResolver getViewResolver() {
		logger.info("==manage view resolver==");
		return viewResolver;
	}
	
	@Bean(name="viewResolver")
	public ViewResolver getViewRisolver() {
		logger.info("==instanciate view resolver==");
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".html");
		return resolver;
	}
	
	@Bean(name="multipartResolver")
	public CommonsMultipartResolver getCommonsMultipartResolver() {
		CommonsMultipartResolver cmpr = new CommonsMultipartResolver();
		cmpr.setMaxUploadSize(1000000000);
		return cmpr;
	}
	
	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public IArhb aspectRuleHelperBean() {
		
		IArhb aspectRuleHelperBean = new  AspectRuleHelperBean();
		return aspectRuleHelperBean;
	}
	
}
