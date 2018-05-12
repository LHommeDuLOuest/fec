package fr.bzh.rzh.service.fec.rules;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;

import fr.bzh.rzh.fec.exceptions.CompAuxEcLetException;
import fr.bzh.rzh.fec.model.FecObjJRdd;
import scala.Tuple2;

/**
 * @note this code was tested, but 45s using {@link #ReqEnum.R7 }
    @Override
	public Object tryRule(Object obj) throws CompAuxEcLetException {
		
		@SuppressWarnings("unchecked")
		Dataset<Row> results = (Dataset<Row>) obj;
		
		if(results.count()!=0) {
			String ret = results.map(new MapFunction<Row,String>() {
				@Override
				public String call(Row row) throws Exception {
					return "line: "+row.getLong(0) +" CompAuxNum: is NULL, EcritureLet:"+ row.getString(1)+"!=NULL";
				}
			},Encoders.STRING())
			.reduce(new ReduceFunction<String>() {
				@Override
				public String call(String v1, String v2) throws Exception {
					return v1+"\n"+v2;
				}
			});
			throw new CompAuxEcLetException(ret);
		}
		return null;
	}
**
* @note this impl take 129s
    @Override
	public Object tryRule(Object obj) throws CompAuxEcLetException {
		
		FecObjJRdd ford = (FecObjJRdd) obj;
		String separator = ford.getSeparator().separator();
		
		String res = ford.getLines().zipWithIndex()
		.filter(x -> x._1.split(separator)[6].trim().isEmpty() && !x._1.split(separator)[13].trim().isEmpty())
		.map(new Function<Tuple2<String,Long>,String>() {
			@Override
			public String call(Tuple2<String, Long> v1) throws Exception {
				return "line:"+(v1._2+1)+"CompAuxNum: is NULL EcritureLet:"+v1._1.split(separator)[13];
			}
		})
		.reduce((x,y) -> x+"\n"+y) ;
		
		if(!res.isEmpty()) {
			throw new CompAuxEcLetException(res);
		}
		return null;
	}
**
 */

/**
 * @author KHERBICHE L 0xCAFEBABE
 * Test 73 Contrôle de la cohérence entre le lettrage et la comptabilité auxiliaire
 */
public class CompAuxEcLetRule implements IRules {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Object tryRule(Object obj_) throws CompAuxEcLetException {
		
		FecObjJRdd ford_ = (FecObjJRdd) obj_;
		String separator_ = ford_.getSeparator().separator();
		
		JavaRDD<String> j_rdd_ = ford_.getLines().zipWithIndex()
		.map(new Function<Tuple2<String,Long>,String>() {
			@Override
			public String call(Tuple2<String, Long> _v1) throws Exception {
				String[] arr_ = _v1._1.split(separator_);
				if(arr_[6].trim().isEmpty() && !arr_[13].trim().isEmpty()) {
					return "line:"+(_v1._2+1)+"CompAuxNum: is NULL EcritureLet:"+arr_[13];
				} else {
					return "empty";
				}
			}
		})
		.filter(x_ -> !x_.contains("empty"));

		if(!j_rdd_.isEmpty()) {
			throw new CompAuxEcLetException(j_rdd_.reduce((_v1_$, _v2_$) -> _v1_$+"\n"+_v2_$));
		}
		return null;
	}

}
