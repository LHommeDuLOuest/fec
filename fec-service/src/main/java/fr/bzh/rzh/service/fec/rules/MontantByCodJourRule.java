package fr.bzh.rzh.service.fec.rules;

import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.ReduceFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import fr.bzh.rzh.fec.exceptions.DebitCreditException;

/**
 * @note org.apache.spark.sql.Encoder
 StructType structType = new StructType();
 structType = structType.add("journalCode", DataTypes.StringType, false);
 structType = structType.add("montant", DataTypes.StringType, false);
 structType = structType.add("sens", DataTypes.StringType, false);
 ExpressionEncoder<Row> encoder = RowEncoder.apply(structType);
 */

/**
 * @author KHERBICHE L 0xCAFEBABE Test 50 ctrl equilibre comptable par code
 *         journal
 */
public class MontantByCodJourRule implements IRules {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String c_minus_ = "C-";

	@Override
	public Object tryRule(Object obj) throws DebitCreditException {

		@SuppressWarnings("unchecked")
		Dataset<Row> res = (Dataset<Row>) obj;
		
		Dataset<Row> r = res
				.map(new MapFunction<Row, Row>() {
					@Override
					public Row call(Row value) throws Exception {
						if (c_minus_.contains(value.getString(2))) {
							/** Example to avoid: if (montant == null) i.e (value.getString(1)== null)  ==> then the return == -null 
							 * Solution:
							 * return RowFactory.create(value.getString(0), "-"+(value.getString(1)!=null?value.getString(1):"0"), ""); 
							 * */
							return RowFactory.create(value.getString(0), "-"+value.getString(1), "");
						}
						return value;
					}
		}, res.exprEnc());
		
		String[] colNames = {"montant"};
		Dataset<String> rr = r
				/*.withColumn("montant", r.col("montant").when(r.col("montant").isNull(), "0").otherwise(r.col("montant"))) */
				.withColumn("montant", r.col("montant").cast("Double"))
				.na().fill(0, colNames)
				.groupBy("journalCode").sum("montant")
				.map(new MapFunction<Row, String>() {
					@Override
					public String call(Row value) throws Exception {
						if(value.getDouble(1) != 0) {
							return "journalCode:"+value.getString(0)+" sum(montant):"+value.getDouble(1);
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
		
		if(rr.count()!=0) {
			throw new DebitCreditException(rr.reduce(new ReduceFunction<String>() {
				@Override
				public String call(String v_, String _v) throws Exception {
					return v_+"\n"+_v;
				}
			}));
		}

		return null;
	}

}
