package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;

public class SQLHaving extends AbstractSQLConstruct {

	private int	searchConditionsIndex	= -1;

	public SQLHaving() {

	}

	public SQLHaving(final SQLSearchConditions conditions) {
		withConditions(conditions);
	}

	public SQLHaving withConditions(final SQLSearchConditions conditions) {
		this.searchConditionsIndex = this.getRSCRepository().addRSC(conditions);
		conditions.setReferencingRSC(this);
		return this;
	}

	public SQLSearchConditions getSearchConditions() {
		return (SQLSearchConditions) this.getRSCRepository().getRSC(this.searchConditionsIndex);
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(SQLGrammarTokens.HAVING).append(SQLGrammarTokens.SPACE);
		sql.append(getSearchConditions().toSql());

		return sql.toString();
	}

	@Override
	public SQLHaving withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLHaving withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.HAVING;
	}

	@Override
	public SQLHaving deepClone() {
		SQLHaving cloneHavling = new SQLHaving().withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
		if (-1 != searchConditionsIndex) {
			cloneHavling.withConditions(getSearchConditions().deepClone());
		}
		return cloneHavling;
	}

	@Override
	public SQLHaving deepCloneWithPreservedIdentifier() {
		SQLHaving cloneHavling = new SQLHaving().withCloneId(this.getIdentifier()).withSQLQueryContext(
				this.sqlQueryContext);
		if (-1 != searchConditionsIndex) {
			cloneHavling.withConditions(getSearchConditions().deepCloneWithPreservedIdentifier());
		}
		return cloneHavling;
	}

	@Override
	public SQLHaving withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
