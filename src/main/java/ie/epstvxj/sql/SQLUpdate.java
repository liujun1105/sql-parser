package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;

/**
 * @author jun
 *
 */
public class SQLUpdate extends AbstractSQLConstruct {

	private int	tableIndex	= -1;
	private int	whereIndex	= -1;

	public SQLUpdate() {

	}

	public SQLUpdate onTable(final SQLTable table) {
		this.tableIndex = this.getRepository().addSQLConstruct(table);
		table.setReferencingConstruct(this);
		return this;
	}

	public SQLUpdate withWhereClause(final SQLWhere where) {
		this.whereIndex = this.getRepository().addSQLConstruct(where);
		where.setReferencingConstruct(this);
		return this;
	}

	public SQLUpdate withSetItem(final SQLConstruct sqlConstruct) {
		this.addSubRSC(sqlConstruct);
		return this;
	}

	public SQLTable getTable() {
		return (SQLTable) this.getRepository().getSQLConstruct(tableIndex);
	}

	public SQLWhere getWhere() {
		return (SQLWhere) this.getRepository().getSQLConstruct(whereIndex);
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.UPDATE;
	}

	@Override
	public boolean isValidSubRSC(final SQLConstruct subSQLConstruct) {
		if (subSQLConstruct.getGeneralType() == SQLConstructType.FUNCTION) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(SQLGrammarTokens.UPDATE).append(SQLGrammarTokens.SPACE);
		sql.append(getTable().toSql()).append(SQLGrammarTokens.SPACE);
		sql.append(SQLGrammarTokens.SET).append(SQLGrammarTokens.SPACE);
		sql.append(super.toSql());

		if (-1 != whereIndex) {
			sql.append(SQLGrammarTokens.SPACE).append(getWhere().toSql());
		}

		return sql.toString();
	}

	@Override
	public SQLUpdate withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLUpdate withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLUpdate deepClone() {
		SQLUpdate cloneUpdate = new SQLUpdate().withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
		cloneUpdate.onTable(getTable().deepClone()).withWhereClause(getWhere().deepClone());

		for (SQLConstruct subSqlConstruct : subRSCList) {
			cloneUpdate.withSetItem(subSqlConstruct.deepClone());
		}

		return cloneUpdate;
	}

	@Override
	public SQLUpdate deepCloneWithPreservedIdentifier() {
		SQLUpdate cloneUpdate = new SQLUpdate().withCloneId(this.getIdentifier()).withSQLQueryContext(
				this.sqlQueryContext);
		cloneUpdate.onTable(getTable().deepCloneWithPreservedIdentifier()).withWhereClause(
				getWhere().deepCloneWithPreservedIdentifier());

		for (SQLConstruct subSqlConstruct : subRSCList) {
			cloneUpdate.withSetItem(subSqlConstruct.deepCloneWithPreservedIdentifier());
		}

		return cloneUpdate;
	}

	@Override
	public SQLUpdate withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
