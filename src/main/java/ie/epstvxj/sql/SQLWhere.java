package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;

/**
 * @author jun
 *
 */
public class SQLWhere extends AbstractSQLConstruct {

	private int	searchConditionsIndex	= -1;

	@Override
	public boolean isValidSubRSC(final SQLConstruct subSQLConstruct) {
		if (subSQLConstruct.getGeneralType() == SQLConstructType.SEARCH_COND) {
			return true;
		} else if (subSQLConstruct.getGeneralType() == SQLConstructType.SEARCH_COND_AND) {
			return true;
		} else if (subSQLConstruct.getGeneralType() == SQLConstructType.SEARCH_COND_OR) {
			return true;
		} else if (subSQLConstruct.getGeneralType() == SQLConstructType.SUBSELECT) {
			return true;
		}
		return false;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.WHERE;
	}

	public SQLWhere withConditions(final SQLSearchConditions conditions) {
		this.searchConditionsIndex = this.getRepository().addSQLConstruct(conditions);
		conditions.setReferencingConstruct(this);
		return this;
	}

	public SQLSearchConditions getSearchConditions() {
		return (SQLSearchConditions) this.getRepository().getSQLConstruct(searchConditionsIndex);
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(SQLGrammarTokens.WHERE).append(SQLGrammarTokens.SPACE);

		sql.append(getSearchConditions().toSql());

		sql.append(super.toSql());

		return sql.toString();
	}

	@Override
	public SQLWhere withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLWhere withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLWhere deepClone() {
		SQLWhere cloneWhere = new SQLWhere().withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);

		cloneWhere.withConditions(getSearchConditions().deepClone());

		return cloneWhere;
	}

	@Override
	public SQLWhere deepCloneWithPreservedIdentifier() {
		SQLWhere cloneWhere = new SQLWhere().withCloneId(this.getIdentifier())
				.withSQLQueryContext(this.sqlQueryContext);

		cloneWhere.withConditions(getSearchConditions().deepCloneWithPreservedIdentifier());

		return cloneWhere;
	}

	@Override
	public SQLWhere withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
