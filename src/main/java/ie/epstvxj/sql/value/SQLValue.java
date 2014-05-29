/**
 * xAdd Project
 * 
 * @author jun 
 * @since 10 Mar 2014
 */
package ie.epstvxj.sql.value;

import ie.epstvxj.sql.AbstractSQLConstruct;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;

/**
 * @author jun
 *
 */
public abstract class SQLValue extends AbstractSQLConstruct {

	@Override
	public abstract SQLValue deepClone();

	@Override
	public abstract SQLValue deepCloneWithPreservedIdentifier();

	public abstract Object getValue();

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.VALUE;
	}

	@Override
	public SQLValue withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
