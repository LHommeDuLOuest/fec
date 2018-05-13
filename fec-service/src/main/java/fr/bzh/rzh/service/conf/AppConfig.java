package fr.bzh.rzh.service.conf;

import org.aspectj.lang.Aspects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.context.annotation.EnableLoadTimeWeaving.AspectJWeaving;
import org.springframework.stereotype.Component;

import fr.bzh.rzh.service.aspect.AspectRule;

/**
 * @author KHERBICHE L
 */
//@EnableLoadTimeWeaving//
//@EnableAspectJAutoProxy
@EnableLoadTimeWeaving(aspectjWeaving = AspectJWeaving.ENABLED)
@Component
@Configuration
@ComponentScan({"fr.bzh.rzh.service.fec" , "fr.bzh.rzh.service.aspect" , "fr.bzh.rzh.repository.fec"})
public class AppConfig {
	
	@Bean
	public AspectRule aspectRule() {
		AspectRule aspect = Aspects.aspectOf(AspectRule.class);
		return aspect;
	}
	
}
