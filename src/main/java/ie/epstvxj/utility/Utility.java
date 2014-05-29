package ie.epstvxj.utility;

import ie.epstvxj.sql.SQLColumn;
import ie.epstvxj.sql.SQLConstruct;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLDelete;
import ie.epstvxj.sql.SQLFrom;
import ie.epstvxj.sql.SQLGroupBy;
import ie.epstvxj.sql.SQLHaving;
import ie.epstvxj.sql.SQLInsert;
import ie.epstvxj.sql.SQLJoin;
import ie.epstvxj.sql.SQLLogicalAND;
import ie.epstvxj.sql.SQLLogicalOR;
import ie.epstvxj.sql.SQLNumericValueExpression;
import ie.epstvxj.sql.SQLOrderBy;
import ie.epstvxj.sql.SQLQueryContext;
import ie.epstvxj.sql.SQLSearchConditions;
import ie.epstvxj.sql.SQLSelect;
import ie.epstvxj.sql.SQLSubSelect;
import ie.epstvxj.sql.SQLTable;
import ie.epstvxj.sql.SQLTargetResource;
import ie.epstvxj.sql.SQLUpdate;
import ie.epstvxj.sql.SQLWhere;
import ie.epstvxj.sql.function.SQLAssignmentFunction;
import ie.epstvxj.sql.function.SQLAvgFunction;
import ie.epstvxj.sql.function.SQLBetweenFunction;
import ie.epstvxj.sql.function.SQLComparisonFunction;
import ie.epstvxj.sql.function.SQLCountFunction;
import ie.epstvxj.sql.function.SQLExistsFunction;
import ie.epstvxj.sql.function.SQLFunction;
import ie.epstvxj.sql.function.SQLInFunction;
import ie.epstvxj.sql.function.SQLLikeFunction;
import ie.epstvxj.sql.function.SQLMaxFunction;
import ie.epstvxj.sql.function.SQLMinFunction;
import ie.epstvxj.sql.function.SQLNullFunction;
import ie.epstvxj.sql.function.SQLSumFunction;
import ie.epstvxj.sql.value.SQLOperator;
import ie.epstvxj.sql.value.SQLValue;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jun
 *
 */
public class Utility {

	public static boolean isBooleanValue(final String value) {
		if ("true".equalsIgnoreCase(value)) {
			return true;
		} else if ("false".equalsIgnoreCase(value)) {
			return true;
		}
		return false;
	}

	public static boolean isNumber(final String value) {
		Pattern pattern = Pattern.compile("(\\d)+|(\\d+\\.\\d+)");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}

	public static boolean isFloatNumber(final String value) {
		Pattern pattern = Pattern.compile("\\d+\\.\\d+");
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}

	public static void updateSQLQueryContext(final SQLConstruct rsc, final SQLQueryContext sqlQueryContext) {
		analyseHelper(rsc, sqlQueryContext);
	}

	private static void analyseHelper(final SQLConstruct rsc, SQLQueryContext sqlQueryContext) {

		if (null != rsc) {

			if (rsc.getActualType() == SQLConstructType.SELECT) {
				analyseSelect((SQLSelect) rsc, sqlQueryContext);
			} else if (rsc.getActualType() == SQLConstructType.SUBSELECT) {
				sqlQueryContext = SQLQueryContext.NESTED_SELECT;
				analyseSelect((SQLSelect) rsc, sqlQueryContext);
			} else if (rsc.getActualType() == SQLConstructType.INSERT) {
				analyseInsert((SQLInsert) rsc, sqlQueryContext);
			} else if (rsc.getActualType() == SQLConstructType.DELETE) {
				analyseDelete((SQLDelete) rsc, sqlQueryContext);
			} else if (rsc.getActualType() == SQLConstructType.UPDATE) {
				analyseUpdate((SQLUpdate) rsc, sqlQueryContext);
			} else if (rsc.getActualType() == SQLConstructType.FUNCTION) {
				analyseFunction((SQLFunction) rsc, sqlQueryContext);
			} else if (rsc.getActualType() == SQLConstructType.COLUMN) {
				analyseColumn((SQLColumn) rsc, sqlQueryContext);
			} else if (rsc.getActualType() == SQLConstructType.TABLE) {
				analyseTable((SQLTable) rsc, sqlQueryContext);
			} else if (rsc.getGeneralType() == SQLConstructType.SEARCH_COND) {

				if (rsc.getActualType() == SQLConstructType.SEARCH_COND_AND) {
					analyseLogicalAnd((SQLLogicalAND) rsc, sqlQueryContext);
				} else if (rsc.getActualType() == SQLConstructType.SEARCH_COND_OR) {
					analyseLogicalOr((SQLLogicalOR) rsc, sqlQueryContext);
				} else {
					analyseConditionTree(((SQLSearchConditions) rsc).getRoot(), sqlQueryContext);
				}
			} else if (rsc.getGeneralType() == SQLConstructType.VALUE) {
				analyseValue((SQLValue) rsc, sqlQueryContext);
			} else if (rsc.getGeneralType() == SQLConstructType.FUNCTION) {
				analyseFunction((SQLFunction) rsc, sqlQueryContext);
			} else if (rsc.getActualType() == SQLConstructType.FROM) {
				analyseFrom((SQLFrom) rsc, sqlQueryContext);
			} else if (rsc.getActualType() == SQLConstructType.WHERE) {
				analyseWhere((SQLWhere) rsc, sqlQueryContext);
			} else if (rsc.getActualType() == SQLConstructType.ORDER_BY) {
				analyseOrderBy((SQLOrderBy) rsc, sqlQueryContext);
			} else if (rsc.getActualType() == SQLConstructType.GROUP_BY) {
				analyseGroupBy((SQLGroupBy) rsc, sqlQueryContext);
			} else if (rsc.getActualType() == SQLConstructType.HAVING) {
				analyseHaving((SQLHaving) rsc, sqlQueryContext);
			} else if (rsc.getGeneralType() == SQLConstructType.EXPRESSION) {
				analyseExpression(rsc, sqlQueryContext);
			}
		}

	}

	private static void analyseSelect(final SQLSelect select, final SQLQueryContext sqlQueryContext) {

		select.withSQLQueryContext(sqlQueryContext);

		analyseHelper(select.getFrom(), sqlQueryContext);
		analyseHelper(select.getWhere(), sqlQueryContext);
		analyseHelper(select.getGroupBy(), sqlQueryContext);
		analyseHelper(select.getOrderBy(), sqlQueryContext);
		analyseHelper(select.getHaving(), sqlQueryContext);

		analyseSubRSCList(select.getSubRSCList(), sqlQueryContext);

		if (select.getActualType() == SQLConstructType.SUBSELECT) {
			if (null != ((SQLSubSelect) select).getAlias()) {
				((SQLSubSelect) select).getAlias().withSQLQueryContext(sqlQueryContext);
			}
		}
	}

	private static void analyseUpdate(final SQLUpdate update, final SQLQueryContext sqlQueryContext) {

		update.withSQLQueryContext(sqlQueryContext);

		if (null != update.getTable()) {
			analyseHelper(update.getTable(), sqlQueryContext);
		}

		if (null != update.getWhere()) {
			analyseHelper(update.getWhere(), sqlQueryContext);
		}

		analyseSubRSCList(update.getSubRSCList(), sqlQueryContext);
	}

	private static void analyseDelete(final SQLDelete delete, final SQLQueryContext sqlQueryContext) {

		delete.withSQLQueryContext(sqlQueryContext);

		if (null != delete.getTable()) {
			analyseHelper(delete.getTable(), sqlQueryContext);
		}

		if (null != delete.getWhere()) {
			analyseHelper(delete.getWhere(), sqlQueryContext);
		}

	}

	private static void analyseInsert(final SQLInsert insert, final SQLQueryContext sqlQueryContext) {

		insert.withSQLQueryContext(sqlQueryContext);

		List<SQLColumn> columns = insert.getColumns();
		if (!columns.isEmpty()) {
			for (SQLColumn column : columns) {
				analyseHelper(column, sqlQueryContext);
			}
		}

		int numOfRows = insert.getNumberOfRows();
		for (int i = 0; i < numOfRows; i++) {
			List<SQLValue> values = insert.getValues(i);
			for (SQLValue value : values) {
				analyseHelper(value, sqlQueryContext);
			}
		}

		if (null != insert.getEmbeddedQuery()) {
			analyseHelper(insert.getEmbeddedQuery(), sqlQueryContext);
		}

		if (null != insert.getTable()) {
			analyseHelper(insert.getTable(), sqlQueryContext);
		}
	}

	private static void analyseHaving(final SQLHaving having, final SQLQueryContext sqlQueryContext) {

		having.withSQLQueryContext(sqlQueryContext);

		analyseSubRSCList(having.getSubRSCList(), sqlQueryContext);
		analyseHelper(having.getSearchConditions(), sqlQueryContext);
	}

	private static void analyseGroupBy(final SQLGroupBy groupBy, final SQLQueryContext sqlQueryContext) {

		groupBy.withSQLQueryContext(sqlQueryContext);

		analyseSubRSCList(groupBy.getSubRSCList(), sqlQueryContext);
	}

	private static void analyseOrderBy(final SQLOrderBy orderBy, final SQLQueryContext sqlQueryContext) {
		orderBy.withSQLQueryContext(sqlQueryContext);
		analyseSubRSCList(orderBy.getSubRSCList(), sqlQueryContext);
	}

	private static void analyseWhere(final SQLWhere where, final SQLQueryContext sqlQueryContext) {
		where.withSQLQueryContext(sqlQueryContext);
		analyseSubRSCList(where.getSubRSCList(), sqlQueryContext);
		analyseHelper(where.getSearchConditions(), sqlQueryContext);
	}

	private static void analyseFrom(final SQLFrom from, final SQLQueryContext sqlQueryContext) {

		from.withSQLQueryContext(sqlQueryContext);

		List<SQLConstruct> rscList = from.getSubRSCList();
		for (SQLConstruct rsc : rscList) {

			List<SQLJoin> joins = ((SQLTargetResource) rsc).getJoins();
			if (!joins.isEmpty()) {
				analyseJoins(joins, sqlQueryContext);
			}

			analyseHelper(rsc, sqlQueryContext);
		}
	}

	private static void analyseJoins(final List<SQLJoin> joins, final SQLQueryContext sqlQueryContext) {
		for (SQLJoin join : joins) {
			join.withSQLQueryContext(sqlQueryContext);
			analyseHelper(join.getTargetSource(), sqlQueryContext);
			analyseHelper(join.getConditions(), sqlQueryContext);
		}
	}

	private static void analyseSubRSCList(final List<SQLConstruct> subRSCList, final SQLQueryContext sqlQueryContext) {
		if (null != subRSCList && !subRSCList.isEmpty()) {
			for (SQLConstruct subRSC : subRSCList) {
				analyseHelper(subRSC, sqlQueryContext);
			}
		}
	}

	private static void analyseValue(final SQLValue value, final SQLQueryContext sqlQueryContext) {
		value.withSQLQueryContext(sqlQueryContext);
	}

	private static void analyseConditionTree(final SQLConstruct root, final SQLQueryContext sqlQueryContext) {
		if (null != root) {

			if (root.getActualType() == SQLConstructType.SEARCH_COND_AND) {
				analyseHelper(root, sqlQueryContext);
			} else if (root.getActualType() == SQLConstructType.SEARCH_COND_OR) {
				analyseHelper(root, sqlQueryContext);
			} else {
				analyseHelper(root, sqlQueryContext);
			}
		}
	}

	private static void analyseLogicalOr(final SQLLogicalOR or, final SQLQueryContext sqlQueryContext) {
		or.withSQLQueryContext(sqlQueryContext);
		analyseConditionTree(or.getLeftOperand(), sqlQueryContext);
		analyseConditionTree(or.getRightOperand(), sqlQueryContext);
	}

	private static void analyseLogicalAnd(final SQLLogicalAND and, final SQLQueryContext sqlQueryContext) {
		and.withSQLQueryContext(sqlQueryContext);
		analyseConditionTree(and.getLeftOperand(), sqlQueryContext);
		analyseConditionTree(and.getRightOperand(), sqlQueryContext);
	}

	private static void analyseFunction(final SQLFunction rsc, final SQLQueryContext sqlQueryContext) {

		rsc.withSQLQueryContext(sqlQueryContext);

		if (rsc.getActualType() == SQLConstructType.COMPARISON_FUNCTION) {
			SQLComparisonFunction func = (SQLComparisonFunction) rsc;
			analyseHelper(func.getOperand1(), sqlQueryContext);
			analyseHelper(func.getOperand2(), sqlQueryContext);
		} else if (rsc.getActualType() == SQLConstructType.SUM_FUNCTION) {
			SQLSumFunction func = (SQLSumFunction) rsc;
			analyseHelper(func.getOperand(), sqlQueryContext);
		} else if (rsc.getActualType() == SQLConstructType.BETWEEN_FUNCTION) {
			SQLBetweenFunction func = (SQLBetweenFunction) rsc;
			analyseHelper(func.getPredicand(), sqlQueryContext);
			analyseHelper(func.getPredicate1(), sqlQueryContext);
			analyseHelper(func.getPredicate2(), sqlQueryContext);
		} else if (rsc.getActualType() == SQLConstructType.IN_FUNCTION) {
			SQLInFunction func = (SQLInFunction) rsc;
			analyseHelper(func.getOperand1(), sqlQueryContext);
			analyseHelper(func.getOperand2(), sqlQueryContext);
		} else if (rsc.getActualType() == SQLConstructType.EXISTS_FUNCTION) {
			SQLExistsFunction func = (SQLExistsFunction) rsc;
			analyseHelper(func.getSubquery(), sqlQueryContext);
		} else if (rsc.getActualType() == SQLConstructType.LIKE_FUNCTION) {
			SQLLikeFunction func = (SQLLikeFunction) rsc;
			analyseHelper(func.getOperand1(), sqlQueryContext);
			analyseHelper(func.getOperand2(), sqlQueryContext);
		} else if (rsc.getActualType() == SQLConstructType.COUNT_FUNCTION) {
			SQLCountFunction func = (SQLCountFunction) rsc;
			analyseHelper(func.getOperand(), sqlQueryContext);
		} else if (rsc.getActualType() == SQLConstructType.MAX_FUNCTION) {
			SQLMaxFunction func = (SQLMaxFunction) rsc;
			analyseHelper(func.getOperand(), sqlQueryContext);
		} else if (rsc.getActualType() == SQLConstructType.MIN_FUNCTION) {
			SQLMinFunction func = (SQLMinFunction) rsc;
			analyseHelper(func.getOperand(), sqlQueryContext);
		} else if (rsc.getActualType() == SQLConstructType.AVG_FUNCTION) {
			SQLAvgFunction func = (SQLAvgFunction) rsc;
			analyseHelper(func.getOperand(), sqlQueryContext);
		} else if (rsc.getActualType() == SQLConstructType.NULL_FUNCTION) {
			SQLNullFunction func = (SQLNullFunction) rsc;
			analyseHelper(func.getOperand(), sqlQueryContext);
		} else if (rsc.getActualType() == SQLConstructType.COMPARISON_FUNCTION) {
			SQLAssignmentFunction func = (SQLAssignmentFunction) rsc;
			analyseHelper(func.getOperand1(), sqlQueryContext);
			analyseHelper(func.getOperand2(), sqlQueryContext);
		}
	}

	private static void analyseExpression(final SQLConstruct rsc, final SQLQueryContext sqlQueryContext) {
		if (rsc.getActualType() == SQLConstructType.NUMERIC_VALUE_EXPRESSION) {
			rsc.withSQLQueryContext(sqlQueryContext);
			SQLNumericValueExpression expr = (SQLNumericValueExpression) rsc;

			List<SQLConstruct> terms = expr.getTerms();
			for (SQLConstruct term : terms) {
				analyseHelper(term, sqlQueryContext);
			}

			List<SQLOperator> signs = expr.getSigns();
			for (SQLOperator sign : signs) {
				analyseHelper(sign, sqlQueryContext);
			}

		}
	}

	private static void analyseColumn(final SQLColumn column, final SQLQueryContext sqlQueryContext) {
		column.withSQLQueryContext(sqlQueryContext);
	}

	private static void analyseTable(final SQLTable table, final SQLQueryContext sqlQueryContext) {
		table.withSQLQueryContext(sqlQueryContext);
	}

	public static SQLConstruct getRSCSearchConditions(final SQLComparisonFunction comparisonFunction) {
		SQLConstruct rsc = comparisonFunction;
		while (rsc != null && rsc.getActualType() != SQLConstructType.SEARCH_COND) {
			rsc = rsc.getReferencingRSC();
		}
		return rsc;
	}
}
