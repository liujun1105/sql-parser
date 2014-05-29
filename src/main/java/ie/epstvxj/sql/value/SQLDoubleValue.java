package ie.epstvxj.sql.value;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;
import ie.epstvxj.sql.tokens.SQLSign;

public class SQLDoubleValue extends SQLNumericValue {

	private final double	value;

	public SQLDoubleValue(final double value) {
		this.value = value;
	}

	@Override
	public SQLDoubleValue withSign(final SQLSign rscSign) {
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
		return SQLConstructType.DOUBLE_VALUE;
	}

	@Override
	public SQLDoubleValue withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLDoubleValue withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public Double getValue() {
		if (null != sign() && sign().equals(SQLGrammarTokens.MINUS)) {
			return value * -1;
		}
		return value;
	}

	@Override
	public SQLDoubleValue deepClone() {
		return new SQLDoubleValue(value).withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLDoubleValue deepCloneWithPreservedIdentifier() {
		return new SQLDoubleValue(value).withCloneId(this.getIdentifier()).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLDoubleValue withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
