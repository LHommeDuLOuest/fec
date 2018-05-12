package fr.bzh.rzh.repository.conf;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author KHERBICHE L
 *
 */
@SuppressWarnings("serial")
@Configuration
public class SparkConfig implements Serializable {

	private static final Log logger = LogFactory.getLog(SparkConfig.class);
	
	private static SparkConf sparkConf;
	private static JavaStreamingContext jssc;
	private static JavaSparkContext javaSparkContext;
	private static SparkSession sparkSession;

	
	public static synchronized SparkConf getSparkConf() {
		String[] jars = {"/opt/Codes/CodeJavaEclipse/ats/fec-parent/fec-repo/target/fec-repo-0.0.1.RELEASE.jar",
				"/opt/Codes/CodeJavaEclipse/ats/fec-parent/fec-service/target/fec-service-0.0.1.RELEASE.jar",
				"/opt/Codes/CodeJavaEclipse/ats/fec-parent/fec-core/target/fec-core-0.0.1.RELEASE.jar"};
		if (sparkConf == null) {
			logger.warn("=== SparkConf == NULL ->try to construct..  ===");
			sparkConf = new SparkConf()
					//.setMaster("local[46]")
					//.set("spark.executor.memory", "2g")
					//.set("spark.executor.instances","5")
					//.set("spark.task.cpus", "2")
					//.setMaster("spark://localhost:7077")
					//.setMaster("spark://lyes:7077")
                                        .setMaster("spark://192.168.1.67:7077")
					.set("spark.driver.maxResultSize", "6g")
					.set("spark.driver.memory", "1g")
					.set("spark.driver.allowMultipleContexts", "true")
					.set("spark.reducer.maxSizeInFlight", "144m")
					.set("spark.shuffle.file.buffer", "5m")
					.set("spark.default.parallelism", "45")
					.set("spark.closure.serializer", "org.apache.spark.serializer.JavaSerializer")
					.set("spark.serializer", "org.apache.spark.serializer.JavaSerializer")
					.setJars(jars)
					.setAppName("JavaSparkfec")
					;
			logger.warn("=== Ok SparkConf ===");
		}
		return sparkConf;
	}

	
	public static synchronized JavaStreamingContext getJavaStreamingContext() {

		if (jssc == null) {
			logger.warn("=== JavaStreamingContext == NULL ->try to construct..  ===");
			jssc = new JavaStreamingContext(getSparkConf(), new Duration(3000));
			logger.warn("=== Ok jssc ===");
		}
		return jssc;
	}
	
	
	
	public static synchronized JavaSparkContext getJavaSparkContext() {
		
		if(javaSparkContext == null) {
			logger.warn("=== JavaSparkContext == NULL ->try to construct..  ===");
			javaSparkContext = new JavaSparkContext(getSparkConf());
			logger.warn("=== Ok JavaSparkContext ===");
		}
		return javaSparkContext;
	}
	
	
	
	public static synchronized SparkSession getSparkSession() {
		
		if (sparkSession == null) {
			logger.warn("=== SparkSession == NULL  ->try to construct..  ===");
			sparkSession = new SparkSession(getJavaSparkContext().sc());
			logger.warn("=== Ok SparkSession ===");
		}
		return sparkSession;
	}

}
