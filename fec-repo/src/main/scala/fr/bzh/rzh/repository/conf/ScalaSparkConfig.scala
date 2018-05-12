package fr.bzh.rzh.repository.conf

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

/**
 * @author L KHERBICHE
 */
class ScalaSparkConfig {
  
  val appName = "scalaSparkfec"
  val master = "local[2]" 
  
  
  def getSparkContext: SparkContext = {
    
    println("== Ret. SparkContext ==")
    val conf = new SparkConf().setAppName(appName).setMaster(master)
    new SparkContext(conf)
    
  }
}