package fr.bzh.rzh.fec.model;

import org.apache.spark.api.java.JavaRDD;

/**
 * 
 * @author KHERBICHE L
 *
 */
@SuppressWarnings("serial")
public class FecObjJRdd extends FecObject {
	
	private JavaRDD<String> lines;

	public JavaRDD<String> getLines() {
		return lines;
	}

	public void setLines(JavaRDD<String> lines) {
		this.lines = lines;
	}
	

}
