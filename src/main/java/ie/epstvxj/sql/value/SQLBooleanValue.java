/**
 * xAdd Project
 * 
 * @author jun 
 * @since 2 Apr 2014
 */
package ie.epstvxj.sql.value;

import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;

/**
 * @author jun
 *
 */
public class SQLBooleanValue extends SQLValue {

	private boolean	value;

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.VALUE;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.BOOLEAN_VALUE;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(value);

		return sql.toString();
	}

	@Override
	public SQLBooleanValue withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLBooleanValue withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	public SQLBooleanValue withValue(final boolean value) {
		this.value = value;
		return this;
	}

	@Override
	public Boolean getValue() {
		return value;
	}

	@Override
	public SQLBooleanValue deepClone() {
		return new SQLBooleanValue().withValue(value)
				.withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLBooleanValue deepCloneWithPreservedIdentifier() {
		return new SQLBooleanValue().withValue(value).withCloneId(this.getIdentifier());
	}

	@Override
	public SQLBooleanValue withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
