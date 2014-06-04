/**
 * xAdd Project
 * 
 * @author jun 
 * @since 11 Mar 2014
 */
package ie.epstvxj.sql.function;

import ie.epstvxj.sql.AbstractSQLConstruct;
import ie.epstvxj.sql.SQLConstruct;
import ie.epstvxj.sql.SQLConstructType;

/**
 * @author jun
 *
 */
public abstract class SQLFunction extends AbstractSQLConstruct {

	@Override
	public boolean isValidSubRSC(final SQLConstruct subSQLConstruct) {
		if (subSQLConstruct.getGeneralType() == SQLConstructType.FUNCTION) {
			return true;
		} else if (subSQLConstruct.getGeneralType() == SQLConstructType.SUBSELECT) {
			return true;
		} else if (subSQLConstruct.getActualType() == SQLConstructType.UNION) {
			return true;
		}
		return false;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.FUNCTION;
	}

	@Override
	public abstract SQLFunction deepClone();

	@Override
	public abstract SQLFunction deepCloneWithPreservedIdentifier();

}
