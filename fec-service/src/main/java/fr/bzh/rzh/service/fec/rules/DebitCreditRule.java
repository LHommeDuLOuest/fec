package fr.bzh.rzh.service.fec.rules;

import org.apache.spark.api.java.function.Function;

import fr.bzh.rzh.fec.exceptions.DebitCreditException;
import fr.bzh.rzh.fec.model.FecObjJRdd;

/**
 * @note this code was tested using:{@link #ReqEnum.R1 }
  Dataset<Row> results_ = (Dataset<Row>) obj_;
  Double debit_ = results_.map(new MapFunction<Row,Double>() {
			@Override public Double
			call(Row row_) throws Exception	{
				return Double.parseDouble(!row_.getString(0).isEmpty() ? row_.getString(0).replace(",",".") : "0.0");
			}
		},	Encoders.DOUBLE())
				.reduce(new ReduceFunction<Double>() {
					@Override	public	Double
					call(Double	x_,	Double	y_)	{
						return	x_+y_;
					}
				});
		logger.info("=== Sum debit(SQL):"+debit_.doubleValue());
		
		Dataset<Double>	creditDFrame_ = results_.map(new MapFunction<Row,Double>() {
			@Override public Double
			call(Row row_) throws Exception	{
				return Double.parseDouble(!row_.getString(1).isEmpty() ? row_.getString(1).replace(",",".") : "0.0");
			}
		},	Encoders.DOUBLE());
		Double	credit_ = creditDFrame_.reduce(new ReduceFunction<Double>() {
			@Override	public	Double
			call(Double	x_,	Double	y_)	{
				return	x_+y_;
			}
		});
		logger.info("=== Sum credit(SQL):"+credit_.doubleValue());
		creditDFrame_=null;
		
		if(credit_.doubleValue() != debit_.doubleValue()) {
			throw new DebitCreditException("credit != debit");
		}
  ******************************************
* @note {@link #ReqEnum.R1_1}
        Dataset<Row> results_ = (Dataset<Row>) obj_;
		
		Double debit_ = results_.first().getDouble(0);
		Double credit_ = results_.first().getDouble(1);
		
		logger.info("=== Sum debit(SQL):"+debit_.doubleValue());
		logger.info("=== Sum debit(SQL):"+credit_.doubleValue());
		
		if(credit_.doubleValue() != debit_.doubleValue()) {
			throw new DebitCreditException("credit != debit");
		}
  *******************************************
 * @note {@link #ReqEnum.R1}
        Dataset<Row> results_ = (Dataset<Row>) obj_;
		String[] colNames = {"debit","credit"};
		Dataset<String> df = results_
		.withColumn("debit", results_.col("debit").cast("Double"))
		.withColumn("credit", results_.col("credit").cast("Double"))
		.na().fill(0,colNames)
		.agg(sum("debit"), sum("credit"))
		.map(new MapFunction<Row,String>() {
			@Override
			public String call(Row value) throws Exception {
				if(value.getDouble(0)!=value.getDouble(1)) {
					logger.info("=== Sum debit(SQL):"+value.getDouble(0));
					logger.info("=== Sum credit(SQL):"+value.getDouble(1));
					return value.getDouble(0)+" "+value.getDouble(1);
				} else {
					return "empty";
				}
			}
		}, Encoders.STRING())
		.filter(new FilterFunction<String>() {
			@Override
			public boolean call(String value) throws Exception {
				return !value.contains("empty");
			}
		});
		if(df.count()!=0) {
			throw new DebitCreditException(df.reduce(new ReduceFunction<String>() {
				@Override
				public String call(String v1, String v2) throws Exception {
					return v1 + "\n" + v2;
				}	
			}));
		}
 */

/**
 * 0xCAFEBABE
 * 
 * @author KHERBICHE L Test 49 ctrl sum(debit) sum(credit)
 */
public class DebitCreditRule implements IRules {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Object tryRule(Object obj_) throws DebitCreditException {

		FecObjJRdd $ford_ = (FecObjJRdd) obj_;
		String separator_ = $ford_.getSeparator().separator();

		Double debit = $ford_.getLines().filter(x_ -> !x_.contains("JournalCode")).map(new Function<String, Double>() {
			@Override
			public Double call(String str) throws Exception {
				return str.split(separator_)[11].equals("") ? 0.0
						: Double.parseDouble(str.split(separator_)[11].replace(",", "."));
			}

		}).reduce((x, y) -> x + y);

		Double credit = $ford_.getLines().filter(x_ -> !x_.contains("JournalCode")).map(new Function<String, Double>() {
			@Override
			public Double call(String str) throws Exception {
				return str.split(separator_)[11].equals("") ? 0.0
						: Double.parseDouble(str.split(separator_)[12].replace(",", "."));
			}

		}).reduce((x, y) -> x + y);
		
		if(debit!=credit) {
			logger.debug("#################### debit credit:"+debit+"!="+credit);
			throw new DebitCreditException("[Error] ... credit != debit"+ debit+"!="+credit);
		}

		return null;
	}

}
