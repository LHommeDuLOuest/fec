package fr.bzh.rzh.repository.conf

import org.apache.spark.sql.SparkSession

/**
 * @author L KHERBICHE
 */
class ScalaSparkSessionConfig () {
  
  val appName = "scalaSparkfec"
  val master = "local[*]" 
    
  
  def getSparkSession() :org.apache.spark.sql.SparkSession = {
    
    SparkSession.builder().appName(appName).master(master).getOrCreate()
  }
}