package ie.epstvxj.sql.tokens;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.SQLConstruct;
import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;

public class SQLNotToken extends SQLTokens {

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.NOT_TOKEN;
	}

	@Override
	public SQLConstruct withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLNotToken withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(SQLGrammarTokens.NOT);

		return sql.toString();
	}

	@Override
	public SQLNotToken deepClone() {
		return new SQLNotToken().withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this));
	}

	@Override
	public SQLNotToken deepCloneWithPreservedIdentifier() {
		return new SQLNotToken().withCloneId(this.getIdentifier());
	}

	@Override
	public SQLNotToken withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
