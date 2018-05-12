package fr.bzh.rzh.service.spark;

import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * 
 * @author A667119 KHERBICHE L
 *
 */
public class JStreamingContext {
	
	private static JavaStreamingContext jssc;
	
	public static JavaStreamingContext getJavaStreamingContext() {
		
		if(jssc == null) {
			jssc = new JavaStreamingContext(SparkContext.getSparkConf(), new Duration(3000));
		}
		return jssc;
	}

}
