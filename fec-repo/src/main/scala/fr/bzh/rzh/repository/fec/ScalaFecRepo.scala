package fr.bzh.rzh.repository.fec

import fr.bzh.rzh.repository.conf.ScalaSparkConfig
import fr.bzh.rzh.repository.conf.ScalaSparkSessionConfig
import org.apache.spark.rdd.RDD
/**
 * @author L KHERBICHE
 */
object ScalaFecRepo {
  
  def fileToRDD (path : String) :RDD[String] = {
    println("== Ret. RDD From path ==")
    var sc = new ScalaSparkConfig().getSparkContext
    sc.textFile(path)
  }
  
  def readTextFileBySession(path: String) {
    
       println("== readTextFileBySession ==")
       var sssc = new ScalaSparkSessionConfig()
       var spark = sssc.getSparkSession()
       var data = spark.read.text(path)
       data.show()
  }
  
}