/**
 * xAdd Project
 * 
 * @author jun 
 * @since 11 Mar 2014
 */
package ie.epstvxj.sql.value;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;
import ie.epstvxj.sql.tokens.SQLSign;

/**
 * @author jun
 *
 */
public class SQLIntValue extends SQLNumericValue {

	private final int	value;

	public SQLIntValue(final int value) {
		this.value = value;
	}

	@Override
	public SQLIntValue withSign(final SQLSign rscSign) {
		super.withSign(rscSign);
		return this;
	}

	@Override
	public String toSql() {
		if (null != sign()) {
			return sign().toSql() + this.value;
		}
		return "" + this.value;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.INT_VALUE;
	}

	@Override
	public Integer getValue() {
		if (null != sign() && sign().equals(SQLGrammarTokens.MINUS)) {
			return value * -1;
		}
		return value;
	}

	@Override
	public SQLIntValue withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLIntValue withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLIntValue deepClone() {
		return new SQLIntValue(value).withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLIntValue deepCloneWithPreservedIdentifier() {
		return new SQLIntValue(value).withCloneId(this.getIdentifier()).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLIntValue withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
