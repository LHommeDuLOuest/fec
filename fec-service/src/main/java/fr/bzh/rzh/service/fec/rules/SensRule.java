package fr.bzh.rzh.service.fec.rules;

import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import fr.bzh.rzh.fec.exceptions.SensException;

/**
 * 0xCAFEBABE
 * @author KHERBICHE L
 * Test 12 sens ctrl: 'D', 'C' or '+', '-',''
 */
public class SensRule implements IRules {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String dc_ = "DC";
	private final String pm_ = "+ -";

	
	@SuppressWarnings({ "unchecked"})
	@Override
	public Object tryRule(Object obj_) throws SensException {
		
		Dataset<Row> results_ = (Dataset<Row>) obj_;
		
		
		/*
		String sens = results.first().getString(0);
		if(Stream.of(dc).anyMatch(x -> x.equals(sens)) ) {
			active = dc;
		} else {
			if(Stream.of(pm).anyMatch(x -> x.equals(sens))) {
				active = pm;
			} else {
				throw new SensException("");
			}
		}
		results.foreach(new ForeachFunction<Row>() {
			@Override
			public void call(Row row) throws Exception {
				if(Stream.of(active).noneMatch(x -> x.equals(row.getString(0)))) {
					throw new SensException("");
				}
			}
		});
		if(dc.contains(sens)) {
			active = dc;
		} else {
			if(pm.contains(sens)) {
				active = pm;
			} else {
				throw new SensException("");
			}
		}
		*/
		/*
		//map function don't work 
		results.map(new MapFunction<Row,String>() { 
			@Override
			public String call(Row row) throws SensException {
				if(!dc.contains(row.getString(0)) && !pm.contains(row.getString(0))) {
					throw new SensException("sens exception");
				}
				return null;
			}	
		},Encoders.STRING());
		*/
		
		results_.foreach(new ForeachFunction<Row>() {
			@Override
			public void call(Row row_) throws SensException {
				if(!dc_.contains(row_.getString(0)) && !pm_.contains(row_.getString(0))) {
					throw new SensException("sens exception");
				}
			}	
		});
		
		return null;
	}

}
