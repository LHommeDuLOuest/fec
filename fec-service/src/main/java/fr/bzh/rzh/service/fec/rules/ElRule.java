package fr.bzh.rzh.service.fec.rules;

import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.api.java.function.ReduceFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;

import fr.bzh.rzh.fec.exceptions.ElException;

/**
 * 0xCAFEBABE
 * @author KHERBICHE L
 * Test 78 Contrôle de l'équilibre des opérations lettrées
 */
public class ElRule implements IRules {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings({ "unchecked"})
	@Override
	public Object tryRule(Object obj_) throws ElException {
		
		Dataset<Row> results_ = (Dataset<Row>)obj_;
		if(results_.count()!=0){
			String ecritureLet_ = results_.map(new MapFunction<Row,String>() {
				@Override public String 
				call(Row row_) throws Exception {
					return row_.get(0).toString();
				}
			}, Encoders.STRING())
					.reduce(new ReduceFunction<String> () {
						@Override	public	String
						call(String	x_,	String	y_)	{
							return	x_+" "+y_+" ";
						}
					});
					
			if(!ecritureLet_.isEmpty()) {
				throw new ElException("elexception");
			}
		}
		return null;
	}

}
