/**
 * xAdd Project
 * 
 * @author jun 
 * @since 10 Mar 2014
 */
package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;

/**
 * @author jun
 *
 */
public class SQLDelete extends AbstractSQLConstruct {

	private int	tableIndex	= -1;
	private int	whereIndex	= -1;

	public SQLDelete() {

	}

	public SQLDelete fromTable(final SQLTable table) {
		this.tableIndex = this.getRepository().addSQLConstruct(table);
		table.setReferencingConstruct(this);
		return this;
	}

	public SQLDelete withWhereClause(final SQLWhere where) {
		this.whereIndex = this.getRepository().addSQLConstruct(where);
		where.setReferencingConstruct(this);
		return this;
	}

	public SQLTable getTable() {
		return (SQLTable) this.getRepository().getSQLConstruct(tableIndex);
	}

	public SQLWhere getWhere() {
		return (SQLWhere) this.getRepository().getSQLConstruct(whereIndex);
	}

	@Override
	public SQLDelete withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLDelete withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(SQLGrammarTokens.DELETE).append(SQLGrammarTokens.SPACE);
		sql.append(SQLGrammarTokens.FROM).append(SQLGrammarTokens.SPACE);
		sql.append(getTable().toSql()).append(SQLGrammarTokens.SPACE);
		sql.append(getWhere().toSql());

		return sql.toString();
	}

	@Override
	public SQLDelete deepClone() {

		return new SQLDelete().withWhereClause(getWhere().deepClone()).fromTable(getTable().deepClone())
				.withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLDelete deepCloneWithPreservedIdentifier() {
		return new SQLDelete().withWhereClause(getWhere().deepCloneWithPreservedIdentifier())
				.fromTable(getTable().deepCloneWithPreservedIdentifier()).withCloneId(this.getIdentifier())
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.DELETE;
	}

	@Override
	public SQLDelete withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}

}
