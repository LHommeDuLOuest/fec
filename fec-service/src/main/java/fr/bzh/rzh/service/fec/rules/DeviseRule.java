package fr.bzh.rzh.service.fec.rules;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

import fr.bzh.rzh.fec.exceptions.DeviseException;
import fr.bzh.rzh.fec.model.FecObjJRdd;
import scala.Tuple2;

/**
 * 0xCAFEBABE
 * @author KHERBICHE L Test 31 ctrl foreign devise
 */
public class DeviseRule implements IRules {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Object tryRule(Object obj_) throws DeviseException {

		FecObjJRdd $ford_ = (FecObjJRdd) obj_;
		String separator_ = $ford_.getSeparator().separator();

		JavaRDD<String> j_rdd_ = $ford_.getLines().zipWithIndex()
				.filter(x ->!x._1.contains("JournalCode"))
				.map(new Function<Tuple2<String, Long>, String>() {
			@Override
			public String call(Tuple2<String, Long> line_) throws Exception {
				String[] arr_ay_ = line_._1.split(separator_);
				if (arr_ay_.length>=17) {
					
					/** arr.length=17 ==> Idevise omitted */
					if(arr_ay_.length==17) { return "line:"+(line_._2+1)+ "Idevise must be mentioned"; }
					/** then arr.length>=18, the line has MontantDevise & Idevise both
					 * arr[16] null or white */
					if(arr_ay_[16].trim().isEmpty()) { return "line:"+(line_._2+1)+ " MontantDevise is null"; }
					/** arr[16] (MontantDevise) is mentioned 
					 * arr[17] null or white */
					if(arr_ay_[17].trim().isEmpty()) { return "line:"+(line_._2+1)+ " Idevise is null (1177174) ?"; }
					/** arr[16]!=null & arr[17]!=null ==> do calculates & compare */
					Double debitOrMontant = Double.parseDouble(!arr_ay_[11].isEmpty() ? arr_ay_[11].replace(",", ".") : "0.0");
					Double creditOrSens = Double.parseDouble(!arr_ay_[12].isEmpty() ? (arr_ay_[12].matches("[CD+-^$]{1}") ? "0.0" : arr_ay_[12].replace(",", "."))  : "0.0");
					if ((debitOrMontant - creditOrSens) != Double.parseDouble(arr_ay_[16].replace(",", "."))) {
						return "line:"+(line_._2+1)+ " (Debit-Credit)!=MonatntDevise*Idevise OR Montant!=MonatntDevise*Idevise";
					} 
				} 
				return "empty";
			}
		}).filter(x_ -> !x_.contains("empty"));

		if (!j_rdd_.isEmpty()) {
			String msg_ = j_rdd_.reduce(new Function2<String,String,String>() {
				@Override
				public String call(String v1_, String v2_) throws Exception {
					return v1_+"\n"+v2_;
				}			
			});
			throw new DeviseException(msg_);
		}

		return null;
	}

}
