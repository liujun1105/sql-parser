package ie.epstvxj.sql.value;

import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;

public class SQLAsterisk extends SQLValue {

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.ASTERISK;
	}

	@Override
	public SQLAsterisk withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLAsterisk withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public String getValue() {
		return "*";
	}

	@Override
	public String toSql() {
		return getValue();
	}

	@Override
	public SQLAsterisk deepClone() {
		return new SQLAsterisk().withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLAsterisk deepCloneWithPreservedIdentifier() {
		return new SQLAsterisk().withCloneId(this.getIdentifier()).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLAsterisk withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
