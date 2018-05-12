package fr.bzh.rzh.service.fec;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;

import fr.bzh.rzh.fec.exceptions.ApplicationException;
import fr.bzh.rzh.fec.model.FecObjJRdd;
import fr.bzh.rzh.fec.model.MimeTypeEnum;
import fr.bzh.rzh.fec.model.SeparatorEnum;
import fr.bzh.rzh.service.fec.rules.IRules;
import fr.bzh.rzh.service.fec.rules.MimeRule;
import fr.bzh.rzh.service.fec.rules.SeparatorRule;

/**
 * 
 * @author KHERBICHE L
 *
 */
@SuppressWarnings("serial")
public class ManagerHelper implements Serializable {

	private static final Log logger = LogFactory.getLog(ManagerHelper.class);
	private IRules rule;

	
	
	public void setRule(IRules newrule) {
		this.rule = newrule;
	}
	
	public void tryRule(Object obj) throws ApplicationException {
		//logger.info("=== mh:"+this+" Object:"+obj.hashCode());
		rule.tryRule(obj);
	}
	

	/**
	 * 
	 * @param rdd:
	 *            passed by parameter
	 * @param path:
	 *            the absolute path of FEC file in SERVLET-Engine
	 * @return FecObjJRdd: - MimeTypeEnum mimeType - SeparatorEnum separator -
	 *         JavaRDD<String> lines
	 * @throws ApplicationException 
	 * 
	 * 
	 */
	public FecObjJRdd fileToFecObj(JavaRDD<String> rdd, String path) throws ApplicationException {

		logger.info("=== Construct FecObjJRdd ===");
		FecObjJRdd ford = new FecObjJRdd();
		
		rule = new MimeRule();
		MimeTypeEnum mimetype = (MimeTypeEnum) rule.tryRule(path);
		ford.setMimeType(mimetype);
		
		rule = new SeparatorRule();
		SeparatorEnum separator = (SeparatorEnum) rule.tryRule(rdd.first());
		ford.setSeparator(separator);

		ford.setFilePath(path);
		ford.setLines(rdd);

		return ford;
	}

	/**
	 * Transform FEC file line RDD to columns.. Map<column, List<String>>
	 * 
	 * @param ford:
	 *            FecObjJRdd encapsulate the JavaRDD to convert, mime type,
	 *            separator & absolute path.
	 * 
	 *            Move data from Cluster to Driver Use RDD.collect()
	 */
	public Map<String, List<String>> rddToCollection(FecObjJRdd ford) {

		logger.info("=== rdd To Collection ===");
		Instant start = Instant.now();
		Map<String, List<String>> map = new LinkedHashMap<String, List<String>>();
		String firstLine = ford.getLines().first();
		String[] colNames = firstLine.split(ford.getSeparator().separator());
		for (String str : colNames) {
			map.put(str, new ArrayList<>(0));
		}

		for (String line : ford.getLines().filter(x -> !x.contains(firstLine)).collect()) {
			int index = 0;
			String[] words = line.split(ford.getSeparator().separator());
			for (Map.Entry<String, List<String>> mapentry : map.entrySet()) {
				mapentry.getValue().add(index < words.length ? words[index] : "");
				index++;
			}
		}
		Instant end = Instant.now();
		logger.info("=== Duration(rdd To Collection) == " + Duration.between(start, end));
		return map;
	}

	/**
	 * 
	 * @param list:
	 *            debit,credit column content. Jvm processing Sum the debit or
	 *            credit List using Collection
	 */
	public Double sumColumn(List<String> list) {
		Instant start = Instant.now();
		Double res = 0.0;
		for (String str : list) {
			res = res + Double.parseDouble(!str.isEmpty() ? str.replace(",", ".") : "0.0");
		}
		Instant end = Instant.now();
		logger.info("=== Duration(sumColumn) == " + Duration.between(start, end));
		return res;
	}

	/**
	 * 
	 * @param ford:
	 *            FecObjJRdd encapsulate the JavaRDD to convert, mime type,
	 *            separator & absolute path. Cluster processing Sum the debit
	 *            column using JavaRDD<String>
	 */
	public void sumDebit(FecObjJRdd ford) {

		Instant start = Instant.now();
		String firstline = ford.getLines().first();
		JavaRDD<Double> debitRDD = ford.getLines().filter(x -> !x.contains(firstline))
				.map(new Function<String, Double>() {
					@Override
					public Double call(String str) throws Exception {
						return str.split(ford.getSeparator().separator())[11].equals("") ? 0.0
								: Double.parseDouble(str.split(ford.getSeparator().separator())[11].replace(",", "."));
					}
				});
		if (firstline.split(ford.getSeparator().separator())[11].equals("Debit")) {
			logger.info("=== Debit(RDD):" + debitRDD.reduce((x, y) -> x + y));
			Instant end = Instant.now();
			logger.info("=== Duration(RDD) == " + Duration.between(start, end));
		} else {
			logger.info("=== Montant(RDD):" + debitRDD.reduce((x, y) -> x + y));
			Instant end = Instant.now();
			logger.info("=== Duration(RDD) == " + Duration.between(start, end));
		}

	}

	/**
	 * 
	 * @param ford:
	 *            FecObjJRdd encapsulate the JavaRDD to convert, mime type,
	 *            separator & absolute path. Cluster processing sum the credit
	 *            column using JavaRDD<String>
	 */
	public void sumCredit(FecObjJRdd ford) {

		Instant start = Instant.now();
		String firstline = ford.getLines().first();

		if (firstline.split(ford.getSeparator().separator())[11].equals("Debit")) {
			JavaRDD<Double> creditRDD = ford.getLines().filter(x -> !x.contains(firstline))
					.map(new Function<String, Double>() {
						@Override
						public Double call(String str) throws Exception {
							return str.split(ford.getSeparator().separator())[12].equals("") ? 0.0
									: Double.parseDouble(
											str.split(ford.getSeparator().separator())[12].replace(",", "."));
						}
					});
			logger.info("=== Credit(RDD):" + creditRDD.reduce((x, y) -> x + y));
			Instant end = Instant.now();
			logger.info("=== Duration(RDD) == " + Duration.between(start, end));
		} else {
			logger.debug("=== no Credit, montant sens file ===");
			Instant end = Instant.now();
			logger.info("=== Duration(RDD) == " + Duration.between(start, end));
		}
	}

}
