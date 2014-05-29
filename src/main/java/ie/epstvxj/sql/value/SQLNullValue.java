/**
 * xAdd Project
 * 
 * @author jun 
 * @since 27 Mar 2014
 */
package ie.epstvxj.sql.value;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;

/**
 * @author jun
 *
 */
public class SQLNullValue extends SQLValue {

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.NULL_VALUE;
	}

	@Override
	public SQLNullValue withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLNullValue withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public String getValue() {
		return SQLGrammarTokens.NULL;
	}

	@Override
	public String toSql() {
		return getValue();
	}

	@Override
	public SQLNullValue deepClone() {
		return new SQLNullValue().withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLNullValue deepCloneWithPreservedIdentifier() {
		return new SQLNullValue().withCloneId(this.getIdentifier()).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLNullValue withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
