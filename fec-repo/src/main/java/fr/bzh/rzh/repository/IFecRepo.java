package fr.bzh.rzh.repository;

import java.io.Serializable;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import fr.bzh.rzh.fec.model.SeparatorEnum;

/**
 * 
 * @author KHERBICHE L
 *
 */
public interface IFecRepo extends Serializable {
	
	public void hdfsStoreFile(String path);
	public void streamHdfsFile(String path) throws InterruptedException;
	public JavaRDD<String> fileTojrddstrByStrm(String path);
	public JavaRDD<String> fileTojrddstrBySess(String path);
	public RDD<String> fileToRdd(String path);
	public void processDataFrame(String path, SeparatorEnum separator);
	public Dataset<Row> getDataFrame(String req);
	public void clean();
	

}
