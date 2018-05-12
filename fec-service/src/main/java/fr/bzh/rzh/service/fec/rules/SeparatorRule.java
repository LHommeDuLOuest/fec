package fr.bzh.rzh.service.fec.rules;

import fr.bzh.rzh.fec.exceptions.SeparatorException;
import fr.bzh.rzh.fec.model.SeparatorEnum;

/**
 * 0xCAFEBABE
 * @author KHERBICHE L
 * Test 7: header ctrl separator 
 */
public class SeparatorRule implements IRules {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int Max_NB_COL = 21;
	private static final int Min_NB_COL = 18;

	@Override
	public Object tryRule(Object obj_) throws SeparatorException {

		String first_line_ = (String) obj_;
		
		if (first_line_.split(SeparatorEnum.TABULATION.separator()).length <= Max_NB_COL
				&& first_line_.split(SeparatorEnum.TABULATION.separator()).length >= Min_NB_COL) {
			//logger.info("=== Separator -> Tabulation ===");
			return SeparatorEnum.TABULATION;
		}
		if (first_line_.split(SeparatorEnum.PIPE.separator()).length <= Max_NB_COL
				&& first_line_.split(SeparatorEnum.PIPE.separator()).length >= Min_NB_COL) {
			//logger.info("=== Separator -> Pipe ===");
			return SeparatorEnum.PIPE;
		}
		if (first_line_.split(SeparatorEnum.COMMA.separator()).length <= Max_NB_COL
				&& first_line_.split(SeparatorEnum.COMMA.separator()).length >= Min_NB_COL) {
			//logger.debug("=== Separator -> Comma ===");
			return SeparatorEnum.COMMA;
			
		}
		logger.debug("=== Separator -> OTHER ===");
		throw new SeparatorException(SeparatorEnum.OTHER.separator());
		// return SeparatorEnum.OTHER;

	}

}
