package fr.bzh.rzh.service.fec.rules;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

import fr.bzh.rzh.fec.exceptions.EOLException;
import fr.bzh.rzh.fec.model.FecObjJRdd;
import scala.Tuple2;

/**
 *  @note this code was tested 
 *  
ford.getLines().foreach(new VoidFunction<String> () {
	@Override
	public void call(String line) throws Exception {
				if(line.trim().length()==0 || line.split(separator).length<16) {
			//TODO Make EOLException out call method else this interrupt execution
			throw new EOLException("white line or number of column < 16 detected");
		}		
	}			
});
*
*/

/**
 * 0xCAFEBABE
 * @author KHERBICHE L
 * Test 3 ctrl EOL & white line
 */
public class EOLRule implements IRules {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Object tryRule(Object obj_) throws EOLException {
		
		FecObjJRdd ford_ = (FecObjJRdd) obj_;
		String separator_ = ford_.getSeparator().separator();
		
		JavaRDD<String> j_rdd_ = ford_.getLines().zipWithIndex().map(new Function<Tuple2<String,Long>,String>() {
			@Override
			public String call(Tuple2<String, Long> line_) throws Exception {
				
				if(line_._1.trim().length()==0 || line_._1.split(separator_).length<16) {
					return "line:"+(line_._2+1) + " white line or number of column<16";
				} else {
					return "empty";
				}
			}	
		}).filter(x_ -> !x_.contains("empty"));
		
		logger.debug("#############count:"+j_rdd_.count());
		
		if(!j_rdd_.isEmpty()) {
			throw new EOLException(j_rdd_.reduce(new Function2<String,String,String>() {
				@Override
				public String call(String v1_, String v2_) throws Exception {
					return v1_ + "\n" + v2_ + "\n";
				}				
			}));
		}
		return null;
	}
}
