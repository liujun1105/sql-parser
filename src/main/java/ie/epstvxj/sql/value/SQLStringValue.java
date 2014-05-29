package ie.epstvxj.sql.value;

import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;

public class SQLStringValue extends SQLValue implements Comparable<SQLStringValue> {

	private String	value;

	public SQLStringValue(final String value) {
		this.value = value;
		if (value.startsWith("'")) {
			this.value = value.substring(1);
		}
		if (value.endsWith("'")) {
			this.value = this.value.substring(0, this.value.length() - 1);
		}

	}

	@Override
	public String toSql() {
		return "'" + this.value + "'";
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.STRING_VALUE;
	}

	@Override
	public int compareTo(final SQLStringValue another) {
		if (another instanceof SQLStringValue) {
			SQLStringValue anotherRSCValue = another;
			return this.getValue().compareTo(anotherRSCValue.getValue());
		}
		return 1;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public SQLStringValue withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLStringValue withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLStringValue deepClone() {
		return new SQLStringValue(value).withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLStringValue deepCloneWithPreservedIdentifier() {
		return new SQLStringValue(value).withCloneId(this.getIdentifier()).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLStringValue withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
