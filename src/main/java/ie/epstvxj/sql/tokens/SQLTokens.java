package ie.epstvxj.sql.tokens;

import ie.epstvxj.sql.AbstractSQLConstruct;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;

/**
 * @author jun
 *
 */
public abstract class SQLTokens extends AbstractSQLConstruct {

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.TOKENS;
	}

	@Override
	public SQLTokens withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
