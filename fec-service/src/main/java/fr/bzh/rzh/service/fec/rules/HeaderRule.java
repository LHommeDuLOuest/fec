package fr.bzh.rzh.service.fec.rules;

import java.util.Arrays;
import java.util.stream.Stream;

import fr.bzh.rzh.fec.exceptions.HeaderException;
import fr.bzh.rzh.fec.model.HeaderDCEnum;
import fr.bzh.rzh.fec.model.HeaderMSEnum;

/**
 * 0xCAFEBABE
 * @author KHERBICHE L
 * Test 5 & 6 Ctrl name & order of a header
 */

public class HeaderRule implements IRules {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Object tryRule(Object obj_) throws HeaderException {
		
		String[] first_line_Arr_ = (String[]) obj_;
		
		String[] header_dc_Arr_ = Stream.of(HeaderDCEnum.values())
				 .map(HeaderDCEnum::name)
				 .toArray(String[]::new);
		
		String[] _header_ms_Arr_ = Stream.of(HeaderMSEnum.values())
	             .map(HeaderMSEnum::name)
	             .toArray(String[]::new);
		
		if(first_line_Arr_.length>=18 && first_line_Arr_.length<=21) {
			first_line_Arr_ = Arrays.copyOfRange(first_line_Arr_, 0, 18);
		} else {
			//logger.debug("=== header is not valid, must be [18,21]");
			throw new HeaderException("header is not valid, must be [18,21]");
		}
		
		if(Arrays.equals(_header_ms_Arr_, first_line_Arr_) || Arrays.equals(header_dc_Arr_, first_line_Arr_)) {
			//logger.info("=== header is valide");
		} else {
			//logger.debug("=== the header order is not valid");
			throw new HeaderException("the header order is not valid");
		}
		
		//to delete
		//if(!Arrays.equals(headermsArr, firstlineArr)) {
		//	logger.debug("---- Arrays not equals -----------");
		//	logger.debug("ms lng:"+headermsArr.length+" first lng:"+firstlineArr.length);
		//	for (int i=0; i<headermsArr.length; i++) {
		//		if(!headermsArr[i].toString().equals(firstlineArr[i].toString())) {
		//			logger.debug("-"+headermsArr[i]+ "-"+firstlineArr[i]);
		//		}
		//	}
		//}
		
		
		return null;
	}

}
