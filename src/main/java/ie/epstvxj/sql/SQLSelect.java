/**
 * xAdd Project
 * 
 * @author jun 
 * @since 10 Mar 2014
 */
package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.parser.SQLGrammarTokens.SetQuantifier;

/**
 * @author jun
 *
 */
public class SQLSelect extends AbstractSQLConstruct {

	protected SetQuantifier	setQuantifier;

	private int				whereIndex		= -1;
	private int				fromIndex		= -1;
	private int				havingIndex		= -1;
	private int				groupByIndex	= -1;
	private int				orderByIndex	= -1;

	public SQLSelect() {

	}

	public SQLSelect withSelectedItem(final SQLConstruct selectedItem) {
		this.addSubRSC(selectedItem);
		return this;
	}

	public SQLSelect withSelectedItems(final SQLConstruct... selectedItems) {
		for (SQLConstruct selectedItem : selectedItems) {
			withSelectedItem(selectedItem);
		}
		return this;
	}

	public SQLSelect selectAll() {
		this.setQuantifier = SetQuantifier.ALL;
		return this;
	}

	public SQLSelect selectDistinct() {
		this.setQuantifier = SetQuantifier.DISTINCT;
		return this;
	}

	public SQLSelect withSetQuantifier(final SetQuantifier setQuantifier) {
		this.setQuantifier = setQuantifier;
		return this;
	}

	public SQLSelect withWhereClause(final SQLWhere where) {
		this.whereIndex = this.getRSCRepository().addRSC(where);
		where.setReferencingRSC(this);
		return this;
	}

	public SQLSelect withFromClause(final SQLFrom from) {
		this.fromIndex = this.getRSCRepository().addRSC(from);
		from.setReferencingRSC(this);
		return this;
	}

	public SQLSelect withHavingClause(final SQLHaving having) {
		this.havingIndex = this.getRSCRepository().addRSC(having);
		having.setReferencingRSC(this);
		return this;
	}

	public SQLSelect withGroupBy(final SQLGroupBy groupBy) {
		this.groupByIndex = this.getRSCRepository().addRSC(groupBy);
		groupBy.setReferencingRSC(this);
		return this;
	}

	public SQLSelect withOrderByClause(final SQLOrderBy orderBy) {
		this.orderByIndex = this.getRSCRepository().addRSC(orderBy);
		orderBy.setReferencingRSC(this);
		return this;
	}

	public SQLWhere getWhere() {
		return (SQLWhere) this.getRSCRepository().getRSC(whereIndex);
	}

	public SQLFrom getFrom() {
		return (SQLFrom) this.getRSCRepository().getRSC(fromIndex);
	}

	public SQLHaving getHaving() {
		return (SQLHaving) this.getRSCRepository().getRSC(havingIndex);
	}

	public SQLGroupBy getGroupBy() {
		return (SQLGroupBy) this.getRSCRepository().getRSC(groupByIndex);
	}

	public SQLOrderBy getOrderBy() {
		return (SQLOrderBy) this.getRSCRepository().getRSC(orderByIndex);
	}

	@Override
	public boolean isValidSubRSC(final SQLConstruct rsc) {
		if (rsc.getGeneralType() == SQLConstructType.COLUMN) {
			return true;
		} else if (rsc.getGeneralType() == SQLConstructType.FUNCTION) {
			return true;
		} else if (rsc.getActualType() == SQLConstructType.SUBSELECT) {
			return true;
		} else if (rsc.getGeneralType() == SQLConstructType.SELECTED_ITEM) {
			return true;
		} else if (rsc.getGeneralType() == SQLConstructType.VALUE) {
			return true;
		} else if (rsc.getGeneralType() == SQLConstructType.EXPRESSION) {
			return true;
		}

		return false;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.SELECT;
	}

	@Override
	public SQLConstructType getActualType() {
		return this.getGeneralType();
	}

	public void setSetQuantifier(final SetQuantifier setQuantifier) {
		this.setQuantifier = setQuantifier;
	}

	public SetQuantifier getSetQuantifier() {
		return this.setQuantifier;
	}

	@Override
	public String toSql() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(SQLGrammarTokens.SELECT).append(SQLGrammarTokens.SPACE);

		if (null != setQuantifier) {
			stringBuilder.append(this.getSetQuantifier()).append(SQLGrammarTokens.SPACE);
		}

		stringBuilder.append(super.toSql());

		if (-1 != fromIndex) {
			stringBuilder.append(SQLGrammarTokens.SPACE);
			stringBuilder.append(getFrom().toSql());
		}
		if (-1 != whereIndex) {
			stringBuilder.append(SQLGrammarTokens.SPACE);
			stringBuilder.append(getWhere().toSql());
		}
		if (-1 != groupByIndex) {
			stringBuilder.append(SQLGrammarTokens.SPACE);
			stringBuilder.append(getGroupBy().toSql());
		}
		if (-1 != havingIndex) {
			stringBuilder.append(SQLGrammarTokens.SPACE);
			stringBuilder.append(getHaving().toSql());
		}
		if (-1 != orderByIndex) {
			stringBuilder.append(SQLGrammarTokens.SPACE);
			stringBuilder.append(getOrderBy().toSql());
		}

		return stringBuilder.toString();
	}

	@Override
	public SQLSelect withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLSelect withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLSelect deepClone() {
		SQLSelect cloneSelect = new SQLSelect().withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);

		if (-1 != fromIndex) {
			cloneSelect.withFromClause(getFrom().deepClone());
		}
		if (-1 != whereIndex) {
			cloneSelect.withWhereClause(getWhere().deepClone());
		}
		if (-1 != havingIndex) {
			cloneSelect.withHavingClause(getHaving().deepClone());
		}
		if (-1 != groupByIndex) {
			cloneSelect.withGroupBy(getGroupBy().deepClone());
		}
		if (-1 != orderByIndex) {
			cloneSelect.withOrderByClause(getOrderBy().deepClone());
		}

		cloneSelect.setSetQuantifier(getSetQuantifier());

		for (SQLConstruct selectedItem : subRSCList) {
			cloneSelect.withSelectedItem(selectedItem.deepClone());
		}

		return cloneSelect;
	}

	@Override
	public SQLSelect deepCloneWithPreservedIdentifier() {
		SQLSelect cloneSelect = new SQLSelect().withCloneId(this.getIdentifier()).withSQLQueryContext(
				this.sqlQueryContext);

		if (-1 != fromIndex) {
			cloneSelect.withFromClause(getFrom().deepCloneWithPreservedIdentifier());
		}
		if (-1 != whereIndex) {
			cloneSelect.withWhereClause(getWhere().deepCloneWithPreservedIdentifier());
		}
		if (-1 != havingIndex) {
			cloneSelect.withHavingClause(getHaving().deepCloneWithPreservedIdentifier());
		}
		if (-1 != groupByIndex) {
			cloneSelect.withGroupBy(getGroupBy().deepCloneWithPreservedIdentifier());
		}
		if (-1 != orderByIndex) {
			cloneSelect.withOrderByClause(getOrderBy().deepCloneWithPreservedIdentifier());
		}

		cloneSelect.setSetQuantifier(getSetQuantifier());

		for (SQLConstruct selectedItem : subRSCList) {
			cloneSelect.withSelectedItem(selectedItem.deepCloneWithPreservedIdentifier());
		}

		return cloneSelect;
	}

	@Override
	public SQLSelect withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
