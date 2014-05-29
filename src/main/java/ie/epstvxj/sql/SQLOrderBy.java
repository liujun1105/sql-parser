package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.parser.SQLGrammarTokens.OrderingSpecification;

import java.util.ArrayList;
import java.util.List;

public class SQLOrderBy extends AbstractSQLConstruct {

	private OrderingSpecification	orderingSpecification;

	/**
	 * Column names in the sequence of addition to RSCOrderBy
	 */
	private final List<String>		orderByColumnNames	= new ArrayList<String>();

	public SQLOrderBy() {

	}

	public SQLOrderBy(final SQLConstruct[] orderByItems) {
		byColumns(orderByItems);
	}

	@Override
	public boolean isValidSubRSC(final SQLConstruct subSQLConstruct) {
		if (subSQLConstruct.getGeneralType() == SQLConstructType.COLUMN) {
			return true;
		}
		return false;
	}

	public SQLOrderBy byColumns(final SQLConstruct... orderByItems) {
		for (SQLConstruct orderByItem : orderByItems) {
			byColumn(orderByItem);
		}
		return this;
	}

	public SQLOrderBy byColumn(final SQLConstruct orderByItem) {
		this.addSubRSC(orderByItem);
		if (orderByItem.getActualType() == SQLConstructType.COLUMN) {
			orderByColumnNames.add(((SQLColumn) orderByItem).getColumnName());
		}

		return this;
	}

	public SQLOrderBy inAscendingOrder() {
		orderingSpecification = OrderingSpecification.ASC;
		return this;
	}

	public SQLOrderBy inDescendingOrder() {
		orderingSpecification = OrderingSpecification.DESC;
		return this;
	}

	public OrderingSpecification getOrderingSpecification() {

		//default value 
		if (null == this.orderingSpecification) {
			orderingSpecification = OrderingSpecification.ASC;
		}

		return this.orderingSpecification;
	}

	/**
	 * Used in Result Set Proxy
	 * Can not be null
	 * @return
	 */
	public List<String> getOrderByColumnNames() {
		return this.orderByColumnNames;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(SQLGrammarTokens.ORDER_BY).append(SQLGrammarTokens.SPACE);
		sql.append(super.toSql());

		if (null != orderingSpecification) {
			sql.append(SQLGrammarTokens.SPACE).append(orderingSpecification);
		}

		return sql.toString();
	}

	@Override
	public SQLOrderBy withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLOrderBy withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.ORDER_BY;
	}

	@Override
	public SQLConstructType getActualType() {
		return this.getGeneralType();
	}

	@Override
	public SQLOrderBy deepClone() {
		SQLOrderBy cloneOrderBy = new SQLOrderBy().withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);

		for (SQLConstruct orderByItem : subRSCList) {
			cloneOrderBy.byColumn(orderByItem.deepClone());
		}

		if (getOrderingSpecification() == OrderingSpecification.ASC) {
			cloneOrderBy.inAscendingOrder();
		} else {
			cloneOrderBy.inDescendingOrder();
		}

		return cloneOrderBy;
	}

	@Override
	public SQLOrderBy deepCloneWithPreservedIdentifier() {
		SQLOrderBy cloneOrderBy = new SQLOrderBy().withCloneId(this.getIdentifier()).withSQLQueryContext(
				this.sqlQueryContext);

		for (SQLConstruct orderByItem : subRSCList) {
			cloneOrderBy.byColumn(orderByItem.deepCloneWithPreservedIdentifier());
		}

		if (getOrderingSpecification() == OrderingSpecification.ASC) {
			cloneOrderBy.inAscendingOrder();
		} else {
			cloneOrderBy.inDescendingOrder();
		}

		return cloneOrderBy;
	}

	@Override
	public SQLOrderBy withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
