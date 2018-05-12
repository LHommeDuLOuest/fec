package fr.bzh.rzh.service.fec.rules;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.ReduceFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;

import fr.bzh.rzh.fec.exceptions.DebitCreditException;

/**
 * @note this was tested {@link #ReqEnum.R9 }
 * Dataset<Row> results = (Dataset<Row>) obj;
 * Dataset<String> df =  results.withColumn("debit", results.col("debit").cast("Long"))
				.withColumn("credit", results.col("credit").cast("Long"))
				.groupBy("journalCode").sum("debit", "credit")
		        .map(new MapFunction<Row, String>() {
			@Override
			public String call(Row v1) {
				return "JournalCode:" + v1.get(0).toString() + " sum(debit):" + v1.get(1).toString() + " sum(credit):"
						+ v1.get(2).toString();
			}
		}, Encoders.STRING());
		if(df.count() !=0) {
			throw new DebitCreditException(df.reduce(new ReduceFunction<String>() {
				@Override
				public String call(String v1, String v2) {
					return v1+"\n"+v2;
				}
			}));
		}
 */

/**
 * @author KHERBICHEL Test 50 ctrl equilibre comptable par code journal
 * 0xCAFEBABE
 */
public class DebCredByCodJourRule implements IRules {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public Object tryRule(Object obj_) throws DebitCreditException {
        
		Dataset<Row> results = (Dataset<Row>) obj_;
		
		if (results.count() != 0) {
			String res = results.map(new MapFunction<Row, String>() {
				@Override
				public String call(Row v1) {
					return "JournalCode:" + v1.get(0).toString() + " sum(debit):" + v1.get(1).toString() + " sum(credit):"
							+ v1.get(2).toString();
				}
			}, Encoders.STRING())
			.reduce(new ReduceFunction<String>() {
				@Override
				public String call(String v1, String v2) {
					return v1+"\n"+v2;
				}
			});
			
			throw new DebitCreditException(res);
		}
		return null;
	}

}
