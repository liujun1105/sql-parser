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
public class SQLFloatValue extends SQLNumericValue {

	private final float	value;

	public SQLFloatValue(final float value) {
		this.value = value;
	}

	@Override
	public SQLFloatValue withSign(final SQLSign rscSign) {
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
		return SQLConstructType.FLOAT_VALUE;
	}

	@Override
	public Float getValue() {
		if (null != sign() && sign().equals(SQLGrammarTokens.MINUS)) {
			return value * -1;
		}
		return value;
	}

	@Override
	public SQLFloatValue withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLFloatValue withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLFloatValue deepClone() {
		return new SQLFloatValue(value).withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLFloatValue deepCloneWithPreservedIdentifier() {
		return new SQLFloatValue(value).withCloneId(this.getIdentifier()).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLFloatValue withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
