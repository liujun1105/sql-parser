package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;

/**
 * @author jun
 *
 */
public class SQLColumn extends AbstractSQLConstruct {

	private String	columnName;

	private int		tableCorrelationNameIndex	= -1;
	private int		columnAliasIndex			= -1;

	public SQLColumn() {
	}

	public SQLColumn(final String columnName) {
		withColumnName(columnName);
	}

	public SQLColumn withColumnName(final String columnName) {
		this.columnName = columnName;
		return this;
	}

	public SQLColumn withCorrelationName(final SQLCorrelationName tableCorrelationName) {
		if (null != tableCorrelationName) {
			tableCorrelationNameIndex = this.getRSCRepository().addRSC(tableCorrelationName);
			tableCorrelationName.setReferencingRSC(this);
		}
		return this;
	}

	public SQLColumn withAlias(final SQLCorrelationName columnAlias) {
		if (null != columnAlias) {
			this.columnAliasIndex = this.getRSCRepository().addRSC(columnAlias);
			columnAlias.setReferencingRSC(this);
		}
		return this;
	}

	@Override
	public SQLColumn withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLColumn withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.COLUMN;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		if (-1 != tableCorrelationNameIndex) {
			sql.append(this.getRSCRepository().getRSC(tableCorrelationNameIndex).toSql()).append(SQLGrammarTokens.DOT);
		}

		sql.append(columnName);

		if (-1 != columnAliasIndex) {
			sql.append(SQLGrammarTokens.SPACE).append(SQLGrammarTokens.AS).append(SQLGrammarTokens.SPACE)
					.append(this.getRSCRepository().getRSC(columnAliasIndex).toSql());
		}

		return sql.toString();
	}

	public String getColumnName() {
		return columnName;
	}

	public SQLCorrelationName getTableCorrelationName() {
		return (SQLCorrelationName) getRSCRepository().getRSC(tableCorrelationNameIndex);
	}

	public SQLCorrelationName getColumnAlias() {
		return (SQLCorrelationName) getRSCRepository().getRSC(columnAliasIndex);
	}

	@Override
	public SQLColumn deepClone() {
		SQLColumn newColumn = new SQLColumn();
		newColumn.withColumnName(getColumnName());

		if (null != getColumnAlias()) {
			newColumn.withAlias(getColumnAlias().deepClone());
		}

		if (null != getTableCorrelationName()) {
			newColumn.withCorrelationName(getTableCorrelationName().deepClone());
		}
		newColumn.withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this)).withSQLQueryContext(
				this.sqlQueryContext);

		return newColumn;
	}

	@Override
	public SQLColumn deepCloneWithPreservedIdentifier() {
		SQLColumn newColumn = new SQLColumn();
		newColumn.withColumnName(getColumnName());

		if (null != getColumnAlias()) {
			newColumn.withAlias(getColumnAlias().deepCloneWithPreservedIdentifier());
		}

		if (null != getTableCorrelationName()) {
			newColumn.withCorrelationName(getTableCorrelationName().deepCloneWithPreservedIdentifier());
		}
		newColumn.withCloneId(this.getIdentifier()).withSQLQueryContext(this.sqlQueryContext);

		return newColumn;
	}

	@Override
	public SQLColumn withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
