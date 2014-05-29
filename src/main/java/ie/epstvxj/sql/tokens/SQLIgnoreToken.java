package ie.epstvxj.sql.tokens;

import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;

/**
 * @author jun
 *
 */
public class SQLIgnoreToken extends SQLTokens {

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.IGNORE_TOKEN;
	}

	@Override
	public SQLIgnoreToken withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLIgnoreToken withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLIgnoreToken deepClone() {
		return new SQLIgnoreToken().withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this));
	}

	@Override
	public SQLIgnoreToken deepCloneWithPreservedIdentifier() {
		return new SQLIgnoreToken().withCloneId(this.getIdentifier());
	}

	@Override
	public SQLIgnoreToken withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
