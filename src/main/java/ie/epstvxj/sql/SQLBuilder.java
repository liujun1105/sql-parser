package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.function.BetweenType;
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
import ie.epstvxj.sql.function.SQLNotFunction;
import ie.epstvxj.sql.function.SQLNullFunction;
import ie.epstvxj.sql.function.SQLSumFunction;
import ie.epstvxj.sql.function.SQLUserDefinedFunction;
import ie.epstvxj.sql.special.MultiChoiceMap;
import ie.epstvxj.sql.tokens.SQLIgnoreToken;
import ie.epstvxj.sql.tokens.SQLSign;
import ie.epstvxj.sql.value.SQLAsterisk;
import ie.epstvxj.sql.value.SQLBooleanValue;
import ie.epstvxj.sql.value.SQLDateValue;
import ie.epstvxj.sql.value.SQLDynamicParameter;
import ie.epstvxj.sql.value.SQLFloatValue;
import ie.epstvxj.sql.value.SQLIntValue;
import ie.epstvxj.sql.value.SQLListValue;
import ie.epstvxj.sql.value.SQLNullValue;
import ie.epstvxj.sql.value.SQLOperator;
import ie.epstvxj.sql.value.SQLStringValue;
import ie.epstvxj.sql.value.SQLValue;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Jun Liu (jun.liu@aol.com)
 *
 */
public final class SQLBuilder {

	private static Logger			LOG				= Logger.getLogger(SQLBuilder.class);

	private final static SQLBuilder	rsc				= new SQLBuilder();
	private final static Function	functionBuilder	= rsc.new Function();
	private final static Value		valueBuilder	= rsc.new Value();
	private final static Tokens		tokenBuilder	= rsc.new Tokens();
	private final static Printer	printer			= rsc.new Printer();
	private final static Special	specialBuilder	= rsc.new Special();

	private SQLBuilder() {

	}

	public static SQLBuilder build() {
		return rsc;
	}

	public static Special special() {
		return specialBuilder;
	}

	public static Value init() {
		return valueBuilder;
	}

	public static Function function() {
		return functionBuilder;
	}

	public static Tokens tokens() {
		return tokenBuilder;
	}

	public static Printer printer() {
		return printer;
	}

	public SQLLogicalAND and() {
		SQLLogicalAND and = new SQLLogicalAND().withId(SQLConstructIdentifierManager.getIdentifier());
		return and;
	}

	public SQLLogicalOR or() {
		SQLLogicalOR or = new SQLLogicalOR().withId(SQLConstructIdentifierManager.getIdentifier());
		return or;
	}

	public SQLInsert insert() {
		SQLInsert insert = new SQLInsert().withId(SQLConstructIdentifierManager.getIdentifier());
		return insert;
	}

	public SQLDelete delete() {
		SQLDelete delete = new SQLDelete().withId(SQLConstructIdentifierManager.getIdentifier());
		return delete;
	}

	public SQLUpdate update() {
		SQLUpdate update = new SQLUpdate().withId(SQLConstructIdentifierManager.getIdentifier());
		return update;
	}

	public SQLSelect select() {
		SQLSelect select = new SQLSelect().withId(SQLConstructIdentifierManager.getIdentifier());
		return select;
	}

	public SQLFrom from() {
		SQLFrom from = new SQLFrom().withId(SQLConstructIdentifierManager.getIdentifier());
		return from;
	}

	public SQLWhere where() {
		SQLWhere where = new SQLWhere().withId(SQLConstructIdentifierManager.getIdentifier());
		return where;
	}

	public SQLHaving having() {
		SQLHaving having = new SQLHaving().withId(SQLConstructIdentifierManager.getIdentifier());
		return having;
	}

	public SQLOrderBy orderBy() {
		SQLOrderBy orderBy = new SQLOrderBy().withId(SQLConstructIdentifierManager.getIdentifier());
		return orderBy;
	}

	public SQLOrderBy orderBy(final SQLConstruct... orderByItems) {
		SQLOrderBy orderBy = new SQLOrderBy(orderByItems).withId(SQLConstructIdentifierManager.getIdentifier());
		return orderBy;
	}

	public SQLGroupBy groupBy() {
		SQLGroupBy groupBy = new SQLGroupBy().withId(SQLConstructIdentifierManager.getIdentifier());
		return groupBy;
	}

	public SQLGroupBy groupBy(final SQLConstruct... groupByItems) {
		SQLGroupBy groupBy = new SQLGroupBy(groupByItems).withId(SQLConstructIdentifierManager.getIdentifier());
		return groupBy;
	}

	public SQLSubSelect subSelect() {
		SQLSubSelect subSelect = new SQLSubSelect().withId(SQLConstructIdentifierManager.getIdentifier());
		return subSelect;
	}

	public SQLColumn column() {
		SQLColumn column = new SQLColumn().withId(SQLConstructIdentifierManager.getIdentifier());
		return column;
	}

	public SQLColumn column(final String columnName) {
		SQLColumn column = new SQLColumn(columnName).withId(SQLConstructIdentifierManager.getIdentifier());
		return column;
	}

	public SQLTable table() {
		SQLTable table = new SQLTable().withId(SQLConstructIdentifierManager.getIdentifier());
		return table;
	}

	public SQLSearchConditions conditions() {
		SQLSearchConditions searchConditions = new SQLSearchConditions().withId(SQLConstructIdentifierManager
				.getIdentifier());
		return searchConditions;
	}

	public SQLJoin join() {
		SQLJoin join = new SQLJoin(SQLJoinType.JOIN).withId(SQLConstructIdentifierManager.getIdentifier());
		return join;
	}

	public SQLJoin innerJoin() {
		SQLJoin join = new SQLJoin(SQLJoinType.INNER_JOIN).withId(SQLConstructIdentifierManager.getIdentifier());
		return join;
	}

	public SQLJoin leftJoin() {
		SQLJoin join = new SQLJoin(SQLJoinType.LEFT_JOIN).withId(SQLConstructIdentifierManager.getIdentifier());
		return join;
	}

	public SQLJoin rightJoin() {
		SQLJoin join = new SQLJoin(SQLJoinType.RIGHT_JOIN).withId(SQLConstructIdentifierManager.getIdentifier());
		return join;
	}

	public SQLJoin fullJoin() {
		SQLJoin join = new SQLJoin(SQLJoinType.FULL_JOIN).withId(SQLConstructIdentifierManager.getIdentifier());
		return join;
	}

	public SQLJoin unionJoin() {
		SQLJoin join = new SQLJoin(SQLJoinType.UNION_JOIN).withId(SQLConstructIdentifierManager.getIdentifier());
		return join;
	}

	public SQLJoin crossJoin() {
		SQLJoin join = new SQLJoin(SQLJoinType.CROSS_JOIN).withId(SQLConstructIdentifierManager.getIdentifier());
		return join;
	}

	public SQLJoin naturalJoin() {
		SQLJoin join = new SQLJoin(SQLJoinType.NATURAL_JOIN).withId(SQLConstructIdentifierManager.getIdentifier());
		return join;
	}

	public SQLJoin naturalLeftJoin() {
		SQLJoin join = new SQLJoin(SQLJoinType.NATURAL_LEFT_JOIN).withId(SQLConstructIdentifierManager
				.getIdentifier());
		return join;
	}

	public SQLJoin naturalRightJoin() {
		SQLJoin join = new SQLJoin(SQLJoinType.NATURAL_RIGHT_JOIN).withId(SQLConstructIdentifierManager
				.getIdentifier());
		return join;
	}

	public SQLJoin naturalFullJoin() {
		SQLJoin join = new SQLJoin(SQLJoinType.NATURAL_FULL_JOIN).withId(SQLConstructIdentifierManager
				.getIdentifier());
		return join;
	}

	public SQLJoin naturalInnerJoin() {
		SQLJoin join = new SQLJoin(SQLJoinType.NATURAL_INNER_JOIN).withId(SQLConstructIdentifierManager
				.getIdentifier());
		return join;
	}

	public SQLSelectedItem selectedItem(final SQLConstruct rsc) {
		SQLSelectedItem selectedItem = new SQLSelectedItem(rsc)
				.withId(SQLConstructIdentifierManager.getIdentifier());
		return selectedItem;
	}

	public SQLCorrelationName correlationName(final String correlationName) {
		SQLCorrelationName rscCorrelationname = new SQLCorrelationName(correlationName)
				.withId(SQLConstructIdentifierManager.getIdentifier());
		return rscCorrelationname;
	}

	public SQLNumericValueExpression numericValueExpr() {
		SQLNumericValueExpression numericValueExpression = new SQLNumericValueExpression()
				.withId(SQLConstructIdentifierManager.getIdentifier());
		return numericValueExpression;
	}

	public SQLUnion union() {
		SQLUnion union = new SQLUnion().withId(SQLConstructIdentifierManager.getIdentifier());
		return union;
	}

	public class Function {

		private Function() {

		}

		public SQLInFunction in() {
			SQLInFunction func = new SQLInFunction().withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLInFunction in(final SQLConstruct operand1, final SQLConstruct operand2) {
			SQLInFunction func = new SQLInFunction(operand1, operand2).withId(SQLConstructIdentifierManager
					.getIdentifier());
			return func;
		}

		public SQLInFunction notIn(final SQLConstruct operand1, final SQLConstruct operand2) {
			SQLInFunction func = new SQLInFunction().notIn(operand1, operand2).withId(
					SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLComparisonFunction comparison() {
			SQLComparisonFunction func = new SQLComparisonFunction().withId(SQLConstructIdentifierManager
					.getIdentifier());
			return func;
		}

		public SQLComparisonFunction comparison(final SQLConstruct operand1, final SQLConstruct operand2,
				final SQLOperator operator) {
			SQLComparisonFunction func = new SQLComparisonFunction(operand1, operator, operand2)
					.withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLComparisonFunction equality(final SQLConstruct operand1, final SQLConstruct operand2) {
			SQLComparisonFunction func = new SQLComparisonFunction(operand1, SQLBuilder.init().operator(
					SQLGrammarTokens.EQUAL), operand2).withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLBetweenFunction between() {
			SQLBetweenFunction func = new SQLBetweenFunction().withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLBetweenFunction between(final SQLConstruct predicand, final SQLConstruct predicate1,
				final SQLConstruct predicate2) {
			SQLBetweenFunction func = new SQLBetweenFunction(predicand, predicate1, predicate2)
					.withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLBetweenFunction between(final SQLConstruct predicand, final SQLConstruct predicate1,
				final SQLConstruct predicate2, final BetweenType betweenType) {
			SQLBetweenFunction func = new SQLBetweenFunction(predicand, predicate1, predicate2, betweenType)
					.withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLExistsFunction exists() {
			SQLExistsFunction func = new SQLExistsFunction().withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLExistsFunction exists(final SQLConstruct subquery) {
			SQLExistsFunction func = new SQLExistsFunction(subquery).withId(SQLConstructIdentifierManager
					.getIdentifier());
			return func;
		}

		public SQLNotFunction not(final SQLConstruct operand) {
			SQLNotFunction func = new SQLNotFunction(operand).withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLSumFunction sum() {
			SQLSumFunction func = new SQLSumFunction().withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLSumFunction sum(final SQLConstruct operand) {
			SQLSumFunction func = new SQLSumFunction(operand).withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLAvgFunction avg() {
			SQLAvgFunction func = new SQLAvgFunction().withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLAvgFunction avg(final SQLConstruct operand) {
			SQLAvgFunction func = new SQLAvgFunction(operand).withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLMaxFunction max() {
			SQLMaxFunction func = new SQLMaxFunction().withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLMaxFunction max(final SQLConstruct operand) {
			SQLMaxFunction func = new SQLMaxFunction(operand).withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLMinFunction min() {
			SQLMinFunction func = new SQLMinFunction().withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLMinFunction min(final SQLConstruct operand) {
			SQLMinFunction func = new SQLMinFunction(operand).withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLLikeFunction like() {
			SQLLikeFunction func = new SQLLikeFunction().withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLLikeFunction like(final SQLConstruct operand1, final SQLStringValue operand2) {
			SQLLikeFunction func = new SQLLikeFunction(operand1, operand2).withId(SQLConstructIdentifierManager
					.getIdentifier());
			return func;
		}

		public SQLCountFunction count() {
			SQLCountFunction func = new SQLCountFunction().withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLCountFunction count(final SQLConstruct operand) {
			SQLCountFunction func = new SQLCountFunction().withOperand(operand).withId(
					SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLNullFunction isNull() {
			SQLNullFunction func = new SQLNullFunction().withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLNullFunction notNull() {
			SQLNullFunction func = new SQLNullFunction().notNull().withId(
					SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLUserDefinedFunction udf(final String functionName, final SQLConstruct operand) {
			SQLUserDefinedFunction func = new SQLUserDefinedFunction(functionName, operand)
					.withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

		public SQLAssignmentFunction assignment(final SQLConstruct operand1, final SQLConstruct operand2) {
			SQLAssignmentFunction func = new SQLAssignmentFunction(operand1, operand2)
					.withId(SQLConstructIdentifierManager.getIdentifier());
			return func;
		}

	}

	public class Value {
		private Value() {

		}

		public SQLIntValue intValue(final int value) {
			SQLIntValue rscValue = new SQLIntValue(value).withId(SQLConstructIdentifierManager.getIdentifier());
			return rscValue;
		}

		public SQLFloatValue floatValue(final float value) {
			SQLFloatValue rscValue = new SQLFloatValue(value).withId(SQLConstructIdentifierManager.getIdentifier());
			return rscValue;
		}

		public SQLStringValue stringValue(final String value) {
			SQLStringValue rscValue = new SQLStringValue(value)
					.withId(SQLConstructIdentifierManager.getIdentifier());
			return rscValue;
		}

		public SQLOperator operator(final String operator) {
			SQLOperator rscOperator = new SQLOperator(operator)
					.withId(SQLConstructIdentifierManager.getIdentifier());
			return rscOperator;
		}

		public SQLDateValue dateValue(final Date value) {
			SQLDateValue rscValue = new SQLDateValue(value).withId(SQLConstructIdentifierManager.getIdentifier());
			return rscValue;
		}

		public SQLDateValue dateValue(final long valueInMillionSeconds) {
			SQLDateValue rscValue = new SQLDateValue(valueInMillionSeconds).withId(SQLConstructIdentifierManager
					.getIdentifier());
			return rscValue;
		}

		public SQLDateValue dateValue(final String dateInString, final String pattern) {
			SQLDateValue rscValue = new SQLDateValue().withDateValue(dateInString, pattern).withId(
					SQLConstructIdentifierManager.getIdentifier());
			return rscValue;
		}

		public SQLBooleanValue truthValue() {
			SQLBooleanValue rscValue = new SQLBooleanValue().withValue(true).withId(
					SQLConstructIdentifierManager.getIdentifier());
			return rscValue;
		}

		public SQLBooleanValue falseValue() {
			SQLBooleanValue rscValue = new SQLBooleanValue().withValue(false).withId(
					SQLConstructIdentifierManager.getIdentifier());
			return rscValue;
		}

		public SQLListValue listValue() {
			SQLListValue rscValue = new SQLListValue().withId(SQLConstructIdentifierManager.getIdentifier());
			return rscValue;
		}

		public SQLListValue listValue(final SQLConstruct... values) {
			SQLListValue rscValue = new SQLListValue(values).withId(SQLConstructIdentifierManager.getIdentifier());
			return rscValue;
		}

		public SQLAsterisk asterisk() {
			SQLAsterisk rscAsterisk = new SQLAsterisk().withId(SQLConstructIdentifierManager.getIdentifier());
			return rscAsterisk;
		}

		public SQLDynamicParameter dynamicParameter() {
			SQLDynamicParameter dynamicParamter = new SQLDynamicParameter().withId(SQLConstructIdentifierManager
					.getIdentifier());
			return dynamicParamter;
		}

		public SQLNullValue nullValue() {
			SQLNullValue rscNullValue = new SQLNullValue().withId(SQLConstructIdentifierManager.getIdentifier());
			return rscNullValue;
		}
	}

	public class Tokens {
		private Tokens() {

		}

		public SQLIgnoreToken ignore() {
			SQLIgnoreToken ignoreToken = new SQLIgnoreToken().withId(SQLConstructIdentifierManager.getIdentifier());
			return ignoreToken;
		}

		public SQLSign sign(final String sign) {
			SQLSign rscSign = new SQLSign(sign).withId(SQLConstructIdentifierManager.getIdentifier());
			return rscSign;
		}

	}

	public class Special {
		private Special() {

		}

		public MultiChoiceMap multiChoiceMapping() {
			return new MultiChoiceMap();
		}

	}

	public final class Printer {

		public void print(final SQLConstruct rsc) {
			analyseHelper(rsc);
		}

		private void analyseHelper(final SQLConstruct rsc) {

			if (null != rsc) {

				if (rsc.getActualType() == SQLConstructType.SUBSELECT || rsc.getActualType() == SQLConstructType.SELECT) {
					analyseSelect((SQLSelect) rsc);
				} else if (rsc.getActualType() == SQLConstructType.INSERT) {
					analyseInsert((SQLInsert) rsc);
				} else if (rsc.getActualType() == SQLConstructType.DELETE) {
					analyseDelete((SQLDelete) rsc);
				} else if (rsc.getActualType() == SQLConstructType.UPDATE) {
					analyseUpdate((SQLUpdate) rsc);
				} else if (rsc.getActualType() == SQLConstructType.FUNCTION) {
					analyseFunction((SQLFunction) rsc);
				} else if (rsc.getActualType() == SQLConstructType.COLUMN) {
					analyseColumn((SQLColumn) rsc);
				} else if (rsc.getActualType() == SQLConstructType.TABLE) {
					analyseTable((SQLTable) rsc);
				} else if (rsc.getGeneralType() == SQLConstructType.SEARCH_COND) {
					analyseConditionTree(((SQLSearchConditions) rsc).getRoot());
				} else if (rsc.getGeneralType() == SQLConstructType.VALUE) {
					analyseValue((SQLValue) rsc);
				} else if (rsc.getGeneralType() == SQLConstructType.FUNCTION) {
					analyseFunction((SQLFunction) rsc);
				} else if (rsc.getActualType() == SQLConstructType.FROM) {
					analyseFrom((SQLFrom) rsc);
				} else if (rsc.getActualType() == SQLConstructType.WHERE) {
					analyseWhere((SQLWhere) rsc);
				} else if (rsc.getActualType() == SQLConstructType.ORDER_BY) {
					analyseOrderBy((SQLOrderBy) rsc);
				} else if (rsc.getActualType() == SQLConstructType.GROUP_BY) {
					analyseGroupBy((SQLGroupBy) rsc);
				} else if (rsc.getActualType() == SQLConstructType.HAVING) {
					analyseHaving((SQLHaving) rsc);
				} else if (rsc.getGeneralType() == SQLConstructType.EXPRESSION) {
					analyseExpression(rsc);
				} else {
					// constructs that we do not handle at moment.
					LOG.warn("Not Implemented Yet (analyseHelper)--> " + rsc.getActualType());
				}
			}

		}

		private void analyseSelect(final SQLSelect select) {

			System.out.println(select + " [" + select.getSQLQueryContext() + "]");
			System.out.println(select.getRepository());
			System.out.println("--------------------------\n");

			analyseHelper(select.getFrom());
			analyseHelper(select.getWhere());
			analyseHelper(select.getGroupBy());
			analyseHelper(select.getOrderBy());
			analyseHelper(select.getHaving());

			analyseSubRSCList(select.getSubRSCList());

			if (select.getActualType() == SQLConstructType.SUBSELECT) {
				if (null != ((SQLSubSelect) select).getAlias()) {
					System.out.println(((SQLSubSelect) select).getAlias() + " ["
							+ ((SQLSubSelect) select).getAlias().getSQLQueryContext() + "]");
					System.out.println(((SQLSubSelect) select).getRepository());
					System.out.println("--------------------------\n");
				}
			}
		}

		private void analyseUpdate(final SQLUpdate update) {

			System.out.println(update + " [" + update.getSQLQueryContext() + "]");
			System.out.println(update.getRepository());
			System.out.println("--------------------------\n");

			if (null != update.getTable()) {
				analyseHelper(update.getTable());
			}

			if (null != update.getWhere()) {
				analyseHelper(update.getWhere());
			}

			analyseSubRSCList(update.getSubRSCList());
		}

		private void analyseDelete(final SQLDelete delete) {
			System.out.println(delete + " [" + delete.getSQLQueryContext() + "]");
			System.out.println(delete.getRepository());
			System.out.println("--------------------------\n");

			if (null != delete.getTable()) {
				analyseHelper(delete.getTable());
			}

			if (null != delete.getWhere()) {
				analyseHelper(delete.getWhere());
			}

		}

		private void analyseInsert(final SQLInsert insert) {

			System.out.println(insert + " [" + insert.getSQLQueryContext() + "]");
			System.out.println(insert.getRepository());
			System.out.println("--------------------------\n");

			List<SQLColumn> columns = insert.getColumns();
			if (!columns.isEmpty()) {
				for (SQLColumn column : columns) {
					analyseHelper(column);
				}
			}

			int numOfRows = insert.getNumberOfRows();
			for (int i = 0; i < numOfRows; i++) {
				List<SQLValue> values = insert.getValues(i);
				for (SQLValue value : values) {
					analyseHelper(value);
				}
			}

			if (null != insert.getEmbeddedQuery()) {
				analyseHelper(insert.getEmbeddedQuery());
			}

			if (null != insert.getTable()) {
				analyseHelper(insert.getTable());
			}
		}

		private void analyseHaving(final SQLHaving having) {
			System.out.println(having + " [" + having.getSQLQueryContext() + "]");
			System.out.println(having.getRepository());
			System.out.println("--------------------------\n");
			analyseSubRSCList(having.getSubRSCList());
			analyseHelper(having.getSearchConditions());
		}

		private void analyseGroupBy(final SQLGroupBy groupBy) {
			System.out.println(groupBy + " [" + groupBy.getSQLQueryContext() + "]");
			System.out.println(groupBy.getRepository());
			System.out.println("--------------------------\n");
			analyseSubRSCList(groupBy.getSubRSCList());
		}

		private void analyseOrderBy(final SQLOrderBy orderBy) {
			System.out.println(orderBy + " [" + orderBy.getSQLQueryContext() + "]");
			System.out.println(orderBy.getRepository());
			System.out.println("--------------------------\n");
			analyseSubRSCList(orderBy.getSubRSCList());
		}

		private void analyseWhere(final SQLWhere where) {
			System.out.println(where + " [" + where.getSQLQueryContext() + "]");
			System.out.println(where.getRepository());
			System.out.println("--------------------------\n");
			analyseSubRSCList(where.getSubRSCList());
			analyseHelper(where.getSearchConditions());
		}

		private void analyseFrom(final SQLFrom from) {

			System.out.println(from + " [" + from.getSQLQueryContext() + "]");
			System.out.println(from.getRepository());
			System.out.println("--------------------------\n");

			List<SQLConstruct> rscList = from.getSubRSCList();
			for (SQLConstruct rsc : rscList) {

				List<SQLJoin> joins = ((SQLTargetResource) rsc).getJoins();
				if (!joins.isEmpty()) {
					analyseJoins(joins);
				}

				analyseHelper(rsc);
			}
		}

		private void analyseJoins(final List<SQLJoin> joins) {
			for (SQLJoin join : joins) {
				System.out.println(join + " [" + join.getSQLQueryContext() + "]");
				System.out.println(join.getRepository());
				System.out.println("--------------------------\n");
				analyseHelper(join.getTargetSource());
				analyseHelper(join.getConditions());
			}
		}

		private void analyseSubRSCList(final List<SQLConstruct> subRSCList) {
			if (null != subRSCList && !subRSCList.isEmpty()) {
				for (SQLConstruct subRSC : subRSCList) {
					analyseHelper(subRSC);
				}
			}
		}

		private void analyseValue(final SQLValue value) {
			System.out.println(value + " [" + value.getSQLQueryContext() + "]");
			System.out.println(value.getRepository());
			System.out.println("--------------------------\n");
		}

		private void analyseConditionTree(final SQLConstruct root) {
			if (null != root) {

				if (root.getActualType() == SQLConstructType.SEARCH_COND_AND) {

					SQLLogicalAND and = (SQLLogicalAND) root;

					System.out.println(and + " [" + and.getSQLQueryContext() + "]");
					System.out.println(and.getRepository());
					System.out.println("--------------------------\n");

					analyseConditionTree(and.getLeftOperand());
					analyseConditionTree(and.getRightOperand());

				} else if (root.getActualType() == SQLConstructType.SEARCH_COND_OR) {

					SQLLogicalOR or = (SQLLogicalOR) root;

					System.out.println(or + " [" + or.getSQLQueryContext() + "]");
					System.out.println(or.getRepository());
					System.out.println("--------------------------\n");

					analyseConditionTree(or.getLeftOperand());
					analyseConditionTree(or.getRightOperand());

				} else {
					analyseHelper(root);
				}
			}
		}

		private void analyseFunction(final SQLFunction rsc) {

			System.out.println(rsc + " [" + rsc.getSQLQueryContext() + "]");
			System.out.println(rsc.getRepository());
			System.out.println("--------------------------\n");

			if (rsc.getActualType() == SQLConstructType.COMPARISON_FUNCTION) {
				SQLComparisonFunction func = (SQLComparisonFunction) rsc;
				analyseHelper(func.getOperand1());
				analyseHelper(func.getOperand2());
			} else if (rsc.getActualType() == SQLConstructType.SUM_FUNCTION) {
				SQLSumFunction func = (SQLSumFunction) rsc;
				analyseHelper(func.getOperand());
			} else if (rsc.getActualType() == SQLConstructType.BETWEEN_FUNCTION) {
				SQLBetweenFunction func = (SQLBetweenFunction) rsc;
				analyseHelper(func.getPredicand());
				analyseHelper(func.getPredicate1());
				analyseHelper(func.getPredicate2());
			} else if (rsc.getActualType() == SQLConstructType.IN_FUNCTION) {
				SQLInFunction func = (SQLInFunction) rsc;
				analyseHelper(func.getOperand1());
				analyseHelper(func.getOperand2());
			} else if (rsc.getActualType() == SQLConstructType.EXISTS_FUNCTION) {
				SQLExistsFunction func = (SQLExistsFunction) rsc;
				analyseHelper(func.getSubquery());
			} else if (rsc.getActualType() == SQLConstructType.LIKE_FUNCTION) {
				SQLLikeFunction func = (SQLLikeFunction) rsc;
				analyseHelper(func.getOperand1());
				analyseHelper(func.getOperand2());
			} else if (rsc.getActualType() == SQLConstructType.COUNT_FUNCTION) {
				SQLCountFunction func = (SQLCountFunction) rsc;
				analyseHelper(func.getOperand());
			} else if (rsc.getActualType() == SQLConstructType.MAX_FUNCTION) {
				SQLMaxFunction func = (SQLMaxFunction) rsc;
				analyseHelper(func.getOperand());
			} else if (rsc.getActualType() == SQLConstructType.MIN_FUNCTION) {
				SQLMinFunction func = (SQLMinFunction) rsc;
				analyseHelper(func.getOperand());
			} else if (rsc.getActualType() == SQLConstructType.AVG_FUNCTION) {
				SQLAvgFunction func = (SQLAvgFunction) rsc;
				analyseHelper(func.getOperand());
			} else if (rsc.getActualType() == SQLConstructType.NULL_FUNCTION) {
				SQLNullFunction func = (SQLNullFunction) rsc;
				analyseHelper(func.getOperand());
			}
		}

		private void analyseExpression(final SQLConstruct rsc) {
			if (rsc.getActualType() == SQLConstructType.NUMERIC_VALUE_EXPRESSION) {
				System.out.println(rsc + " [" + rsc.getSQLQueryContext() + "]");
				System.out.println(rsc.getRepository());
				System.out.println("--------------------------\n");
				SQLNumericValueExpression expr = (SQLNumericValueExpression) rsc;

				List<SQLConstruct> terms = expr.getTerms();
				for (SQLConstruct term : terms) {
					analyseHelper(term);
				}

				List<SQLOperator> signs = expr.getSigns();
				for (SQLOperator sign : signs) {
					analyseHelper(sign);
				}

			}
		}

		private void analyseColumn(final SQLColumn column) {
			System.out.println(column + " [" + column.getSQLQueryContext() + "]");
			System.out.println(column.getRepository());
			System.out.println("--------------------------\n");
		}

		private void analyseTable(final SQLTable table) {
			System.out.println(table + " [" + table.getSQLQueryContext() + "]");
			System.out.println(table.getRepository());
			System.out.println("--------------------------\n");
		}
	}
}
