package fr.bzh.rzh.service.spark;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaDStream;


/**
 * 
 * @author A667119 KHERBICHE L
 *
 */
public class Launch {
	
	private static final Log logger = LogFactory.getLog(Launch.class);

	public static void main(String[] args) throws java.lang.InterruptedException {
		
		//String path = "/opt/mavenVersion/apache-maven-3.5.0/README.txt";
		//String path = "/opt/tomcatVersion/apache-tomcat-8.0.44/tempDir/README.txt";
		//String path = "C:/Users//A667119/Desktop/NETTI/809127038FEC20151231.txt";
		//String path = "C:/Users/A667119/Desktop/testes";
		String path = "hdfs://localhost:9000/lyes/fec";
		logger.info("Spark");
		
		
		JavaSparkContext sparkContext = new JavaSparkContext(SparkContext.getSparkConf());
		JavaRDD<String> lines = sparkContext.textFile(path);
		long count = lines.count(); 
		logger.info("==Count== "+count);
		lines.foreach(new VoidFunction<String>() {
			@Override
			public void call(String line) throws Exception {
				logger.info("==Line== "+line);
			}});
		
		
		/*
		JavaDStream<String> liness = JStreamingContext.getJavaStreamingContext().textFileStream(path).cache();
		logger.info("==JavaDStream Line toString== "+liness.toString());
		logger.info("==JavaDStream Line Count== "+liness.count());
		*/
		
		/*
		liness.foreachRDD(new Function2<JavaRDD<String>,Time,Void>() {
			@Override
			public Void call(JavaRDD<String> rdd, Time time) {
				logger.info("==RDD Count== "+rdd.name());
				rdd.foreach(new VoidFunction<String>() {
						@Override
						public void call(String s) {
							logger.info("==Spark== "+s);
						}
				});
				return null;
			}
		});
		*/
		/*
		
		liness.foreachRDD(
				  new Function2<JavaRDD<String>, Time, Void>() {
					    @Override
					    public Void call(JavaRDD<String> rdd, Time time) {
					      logger.info("==RDD toString == "+rdd.toString());
					      JavaRDD<String> rowRDD = rdd.map(new Function<String, String>() {
					        public String call(String word) {
					        	logger.info("==word== "+word);
					          return word;
					        }
					      });
					      return null;
					    }
					  }
					);
		
		*/
		
		/*
		JavaDStream<String> wordsDStream = liness.filter(new Function<String, Boolean>() {
	        public Boolean call(String line) {
	        	logger.info("== error == "+line);
	            return line.contains("localhost kernel: TCP bind hash");
	          }});
		wordsDStream.foreachRDD(new Function<JavaRDD<String>, Void>() {
	        public Void call(JavaRDD<String> rdd) throws Exception {
	            if(rdd!=null){
	            	//rdd.saveAsTextFile("");
	                for (String temp : rdd.collect()) {
	                    logger.info("== =="+temp);
	                }
	            }
	            return null;
	        }});
	    */
		
		/*
		liness.foreachRDD(new Function<JavaRDD<String>, Void>() {
	        public Void call(JavaRDD<String> rdd) throws Exception {
	            if(rdd!=null){
	            	//rdd.saveAsTextFile("");
	                for (String temp : rdd.collect()) {
	                    logger.info("==line== "+temp);
	                }
	            }
	            return null;
	        }});
		*/
		
		
		JStreamingContext.getJavaStreamingContext().start();
		JStreamingContext.getJavaStreamingContext().awaitTermination();
		
	}

}
