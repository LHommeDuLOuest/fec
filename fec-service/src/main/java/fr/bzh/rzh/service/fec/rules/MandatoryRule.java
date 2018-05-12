package fr.bzh.rzh.service.fec.rules;

import java.util.regex.Pattern;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

import fr.bzh.rzh.fec.exceptions.MandatoryException;
import fr.bzh.rzh.fec.model.FecObjJRdd;
import scala.Tuple2;

/**
 * @note this code was tested, but missed the line number
 * 
JavaRDD<String> rdd = ford.getLines().map(new Function<String,String>() {
	@Override
	public String call(String line) throws Exception {
		
		String[] arr = line.split(separator);
		String msg = "";
		
		if(arr[0].trim().isEmpty()) {
			msg = msg + "JournalCode is null ";
		}
		if(arr[1].trim().isEmpty()) {
			msg = msg + "JournalLib is null ";
		}
        if(arr[2].trim().isEmpty()) {
        	msg = msg + "EcritureNum is null ";
		}
        if(arr[3].trim().isEmpty()) {
        	msg = msg + "EcritureDate is null ";
		}
        if(arr[4].trim().isEmpty()) {
        	msg = msg + "CompteNum is null ";
		}
        if(arr[5].trim().isEmpty()) {
        	msg = msg + "CompteLib is null ";
		}
        if(arr[8].trim().isEmpty()) {
        	msg = msg + "PieceRef is null ";
		}
        if(arr[9].trim().isEmpty()) {
        	msg = msg + "PieceDate is null ";
		}
        if(arr[10].trim().isEmpty()) {
        	msg = msg + "EcritureLib is null ";
		}
        if(arr[15].trim().isEmpty()) {
        	msg = msg + "ValidDate is null ";
		}
        if(msg.isEmpty()) {
        	return "empty";
        } else {
        	return line+" "+msg;
        }	
	}			
}).filter(x -> !x.contains("empty"));
           **********
@note this code was tested, but 45s using {@link #ReqEnum.R6 }

Dataset<Row> results = (Dataset<Row>) obj;
		logger.info(">>>> count:"+results.count());
		if(results.count()!=0) {
			String msg = results.map(new MapFunction<Row,String>() {
				@Override
				public String call(Row value) throws Exception {
					return value.getString(0);
				}	
			}, Encoders.STRING())
			.reduce(new ReduceFunction<String>() {
				@Override
				public String call(String v1, String v2) throws Exception {
					return "line:"+v1 + "line:"+v2;
				}		
			});
			
			if(!msg.isEmpty()) {
				logger.info(">>>>>>> "+msg);
			}
		}
*
*/


/**
 * 0xCAFEBABE
 * @author KHERBICHE L
 * Test 20 & 32 the mandatory data 
 */
public class MandatoryRule implements IRules {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Object tryRule(Object obj_) throws MandatoryException {
		
		/** TODO try this rule with Java Collection jrddToCollection */
		
		FecObjJRdd ford_$_ = (FecObjJRdd) obj_;
		String separator = ford_$_.getSeparator().separator();
		String elevenCol = ford_$_.getLines().first().split(separator)[11];
		
		JavaPairRDD<String,Long> pair_ = ford_$_.getLines().zipWithIndex();
		
		JavaRDD<String> rdd_x0_;
		
		if(elevenCol.equals("Debit")) {
			rdd_x0_ = pair_.map(new Function<Tuple2<String,Long>,String>() {
				@Override
				public String call(Tuple2<String, Long> line_) throws Exception {
					String[] arr = line_._1.split(separator);
					/** TODO Test arr.length must >=16 if {@link #EOLRule} don't work */
					String msg_ = common_(arr);      
	                /** Test 32 */
					msg_ = dcTest32(arr[11], arr[12]);
					
	                if(msg_.isEmpty()) {
	                	return "empty";
	                } else {return "line num:"+(line_._2+1) + " "+msg_;}
				}
			}).filter(x_ -> !x_.contains("empty"));

		} else {
			rdd_x0_ = pair_.map(new Function<Tuple2<String,Long>,String>() {
				@Override
				public String call(Tuple2<String, Long> line_) throws Exception {
					String[] arr = line_._1.split(separator);
					/** TODO Test arr.length must >=16 if {@link #EOLRule} don't work */
					 String msg_ = common_(arr);
					 /** Test 32 */
					 msg_ = mTest32(arr[11]);

	                if(msg_.isEmpty()) {
	                	return "empty";
	                } else {return "line num:"+(line_._2+1) + " "+msg_;}
				}
			}).filter(x_ -> !x_.contains("empty"));
		}
		
		
		
		if(!rdd_x0_.isEmpty()) {
			String msg_ = rdd_x0_.reduce(new Function2<String, String, String>() {
				@Override
				public String call(String v1_, String v2_) throws Exception {
					return v1_ + "\n" + v2_;
				}
			});
			throw new MandatoryException(msg_);
		}        
		return null;
	}
	
	
	private String common_(String[] arr) {
		String msg_ ="";
		if(arr[0].trim().isEmpty()) { msg_ = msg_ + "JournalCode is null "; }
		if(arr[1].trim().isEmpty()) { msg_ = msg_ + "JournalLib is null "; }
        if(arr[2].trim().isEmpty()) { msg_ = msg_ + "EcritureNum is null "; }
        if(arr[3].trim().isEmpty()) { msg_ = msg_ + "EcritureDate is null "; }
        if(arr[4].trim().isEmpty()) { msg_ = msg_ + "CompteNum is null "; }
        if(arr[5].trim().isEmpty()) { msg_ = msg_ + "CompteLib is null "; }
        if(arr[8].trim().isEmpty()) { msg_ = msg_ + "PieceRef is null "; }
        if(arr[9].trim().isEmpty()) { msg_ = msg_ + "PieceDate is null ";}
        if(arr[10].trim().isEmpty()) {msg_ = msg_ + "EcritureLib is null "; }
        if(arr[15].trim().isEmpty()) {msg_ = msg_ + "ValidDate is null "; }
        return msg_;
	}
	
	/*  (0|empty)   (empty|0)   (empty|empty)   (0|0)   (R*|R*)  */
	private String dcTest32(String debit, String credit) {
		
		String ret_ = "";
		String reg = "^(((\\+|-)?0+(,[0]+)?)|(0+(,[0]+)?)(\\+|-)?)$";
		
		if(debit.trim().isEmpty() && credit.trim().isEmpty()) { /** (empty|empty) */
			ret_ = ret_ + "Debit & Credit are empty ";
			return ret_;
		} else { /** (R|empty) or (empty|R) or (R|R) */
			if(Pattern.matches(reg, debit) && Pattern.matches(reg, credit)) { /** (0|0) */
				ret_ = ret_ + "Debit=0 & Credit=0 ";
				return ret_;
			}
			if(Pattern.matches(reg, debit) && credit.trim().isEmpty()) { /** (0|empty) */
				ret_ = ret_ + "Debit=0 & Credit=empty ";
				return ret_;
			}
			if(Pattern.matches(reg, credit) && debit.trim().isEmpty()) { /** (empty|0) */
				ret_ = ret_ + "Debit=empty & Credit=0 ";
				return ret_;
			}
		}
		if(!Pattern.matches(reg, debit) && !Pattern.matches(reg, credit)) { /** (R*|R*)*/
			ret_ = ret_ + "Debit & Credit have value both";
		}
		return ret_;
	}
	
	/** @note sens is controlled {@link #SensRule} Test 12*/
	private String mTest32(String montant) {
		
		String ret_ = "";
		String reg = "^(((\\+|-)?0+(,[0]+)?)|(0+(,[0]+)?)(\\+|-)?)$";
		
		if(montant.trim().isEmpty()) {
			ret_ = ret_ + "Montant is empty ";
			return ret_;
		}
		if(Pattern.matches(reg, montant)) {
			ret_ = ret_ + "Montant=0 ";
		}
		return ret_;
	}
	

}
