package fr.bzh.rzh.service.fec;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import fr.bzh.rzh.fec.exceptions.ApplicationException;
import fr.bzh.rzh.fec.model.FecObjJRdd;
import fr.bzh.rzh.repository.IFecRepo;
import fr.bzh.rzh.service.IManager;
import fr.bzh.rzh.service.fec.rules.CompAuxEcLetRule;
import fr.bzh.rzh.service.fec.rules.DebCredByCodJourRule;
import fr.bzh.rzh.service.fec.rules.DebitCreditRule;
import fr.bzh.rzh.service.fec.rules.DeviseRule;
import fr.bzh.rzh.service.fec.rules.EOLRule;
import fr.bzh.rzh.service.fec.rules.ElRule;
import fr.bzh.rzh.service.fec.rules.FileNameRule;
import fr.bzh.rzh.service.fec.rules.HeaderRule;
import fr.bzh.rzh.service.fec.rules.IRules;
import fr.bzh.rzh.service.fec.rules.MandatoryRule;
import fr.bzh.rzh.service.fec.rules.MontantByCodJourRule;
import fr.bzh.rzh.service.fec.rules.MontantRule;
import fr.bzh.rzh.service.fec.rules.SensRule;

/**
 * 0xCAFEBABE
 * @author KHERBICHE L
 */
@SuppressWarnings("serial")
@Service
@Scope(value="prototype")
public class ManagerImpl implements IManager {

	private static final Log logger = LogFactory.getLog(ManagerImpl.class);
	
	private FecObjJRdd ford;
	
	@Autowired(required = true)
	private IFecRepo fecRepoImpl;
	
	
	//@Autowired
	//private Provider<FecRepoImpl> provider;
	
	//private IFecRepo getIFecRepoInstance() {
	//	fecRepoImpl = provider.get();
	//	return fecRepoImpl;
	//}
	/*
	 * @Inject Car(Provider<Seat> seatProvider) {
       Seat driver = seatProvider.get();
       Seat passenger = seatProvider.get();
       ...
     }*/
	

	@Override
	public void filetoFecObj(String path) throws ApplicationException {
		
		//getIFecRepoInstance();
		//logger.info("=== fecRepoImpl ==="+fecRepoImpl);
		ManagerHelper mh = new ManagerHelper();
		ford = new FecObjJRdd();
		
		logger.info("=== 	compute path to FecObjJRdd 	===");
		ford = mh.fileToFecObj(fecRepoImpl.fileTojrddstrBySess(path), path); //ford = mh.fileToFecObj(fecRepoImpl.fileTojrddstrByStrm(path), path);
		fecRepoImpl.processDataFrame(path, ford.getSeparator());
	}


	/**
	 * Service Layer Computing FecObjJRdd : + JavaRDD + Type Mime + Separator
	 * 
	 * @param path
	 *            : the absolute path of FEC file in SERVLET-Engine
	 * @return FecObjJRdd
	 * @throws ApplicationException 
	 */
	@Override
	public void doProcess(String path) {
		
		ManagerHelper mh = new ManagerHelper();
		
		/* // @note this part does the calculation using JavaRDD & Collection
		logger.info("=== Start JavaRDD Sum TEST ===");
		mh.sumDebit(ford); // to pass Test 3 EOFRule
		mh.sumCredit(ford);// to pass Test 3 EOFRule
		logger.info("=== END JavaRDD Sum TEST ===");
		logger.info("=== Start Collection Sum TEST ===");
		Map<String,List<String>> map = mh.rddToCollection(ford);
		if(map.containsKey("Debit")){
			logger.info("=== Debit(Collection):"+mh.sumColumn(map.get("Debit")));
		}
		if(map.containsKey("Credit")) {
			logger.info("=== Credit(Collection):"+mh.sumColumn(map.get("Credit")));
		}
		if(map.containsKey("Montant")) {
			logger.info("=== Montant(Collection):"+mh.sumColumn(map.get("Montant")));
		}
		logger.info("=== END Collection Sum	TEST  ===");
		*/
		
		//1
		IRules iRules = new FileNameRule();
		mh.setRule(iRules);
		try {
			mh.tryRule(path);
		} catch (ApplicationException e1) {}
		
		
		//5&6
		iRules = new HeaderRule();
		mh.setRule(iRules);
		try {
			mh.tryRule(ford.getLines().first().split(ford.getSeparator().separator()));
		} catch (ApplicationException e1) {}
		
		
		//3
		iRules = new EOLRule();
		mh.setRule(iRules);
		try {
			mh.tryRule(ford);
		} catch (ApplicationException e1) {}
		
		//20 & 32
		iRules = new MandatoryRule();
		mh.setRule(iRules);
		try {
			mh.tryRule(ford);
			/** mh.tryRule(fecRepoImpl.getDataFrame("R6")); //->45s*/ 
		} catch (ApplicationException e1) {}
		
		//31
		iRules = new DeviseRule();
		mh.setRule(iRules);
		try{
			mh.tryRule(ford);
		} catch (ApplicationException e) {}
		
		
		//73
		iRules = new CompAuxEcLetRule();
		mh.setRule(iRules);
		try{
			mh.tryRule(ford);
			} catch (ApplicationException e) {}
		
		
		String elevenCol = ford.getLines().first().split(ford.getSeparator().separator())[11];
		
		if (elevenCol.equals("Debit")) {
			
			//49
			//Dataset<Row> results = fecRepoImpl.getDataFrame("R1");
			iRules = new DebitCreditRule();
			mh.setRule(iRules);
			try {
				//logger.info("=== "+this+" mh:"+mh+" DebitCreditRule:"+iRules+" Object:"+results.hashCode()+" "+results.count());//
				mh.tryRule(ford);
			} catch(ApplicationException e) {}
			
			
			//50
			iRules = new DebCredByCodJourRule();
			mh.setRule(iRules);
			try {
				mh.tryRule(fecRepoImpl.getDataFrame("R8"));
			} catch(ApplicationException e) {}
			
			
			//78
			iRules = new ElRule();
			mh.setRule(iRules);
			try {
				mh.tryRule(fecRepoImpl.getDataFrame("R3"));
			} catch (ApplicationException e) {}
		}
		
		if(elevenCol.equals("Montant")) {
			
			//12
			Dataset<Row> results = fecRepoImpl.getDataFrame("R5");
			iRules = new SensRule();
			mh.setRule(iRules);
			try {
				mh.tryRule(results);
			} catch (ApplicationException e) {}
			
			//49
			results = fecRepoImpl.getDataFrame("R2");
			iRules = new MontantRule();
			mh.setRule(iRules);
			try {
				mh.tryRule(results);
			} catch (ApplicationException e) {}
			
			//50 bis
			iRules = new MontantByCodJourRule();
		    mh.setRule(iRules);
		    try {
		    	mh.tryRule(fecRepoImpl.getDataFrame("R10"));
		    } catch (ApplicationException e) {}
			
			//78
			results = fecRepoImpl.getDataFrame("R4");
			iRules = new ElRule();
			mh.setRule(iRules);
			try {
				mh.tryRule(results);
			} catch (ApplicationException e) {}
		}

	}

	
	@Override
	public void clean() {
		logger.info("=== Clean ===");
		ford = null;
		fecRepoImpl.clean();
	}
	@Override
	public void storeFile(String path) {
		logger.info("===	storeFile	===");
		logger.info("===	Path	===	" + path);
		fecRepoImpl.hdfsStoreFile(path);
	}

	
	@Override
	public void readFile(String path) throws InterruptedException {
		logger.info("===	Listening && Reading	===");
		fecRepoImpl.streamHdfsFile(path);
	}
	
}
