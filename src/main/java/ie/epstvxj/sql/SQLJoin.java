package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;

/**
 * @author jun
 *
 */
public class SQLJoin extends AbstractSQLConstruct {

	private SQLJoinType	joinType;

	private int			targetSourceIndex		= -1;
	private int			searchConditionsIndex	= -1;

	public SQLJoin() {

	}

	public SQLJoin(final SQLJoinType joinType) {
		this.joinType = joinType;
	}

	public SQLJoin onTable(final SQLTargetResource targetSource) {
		this.targetSourceIndex = getRSCRepository().addRSC(targetSource);
		targetSource.setReferencingRSC(this);

		return this;
	}

	public SQLJoin withConditions(final SQLSearchConditions conditions) {
		this.searchConditionsIndex = getRSCRepository().addRSC(conditions);
		conditions.setReferencingRSC(this);
		return this;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(joinType.toSql()).append(SQLGrammarTokens.SPACE);
		sql.append(getTargetSource().toSql()).append(SQLGrammarTokens.SPACE);
		if (-1 != this.searchConditionsIndex) {
			sql.append(SQLGrammarTokens.ON).append(SQLGrammarTokens.SPACE).append(getConditions().toSql());
		}
		return sql.toString();
	}

	public SQLJoinType getJoinType() {
		return joinType;
	}

	public SQLTargetResource getTargetSource() {
		return (SQLTargetResource) this.getRSCRepository().getRSC(targetSourceIndex);
	}

	public SQLSearchConditions getConditions() {
		return (SQLSearchConditions) this.getRSCRepository().getRSC(searchConditionsIndex);
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.JOIN;
	}

	@Override
	public SQLConstructType getActualType() {
		if (joinType == SQLJoinType.CROSS_JOIN) {
			return SQLConstructType.CROSS_JOIN;
		} else if (joinType == SQLJoinType.FULL_JOIN) {
			return SQLConstructType.FULL_JOIN;
		} else if (joinType == SQLJoinType.INNER_JOIN) {
			return SQLConstructType.INNER_JOIN;
		} else if (joinType == SQLJoinType.LEFT_JOIN) {
			return SQLConstructType.LEFT_JOIN;
		} else if (joinType == SQLJoinType.NATURAL_FULL_JOIN) {
			return SQLConstructType.NATURAL_FULL_JOIN;
		} else if (joinType == SQLJoinType.NATURAL_INNER_JOIN) {
			return SQLConstructType.NATURAL_INNER_JOIN;
		} else if (joinType == SQLJoinType.NATURAL_JOIN) {
			return SQLConstructType.NATURAL_JOIN;
		} else if (joinType == SQLJoinType.NATURAL_LEFT_JOIN) {
			return SQLConstructType.NATURAL_LEFT_JOIN;
		} else if (joinType == SQLJoinType.NATURAL_RIGHT_JOIN) {
			return SQLConstructType.NATURAL_RIGHT_JOIN;
		} else if (joinType == SQLJoinType.RIGHT_JOIN) {
			return SQLConstructType.RIGHT_JOIN;
		} else if (joinType == SQLJoinType.UNION_JOIN) {
			return SQLConstructType.UNION_JOIN;
		} else {
			return SQLConstructType.JOIN;
		}
	}

	@Override
	public SQLJoin withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLJoin withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLJoin deepClone() {
		return new SQLJoin(joinType).withConditions(getConditions().deepClone()).onTable(getTargetSource().deepClone())
				.withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLJoin deepCloneWithPreservedIdentifier() {
		return new SQLJoin(joinType).withConditions(getConditions().deepCloneWithPreservedIdentifier())
				.onTable(getTargetSource().deepCloneWithPreservedIdentifier()).withCloneId(this.getIdentifier())
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLJoin withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
