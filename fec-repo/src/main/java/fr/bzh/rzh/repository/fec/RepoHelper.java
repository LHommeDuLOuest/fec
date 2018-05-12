package fr.bzh.rzh.repository.fec;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import fr.bzh.rzh.fec.exceptions.NumberColumnException;
import fr.bzh.rzh.fec.model.FecLine;
import fr.bzh.rzh.fec.model.SeparatorEnum;

/**
 * 
 * @author KHERBICHE L
 *
 */
public class RepoHelper {
	
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(RepoHelper.class);
	
	
	/**
	 * this code for javaBeans test
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private void checkJavaBean() {
		try {
			PropertyDescriptor[] props = Introspector.getBeanInfo(FecLine.class).getPropertyDescriptors();
			for(PropertyDescriptor p: props){
				System.out.println("== p.getDisplayName == \n"+p.getDisplayName());
				System.out.println("== p.getReadMethod == \n"+p.getReadMethod());
				System.out.println("== p.getWriteMethod == \n"+p.getWriteMethod());
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 
	 * @param elevencol the 11 column first line FEC file
	 * @return the generated schema based on field of bean
	 * Java Reflect
	 * 
	 */
	public StructType generateSchema(String elevencol) {
		
		List<StructField> list = new ArrayList<>();
		Field[] fields = FecLine.class.getDeclaredFields();
		
		for(Field fld : fields) {
			StructField field = DataTypes.createStructField(fld.getName(), DataTypes.StringType, true);
			list.add(field);
		}
		
		list.remove(0); list.remove(0); // remove static field MAX_NMBR_ITEM & MIN_NMBR_ITEM
		if(elevencol.equals("Debit")) {
			list.add(11, DataTypes.createStructField("debit", DataTypes.StringType, true));
			list.add(12, DataTypes.createStructField("credit", DataTypes.StringType, true));
		}
		if(elevencol.equals("Montant")) {
			list.add(11, DataTypes.createStructField("montant", DataTypes.StringType, true));
			list.add(12, DataTypes.createStructField("sens", DataTypes.StringType, true));
		}
		
		return DataTypes.createStructType(list);
	}
	
	//TO DELETE
	@Deprecated
	public void generic(SparkSession spark, String path, FecLine fl, SeparatorEnum separator) {
		
		JavaRDD<FecLine> jrddfl = spark.read()
				.textFile(path)
				.javaRDD()
				.filter(x -> !x.contains("JournalCode"))
				.map(new Function<String, FecLine> () {
					@Override
					public FecLine call(String line) {
						try {
							fl.popByArray(line.split(separator.separator()));
						} catch (NumberColumnException e) {
							e.printStackTrace();
						}
						return fl;
					}
 				}) ;
	}
}
