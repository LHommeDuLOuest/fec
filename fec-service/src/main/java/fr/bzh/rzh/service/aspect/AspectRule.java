package fr.bzh.rzh.service.aspect;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StopWatch;

import fr.bzh.rzh.fec.exceptions.ApplicationException;
import fr.bzh.rzh.service.fec.mail.MailClient;

/**
 * @author KHERBICHE L
 */
@Aspect
public class AspectRule {
	
	private static final Log logger = LogFactory.getLog(AspectRule.class);
	
	@Autowired
	private IArhb aspectRuleHelperBean;
	
	@AfterThrowing(
			pointcut = "execution(* fr.bzh.rzh.service.fec.rules.IRules.tryRule(..))",
			throwing = "error")
	public void afterThrowing(JoinPoint joinpoint, ApplicationException error) {
		
		
		logger.info("=== After throwing is running ===");
		logger.info("=== Class exception is:"+error);
		//logger.info("=== ApplicationException Exception is:"+error.getMsg());//<-
		/*for(Object obj : joinpoint.getArgs()) {
			logger.info("=== Arg === "+obj.toString());
		}*/
		
		aspectRuleHelperBean.addElement(error.getMsg());		
	}
	
	@Around(
			"execution(public * *(..)) && !execution(public * call(..)) && !execution(public * addElement(..)) "
			)
	public Object around(ProceedingJoinPoint joinpoint) throws Throwable {
		
		Object obj=null;
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		try {
			obj = joinpoint.proceed();
		} catch (ApplicationException e) {}
		stopWatch.stop();
		
		logger.info("**** Signature:"+joinpoint.getSourceLocation().getFileName()+" "+joinpoint.getSignature().getName());
		logger.info("**** "+stopWatch.getTotalTimeSeconds()+" sec");
		
		return obj;
	}
	
	@AfterReturning(
			pointcut = "execution(* fr.bzh.rzh.service.fec.ManagerImpl.doProcess(..))",
			returning = "result")
	public void afterRet(JoinPoint joinpoint, Object result) {
		
		logger.info("=== after returning is running ===");
		logger.info("=== Signature: "+joinpoint.getSignature().getName());
		logger.info("=== return value: "+result);
		MailClient mc = new MailClient();
		mc.sendMail(aspectRuleHelperBean.getList());
	}
	
	
	
	/*
	@Before("execution(* fr.bzh.rzh.service.fec.rules.IRules.*(..))")
	public void befor(JoinPoint joinpoint) {
		logger.info("=== Before is running");
		logger.info("=== Signature: "+joinpoint.getSignature().getName());
		for(Object obj : joinpoint.getArgs()) {
			logger.info("=== Arg === "+obj.toString());
		}
		logger.info("=== Kind:"+joinpoint.getKind());
		logger.info("=== SourceLocation:"+joinpoint.getSourceLocation().getFileName());
		logger.info("=== Target:"+joinpoint.getTarget());
		logger.info("=== StaticPart:"+joinpoint.getStaticPart());
	}
	*/
	
	
	/*
	@AfterReturning(
			pointcut = "execution(* fr.bzh.rzh.service.fec.rules.IRules.*(..))",
			returning = "result")
	public void afterRet(JoinPoint joinpoint, Object result) {
		
		logger.info("=== after returning is running ===");
		logger.info("=== Signature: "+joinpoint.getSignature().getName());
		logger.info("=== return value: "+result);
	}
	*/
	
	
	/*
	@Before("execution(* fr.bzh.rzh.service.IManager.*(..))")
	public void before(JoinPoint joinpoint) {
		logger.info("IManager before is running");
		logger.info("IManager signature: "+joinpoint.getSignature().getName());
	}
    */
	
}
