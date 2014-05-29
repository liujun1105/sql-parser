package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.parser.SQLGrammarTokens.SetQuantifier;

import java.util.ArrayList;
import java.util.List;

public class SQLSubSelect extends SQLSelect implements SQLTargetResource {

	private int					aliasIndex		= -1;
	private final List<Integer>	joinIndexList	= new ArrayList<Integer>();

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.TARGET_RESOURCE;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.SUBSELECT;
	}

	public SQLSubSelect as(final SQLCorrelationName alias) {
		this.aliasIndex = this.getRSCRepository().addRSC(alias);
		alias.setReferencingRSC(this);
		return this;
	}

	@Override
	public SQLSubSelect withSelectedItem(final SQLConstruct selectedItem) {
		this.addSubRSC(selectedItem);
		return this;
	}

	@Override
	public SQLSubSelect withSetQuantifier(final SetQuantifier setQuantifier) {
		this.setQuantifier = setQuantifier;
		return this;
	}

	@Override
	public SQLSubSelect withWhereClause(final SQLWhere where) {
		super.withWhereClause(where);
		return this;
	}

	@Override
	public SQLSubSelect withFromClause(final SQLFrom from) {
		super.withFromClause(from);
		return this;
	}

	@Override
	public SQLSubSelect withHavingClause(final SQLHaving having) {
		super.withHavingClause(having);
		return this;
	}

	@Override
	public SQLSubSelect withGroupBy(final SQLGroupBy groupBy) {
		super.withGroupBy(groupBy);
		return this;
	}

	@Override
	public SQLSubSelect withOrderByClause(final SQLOrderBy orderBy) {
		super.withOrderByClause(orderBy);
		return this;
	}

	public SQLCorrelationName getAlias() {
		return (SQLCorrelationName) this.getRSCRepository().getRSC(aliasIndex);
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(SQLGrammarTokens.LEFT_PAREN);
		sql.append(super.toSql());
		sql.append(SQLGrammarTokens.RIGHT_PAREN);

		if (-1 != aliasIndex) {
			sql.append(SQLGrammarTokens.SPACE);
			sql.append(SQLGrammarTokens.AS);
			sql.append(SQLGrammarTokens.SPACE);
			sql.append(getAlias().toSql());
		}

		int numOfJoins = getJoins().size();
		if (numOfJoins > 0) {
			for (SQLJoin join : getJoins()) {
				sql.append(SQLGrammarTokens.SPACE);
				sql.append(join.toSql());
			}
		}

		return sql.toString();
	}

	@Override
	public SQLSubSelect withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLSubSelect withJoin(final SQLJoin join) {
		this.joinIndexList.add(this.getRSCRepository().addRSC(join));
		join.setReferencingRSC(this);
		return this;
	}

	@Override
	public SQLSubSelect withCorrelationName(final SQLCorrelationName correlationName) {
		this.as(correlationName);
		return this;
	}

	@Override
	public SQLCorrelationName getTableCorrelation() {
		return getAlias();
	}

	@Override
	public String getTableCorrelationName() {
		if (null != getTableCorrelation()) {
			return getTableCorrelation().getName();
		}
		return null;
	}

	@Override
	public List<SQLJoin> getJoins() {
		List<SQLJoin> joinList = new ArrayList<SQLJoin>();
		for (Integer joinIndex : joinIndexList) {
			joinList.add((SQLJoin) this.getRSCRepository().getRSC(joinIndex));
		}
		return joinList;
	}

	@Override
	public SQLSubSelect withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLSubSelect deepClone() {
		SQLSubSelect cloneSubSelect = new SQLSubSelect().withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this));

		cloneSubSelect.withSQLQueryContext(this.sqlQueryContext);

		if (null != getFrom()) {
			cloneSubSelect.withFromClause(getFrom().deepClone());
		}
		if (null != getWhere()) {
			cloneSubSelect.withWhereClause(getWhere().deepClone());
		}
		if (null != getHaving()) {
			cloneSubSelect.withHavingClause(getHaving().deepClone());
		}
		if (null != getGroupBy()) {
			cloneSubSelect.withGroupBy(getGroupBy().deepClone());
		}
		if (null != getOrderBy()) {
			cloneSubSelect.withOrderByClause(getOrderBy().deepClone());
		}

		cloneSubSelect.setSetQuantifier(getSetQuantifier());

		for (SQLConstruct selectedItem : subRSCList) {
			cloneSubSelect.withSelectedItem(selectedItem.deepClone());
		}

		if (null != getAlias()) {
			cloneSubSelect.as(getAlias().deepClone());
		}

		return cloneSubSelect;
	}

	@Override
	public SQLSubSelect deepCloneWithPreservedIdentifier() {
		SQLSubSelect cloneSubSelect = new SQLSubSelect().withCloneId(this.getIdentifier());

		cloneSubSelect.withSQLQueryContext(this.sqlQueryContext);

		if (null != getFrom()) {
			cloneSubSelect.withFromClause(getFrom().deepCloneWithPreservedIdentifier());
		}
		if (null != getWhere()) {
			cloneSubSelect.withWhereClause(getWhere().deepCloneWithPreservedIdentifier());
		}
		if (null != getHaving()) {
			cloneSubSelect.withHavingClause(getHaving().deepCloneWithPreservedIdentifier());
		}
		if (null != getGroupBy()) {
			cloneSubSelect.withGroupBy(getGroupBy().deepCloneWithPreservedIdentifier());
		}
		if (null != getOrderBy()) {
			cloneSubSelect.withOrderByClause(getOrderBy().deepCloneWithPreservedIdentifier());
		}

		cloneSubSelect.setSetQuantifier(getSetQuantifier());

		for (SQLConstruct selectedItem : subRSCList) {
			cloneSubSelect.withSelectedItem(selectedItem.deepCloneWithPreservedIdentifier());
		}

		if (null != getAlias()) {
			cloneSubSelect.as(getAlias().deepCloneWithPreservedIdentifier());
		}

		return cloneSubSelect;
	}

}
