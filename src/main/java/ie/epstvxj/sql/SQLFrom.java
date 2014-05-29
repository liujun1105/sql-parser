package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;

import java.util.List;

public class SQLFrom extends AbstractSQLConstruct {

	@Override
	public boolean isValidSubRSC(final SQLConstruct subSQLConstruct) {
		if (subSQLConstruct.getGeneralType() == SQLConstructType.TARGET_RESOURCE) {
			return true;
		}

		return false;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.FROM;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(SQLGrammarTokens.FROM).append(SQLGrammarTokens.SPACE);

		sql.append(super.toSql());

		return sql.toString();
	}

	public SQLFrom withSource(final SQLConstruct sourceRSC) {
		this.addSubRSC(sourceRSC);
		return this;
	}

	@Override
	public SQLFrom deepClone() {
		SQLFrom cloneFrom = new SQLFrom().withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);

		List<SQLConstruct> subRSCList = getSubRSCList();

		for (SQLConstruct subSqlConstruct : subRSCList) {
			cloneFrom.withSource(subSqlConstruct.deepClone());
		}

		return cloneFrom;
	}

	@Override
	public SQLFrom deepCloneWithPreservedIdentifier() {
		SQLFrom cloneFrom = new SQLFrom().withCloneId(this.getIdentifier()).withSQLQueryContext(this.sqlQueryContext);

		List<SQLConstruct> subRSCList = getSubRSCList();

		for (SQLConstruct subSqlConstruct : subRSCList) {
			cloneFrom.withSource(subSqlConstruct.deepCloneWithPreservedIdentifier());
		}

		return cloneFrom;
	}

	@Override
	public SQLFrom withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLFrom withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLFrom withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
