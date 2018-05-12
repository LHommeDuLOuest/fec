package fr.bzh.rzh.service.spark;

import org.apache.spark.SparkConf;

/**
 * 
 * @author A667119 KHERBICHE L
 *
 */
 public class SparkContext {
	
	private static SparkConf confInstance;
	
	
	public static SparkConf getSparkConf() {
		
		if(confInstance == null) {
			confInstance = new SparkConf()
					.setMaster("local[2]")
					.set("spark.driver.allowMultipleContexts", "true")
					.setAppName("fecfec");
		}
		return confInstance;
	}

}
