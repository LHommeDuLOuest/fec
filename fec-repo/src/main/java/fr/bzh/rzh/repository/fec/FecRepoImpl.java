package fr.bzh.rzh.repository.fec;

import java.io.File;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.ReduceFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import fr.bzh.rzh.fec.exceptions.NumberColumnException;
import fr.bzh.rzh.fec.model.FecLineDC;
import fr.bzh.rzh.fec.model.SeparatorEnum;
import fr.bzh.rzh.repository.IFecRepo;
import fr.bzh.rzh.repository.ReqEnum;
import fr.bzh.rzh.repository.conf.SparkConfig;
import fr.bzh.rzh.repository.fec.ScalaFecRepo;

/**
 * 
 * @author KHERBICHE L
 *
 */
@SuppressWarnings("serial")
@Repository
@ComponentScan("fr.atos.rennes.repository.conf")
@Scope("prototype")
public class FecRepoImpl implements IFecRepo {
	
	private static final Log logger = LogFactory.getLog(FecRepoImpl.class);
	private JavaRDD<String> feclinejrddstr;
	private Dataset<Row> feclineDataFrame;
	private String tmpTable;
	
	
	public JavaRDD<String> getFeclinejrddstr() {
		return feclinejrddstr;
	}

	private void setFeclinejrddstr(JavaRDD<String> feclinejrddstr) {
		this.feclinejrddstr = feclinejrddstr;
	}
	
	/**
	 * 
	 * @param generifeclinejrdd a generic JavaRDD<String> of FEC File
	 * @param separator
	 * @return convert JavaRDD<String> to JavaRDD<Row>
	 */
	private JavaRDD<Row> jRddStrTojRddRow(JavaRDD<String> generifeclinejrdd, SeparatorEnum separator) {
		
		JavaRDD<Row> rowRDD = generifeclinejrdd
				.filter(x -> !x.contains("JournalCode"))
				.map(new Function<String,Row>() {
			        @Override public Row 
			        call(String line) throws Exception {
			        	line = line.replaceAll("(\\d+)\\,(\\d+)", "$1.$2");
			        	return RowFactory.create(Arrays.copyOf(line.split(separator.separator()),18));
			        }
		        });
		return rowRDD;
	}

	@Deprecated	
	@Override
	public void hdfsStoreFile(String path) {
		
		logger.info("==storeFile==");
		SparkConfig.getSparkConf().set("spark.cleaner.ttl", "10000");	
		SparkConfig.getJavaSparkContext().hadoopConfiguration().set("mapreduce.fileoutputcommitter.marksuccessfuljobs", "false");
		SparkConfig.getJavaSparkContext().hadoopConfiguration().set("parquet.enable.summary-metadata", "false");
		JavaRDD<String> lines = this.fileTojrddstrByStrm(System.getProperty("catalina.home")+ File.separator + "tempDir"+ File.separator +path).repartition(1);
		lines.saveAsTextFile("hdfs://localhost:9000/lyes/fec/"+path);
		
		logger.info("==Count== "+lines.count());
		lines.foreach(new VoidFunction<String>() {
			@Override
			public void call(String line) throws Exception {
				logger.info("==Line== "+line);
			}});
		lines = null;
		//SparkConfig.getJavaSparkContext().close();
	}

	@Override
	@Deprecated
	public void streamHdfsFile(String path) throws InterruptedException {
		
		logger.info("==readFile()==");
		//String paths = "hdfs://localhost:9000/lyes/fec/"+path;
		/*
		JavaSparkContext javaSparkContext = new JavaSparkContext(SparkConfig.getSparkConf());
		JavaRDD<String> lines = javaSparkContext.textFile(paths);
		long count = lines.count(); 
		logger.info("==Count== "+count);
		lines.foreach(new VoidFunction<String>() {
			@Override
			public void call(String line) throws Exception {
				logger.debug("==>>== "+line);
			}});
		javaSparkContext.close();
	*/
		JavaStreamingContext jssc = new JavaStreamingContext(SparkConfig.getSparkConf(), new Duration(8000));
		@SuppressWarnings("unused")
		JavaDStream<String> liness = jssc.textFileStream("hdfs://localhost:9000/lyes/fec/");
		/*liness.foreachRDD(new Function<JavaRDD<String>, Void>() {
	        public Void call(JavaRDD<String> rdd) throws Exception {
	            if(rdd!=null){
	                for (String temp : rdd.collect()) {
	                    logger.debug("==>>>=>= "+temp);
	                }
	            }
	            return null;
	        }});*/
		jssc.start();
		jssc.awaitTermination();
		jssc.close();
	}
	

	/**
	 * @deprecated use {@link #fileTojrddstrBySess(String)} instead.
	 * 
	 * Repository Layer computing JavaRDD<String> by Spark Stream API 2.x
	 * @param path:  The absolute path of FEC file in SERVLET-Engine
	 * @return JavaRDD<String> : Compute JavaRDD by using Spark Stream API 2.x
	 */
	@Deprecated
	@Override public JavaRDD<String>
	fileTojrddstrByStrm(String path) {
		Instant start = Instant.now();
		logger.info("===  tSpark Stream Api -> return JavaRDD<String> from Path  ===");
		JavaRDD<String> ret = SparkConfig.getJavaSparkContext().textFile(path);
		Instant end = Instant.now();
		logger.info("   == Duration == "+java.time.Duration.between(start, end));
 		return ret;
	}
	
	
	/**
	 * Repository Layer computing JavaRDD<String> by Spark Session API 2.x
	 *  
	 * @param path:  The absolute path of FEC file in SERVLET-Engine
	 * @return JavaRDD<String> : Compute JavaRDD by using Spark Session API 2.x
	 */
	@Override
	public JavaRDD<String> fileTojrddstrBySess(String	path) {
		
		Instant start = Instant.now();
		logger.info("===  Spark Session Api -> return JavaRDD<String> from Path  ===");
		this.setFeclinejrddstr(SparkConfig.getSparkSession().read().textFile(path).javaRDD());
		Instant end = Instant.now();
		logger.info("   == Duration == "+java.time.Duration.between(start, end));
		
		return this.getFeclinejrddstr();
	}
	
	/**
	 * @deprecated use {@link #processDataFrame(String, SeparatorEnum) and #getDataFrame(String)} instead
	 * @param path
	 * @param separator
	 */
	@Deprecated
	public void trySql(String path, SeparatorEnum separator) {
		
		SparkSession spark = SparkConfig.getSparkSession();
				
				JavaRDD<FecLineDC> jrddfl = spark.read()
						.textFile(path)
						.javaRDD()
						.filter(x -> !x.contains("JournalCode"))
						.map(new Function<String, FecLineDC> () {
							@Override
							public FecLineDC call(String line) {
								FecLineDC fl = new FecLineDC();
								try {
									//logger.info("->"+line.split(separator.separator()).length);
									fl.popByArray(line.split(separator.separator()));
								} catch (NumberColumnException e) {
									e.printStackTrace();
								}
								return fl;
							}
		 				}) ;	
				
				Instant start = Instant.now();
				Dataset<Row> fecLineDF = spark.createDataFrame(jrddfl, FecLineDC.class); // Apply a schema to an RDD
				
				fecLineDF.createOrReplaceTempView("FEC"); // register the DataFrame as a temporary view
				
				Dataset<Row> debitDF = spark.sql("SELECT	debit	FROM	FEC");
				Dataset<Double>	debitDFrame	= debitDF.map(new MapFunction<Row,Double>()	{
					@Override public Double
					call(Row row) throws Exception	{
						return Double.parseDouble(!row.getString(0).isEmpty()?row.getString(0).replace(",","."):"0.0");
					}
				},	Encoders.DOUBLE());
				//float	xx	=	debitDFFloat.reduce((x,y) -> (x+y) );
				Double	debit = debitDFrame.reduce(new	ReduceFunction<Double>()	{
					@Override	public	Double
					call(Double	x,	Double	y)	{
						return	x+y;
					}
				});
				logger.info("=== Result Add debit === "+debit.doubleValue());
				Instant end = Instant.now();
				logger.info("== Duration == "+java.time.Duration.between(start, end));
				
				start = Instant.now();
				Dataset<Row> creditDF = spark.sql("SELECT credit FROM FEC");
				Dataset<Double> creditDFrame = creditDF.map(new MapFunction<Row,Double>() {
					@Override public Double
					call(Row row) throws Exception {
						return Double.parseDouble(!row.getString(0).isEmpty() ? row.getString(0).replace(",",".") : "0.0");
					}
				}, Encoders.DOUBLE());
				Double	credit	=	creditDFrame.reduce(new ReduceFunction<Double>() {
					@Override	public	Double
					call(Double	x,	Double	y) {
						return	x+y;
					}
				});
				logger.info("=== Result Add Credit === "+credit.doubleValue());
				end = Instant.now();
				logger.info("== Duration == "+java.time.Duration.between(start, end));
				
				/**
				 * @fecLineDF2 : has unordered row<FecLine> attributes, 
				 * row.getString(0) musn't be correspond to the first variable of FecLine  
				 */
				start = Instant.now();
				Dataset<FecLineDC> fecLineDF2 = fecLineDF.map(new MapFunction<Row,FecLineDC>() {
					@Override public FecLineDC
					call(Row row) throws Exception {
						FecLineDC ret = new FecLineDC();
						ret.setJournalCode(row.getAs("journalCode"));       ret.setJournalLib(row.getAs("journalLib"));
						ret.setEcritureNum(row.getAs("ecritureNum"));       ret.setEcritureDate(row.getAs("ecritureDate")); 
						ret.setCompteNum(row.getAs("compteNum"));           ret.setCompteLib(row.getAs("compteLib"));
						ret.setCompAuxNum(row.getAs("compAuxNum"));         ret.setCompAuxLib(row.getAs("compAuxLib"));
						ret.setPieceRef(row.getAs("pieceRef"));             ret.setPieceDate(row.getAs("pieceDate"));
						ret.setEcritureLib(row.getAs("ecritureLib"));       ret.setDebit(row.getAs("debit"));
						ret.setCredit(row.getAs("credit"));                 ret.setEcritureLet(row.getAs("ecritureLet"));
						ret.setDateLet(row.getAs("dateLet"));               ret.setValidDate(row.getAs("validDate"));
						ret.setMontantDevise(row.getAs("montantDevise"));   ret.setIdevise(row.getAs("idevise"));
						return ret;
					}
				}, Encoders.bean(FecLineDC.class));
				//Dataset<String> debitDFS = fecLineDF2.map((FecLine fl) -> fl.getDebit() , Encoders.STRING());
				Dataset<String> debitDFrameTest = fecLineDF2.map(new MapFunction<FecLineDC,String> () {
					@Override public String 
					call(FecLineDC fl) throws Exception {
						return fl.getDebit();
					}
 				}, Encoders.STRING());
				String	debitTest = debitDFrameTest.reduce(new ReduceFunction<String> () {
					@Override
					public String call(String str0, String str1) throws Exception {
						Double res = Double.parseDouble(!str0.isEmpty() ? str0.replace(",",".") : "0.0") + Double.parseDouble(!str1.isEmpty() ? str1.replace(",",".") : "0.0");
						return String.valueOf(res.doubleValue());
					}
				});
				logger.info("=== Result Add Test Debit === "+debitTest);
				end = Instant.now();
				logger.info("== Duration == "+java.time.Duration.between(start, end));
				
				start = Instant.now();
				Dataset<String> creditDFrameTest = fecLineDF2.map(new MapFunction<FecLineDC,String> () {
					@Override public String
					call(FecLineDC fl) throws Exception {
						return fl.getCredit();
					}
				}, Encoders.STRING());
				String creditTest = creditDFrameTest.reduce(new ReduceFunction<String> () {
					@Override
					public String call(String str0, String str1) throws Exception {
						Double res = Double.parseDouble(!str0.isEmpty() ? str0.replace(",",".") : "0.0") + Double.parseDouble(!str1.isEmpty() ? str1.replace(",",".") : "0.0");
						return String.valueOf(res.doubleValue());
					}
				});
				logger.info("=== Result Add Test Credit === "+creditTest);
				end = Instant.now();
				logger.info("== Duration == "+java.time.Duration.between(start, end));
	}
	
	
	/**
	 * process the generic {@link #feclineDataFrame } of file
	 * @param path the absolute path of FEC file
	 * @param separator {@link #SeparatorEnum }
	 */
	@Override
	public void processDataFrame(String path, SeparatorEnum separator) {
			
        //fileTojrddstrBySess(path);//
		JavaRDD<Row> feclinejrddrow = this.jRddStrTojRddRow(feclinejrddstr, separator);
		
		//logger.info("=== Path:"+path+" === first line:"+feclinejrddstr.first()+" === Separator:"+separator.separator());
		String elevencol = feclinejrddstr.first().split(separator.separator())[11]; //11 columns header file ->(debit,credit) or (motant,sens)
		
		feclineDataFrame = SparkConfig.getSparkSession().createDataFrame(feclinejrddrow, new RepoHelper()
				.generateSchema(elevencol))
				.withColumn("index", functions.monotonically_increasing_id())
				.cache();
		
		tmpTable = genTempTableName();
		feclineDataFrame.createOrReplaceTempView(tmpTable);
		//feclineDataFrame.show();
	}
	
    /**
     * 
     * @param req {@link #ReqEnum}
     * @return results of SQL Request
     */
	@Override
    public Dataset<Row> getDataFrame(String req) {
		req = ReqEnum.valueOf(req).request().replace("FEC", tmpTable);
    	return SparkConfig.getSparkSession().sql(req);
    }
	
	
	public void clean() {
		logger.info("=== Clean ===");
		this.feclineDataFrame = null;
		this.feclinejrddstr = null;
	}
	
	
	private String genTempTableName() {
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 20; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		return sb.toString();
	}
	
	/**
	 * Call scala code to compute path file to RDD<String>
	 * 
	 * @param path: the absolute path of FEC file in SERVLET-Engine
	 */
	@Override	public	RDD<String> 
	fileToRdd(String path) {
		return ScalaFecRepo.fileToRDD(path);
	}

}
