package fr.bzh.rzh.service.fec.rules;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.ReduceFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;

import fr.bzh.rzh.fec.exceptions.ApplicationException;

/**
 * 0xCAFEBABE
 * @author KHERBICHE L
 * Test 49 ctrl sum(montant)
 */
public class MontantRule implements IRules {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked"})
	@Override
	public Object tryRule(Object obj_) throws ApplicationException {
		
		Dataset<Row> results_ = (Dataset<Row>) obj_;
		
		Dataset<Double>	montant_DFrame_	= results_.map(new MapFunction<Row,Double>()	{
			@Override public Double
			call(Row row_) throws Exception	{
				return Double.parseDouble(!row_.getString(0).isEmpty()?row_.getString(0).replace(",","."):"0.0");
			}
		},	Encoders.DOUBLE());
		Double	montant_ = montant_DFrame_.reduce(new ReduceFunction<Double>() {
			@Override	public	Double
			call(Double	x_,	Double	y_)	{
				return	x_+y_;
			}
		});
		logger.info("=== Sum montant === "+montant_.doubleValue());
		return null;
	}

}
