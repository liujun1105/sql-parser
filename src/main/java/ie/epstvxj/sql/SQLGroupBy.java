package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;

import java.util.List;

public class SQLGroupBy extends AbstractSQLConstruct {

	public SQLGroupBy() {

	}

	public SQLGroupBy(final SQLConstruct[] groupByItems) {
		byColumns(groupByItems);
	}

	@Override
	public boolean isValidSubRSC(final SQLConstruct subSQLConstruct) {
		if (subSQLConstruct.getGeneralType() == SQLConstructType.COLUMN) {
			return true;
		}
		return false;
	}

	public SQLGroupBy byColumns(final SQLConstruct... orderByItems) {
		for (SQLConstruct orderByItem : orderByItems) {
			byColumn(orderByItem);
		}
		return this;
	}

	public SQLGroupBy byColumn(final SQLConstruct orderByItem) {
		this.addSubRSC(orderByItem);
		return this;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(SQLGrammarTokens.GROUP_BY).append(SQLGrammarTokens.SPACE);
		sql.append(super.toSql());

		return sql.toString();
	}

	@Override
	public SQLGroupBy deepClone() {
		SQLGroupBy cloneGroupBy = new SQLGroupBy().withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);

		List<SQLConstruct> subRSCList = getSubRSCList();

		for (SQLConstruct subSqlConstruct : subRSCList) {
			cloneGroupBy.byColumn(subSqlConstruct.deepClone());
		}

		return cloneGroupBy;
	}

	@Override
	public SQLGroupBy deepCloneWithPreservedIdentifier() {
		SQLGroupBy cloneGroupBy = new SQLGroupBy().withCloneId(this.getIdentifier()).withSQLQueryContext(
				this.sqlQueryContext);

		List<SQLConstruct> subRSCList = getSubRSCList();

		for (SQLConstruct subSqlConstruct : subRSCList) {
			cloneGroupBy.byColumn(subSqlConstruct.deepCloneWithPreservedIdentifier());
		}

		return cloneGroupBy;
	}

	@Override
	public SQLGroupBy withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLGroupBy withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.GROUP_BY;
	}

	@Override
	public SQLGroupBy withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
