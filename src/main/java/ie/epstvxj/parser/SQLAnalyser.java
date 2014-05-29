package ie.epstvxj.parser;

import ie.epstvxj.parser.SQLGrammarParser.Boolean_termContext;
import ie.epstvxj.parser.SQLGrammarParser.Set_function_typeContext;
import ie.epstvxj.parser.SQLGrammarParser.SignContext;
import ie.epstvxj.sql.SQLBuilder;
import ie.epstvxj.sql.SQLColumn;
import ie.epstvxj.sql.SQLConstruct;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLCorrelationName;
import ie.epstvxj.sql.SQLDelete;
import ie.epstvxj.sql.SQLFrom;
import ie.epstvxj.sql.SQLGroupBy;
import ie.epstvxj.sql.SQLHaving;
import ie.epstvxj.sql.SQLInsert;
import ie.epstvxj.sql.SQLJoin;
import ie.epstvxj.sql.SQLJoinType;
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
import ie.epstvxj.sql.function.SQLNotFunction;
import ie.epstvxj.sql.function.SQLNullFunction;
import ie.epstvxj.sql.function.SQLSumFunction;
import ie.epstvxj.sql.tokens.SQLSign;
import ie.epstvxj.sql.value.SQLFloatValue;
import ie.epstvxj.sql.value.SQLIntValue;
import ie.epstvxj.sql.value.SQLListValue;
import ie.epstvxj.sql.value.SQLOperator;
import ie.epstvxj.sql.value.SQLStringValue;
import ie.epstvxj.sql.value.SQLValue;
import ie.epstvxj.utility.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.log4j.Logger;

public class SQLAnalyser extends SQLGrammarBaseListener {

	private static Logger					LOG								= Logger.getLogger(SQLAnalyser.class);

	private final Stack<SQLConstruct>		sqlConstructStack				= new Stack<SQLConstruct>();
	private SQLConstruct					sql;

	private boolean							ignoreIdentifierChain			= false;
	private boolean							ignoreColumnName				= false;
	private boolean							withinParenthesis				= false;
	private boolean							withinGeneralSetFunction		= false;
	private boolean							withInFromSubquery				= false;

	private int								ignoreQuerySpecificationCount	= 0;

	private boolean							withinInsertStatement;
	private boolean							withinUpdateStatement;
	private boolean							withinDeleteStatement;

	private final Stack<SQLQueryContext>	queryContextStack				= new Stack<SQLQueryContext>();

	/**
	 * Used to reset all variables,
	 * as we are reusing SQLSQLAnalyser instance
	 */
	public void preProcess() {
		ignoreIdentifierChain = false;
		ignoreColumnName = false;
		withinParenthesis = false;
		withinGeneralSetFunction = false;
		withInFromSubquery = false;

		ignoreQuerySpecificationCount = 0;

		withinInsertStatement = false;
		withinUpdateStatement = false;
		withinDeleteStatement = false;
		sqlConstructStack.clear();
		queryContextStack.clear();
		sql = null;
	}

	public SQLConstruct getSQLConstruct() {

		return sql;
	}

	@Override
	public void enterQuery_specification(@NotNull final SQLGrammarParser.Query_specificationContext ctx) {
		if (withInFromSubquery) {
			sqlConstructStack.push(SQLBuilder.tokens().ignore().withSQLQueryContext(queryContextStack.peek()));
		}
		if (ignoreQuerySpecificationCount == 0) {
			if (withinInsertStatement || withinUpdateStatement || withinDeleteStatement) {
				queryContextStack.push(SQLQueryContext.NESTED_SELECT);
			} else {
				queryContextStack.push(SQLQueryContext.SELECT);
			}
		}
	}

	@Override
	public void exitQuery_specification(@NotNull final SQLGrammarParser.Query_specificationContext ctx) {

		if (ignoreQuerySpecificationCount == 0) {
			SQLSelect select = SQLBuilder.build().select().withSQLQueryContext(queryContextStack.peek());

			/*
			 * We need maintain the order of selected element, therefore, we use a temp list to store them
			 */
			Stack<SQLConstruct> tempStack = new Stack<SQLConstruct>();

			SQLConstruct temp = null;
			while (!sqlConstructStack.isEmpty()) {
				temp = sqlConstructStack.pop();
				if (temp.getActualType() == SQLConstructType.WHERE) {
					select.withWhereClause((SQLWhere) temp);
				} else if (temp.getActualType() == SQLConstructType.FROM) {
					select.withFromClause((SQLFrom) temp);
				} else if (temp.getActualType() == SQLConstructType.HAVING) {
					select.withHavingClause((SQLHaving) temp);
				} else if (temp.getActualType() == SQLConstructType.ORDER_BY) {
					select.withOrderByClause((SQLOrderBy) temp);
				} else if (temp.getActualType() == SQLConstructType.GROUP_BY) {
					select.withGroupBy((SQLGroupBy) temp);
				} else if (temp.getActualType() == SQLConstructType.ASTERISK) {
					select.withSelectedItem(temp);
				} else if (temp.getActualType() == SQLConstructType.IGNORE_TOKEN) {
					break;
				} else {
					tempStack.push(temp);
				}
			}

			if (null != ctx.set_quantifier()) {
				if (null != ctx.set_quantifier().ALL()) {
					select.selectAll();
				} else {
					select.selectDistinct();
				}
			}

			while (!tempStack.isEmpty()) {
				select.withSelectedItem(tempStack.pop());
			}

			if (temp.getActualType() == SQLConstructType.IGNORE_TOKEN) {
				sqlConstructStack.push(select);
			} else {
				sql = select;
			}

			queryContextStack.pop();
		}

	}

	@Override
	public void enterSubquery(@NotNull final SQLGrammarParser.SubqueryContext ctx) {
		sqlConstructStack.push(SQLBuilder.build().subSelect().withSQLQueryContext(SQLQueryContext.NESTED_SELECT));
		queryContextStack.push(SQLQueryContext.NESTED_SELECT);
		ignoreQuerySpecificationCount++;
	}

	@Override
	public void exitSubquery(@NotNull final SQLGrammarParser.SubqueryContext ctx) {
		SQLConstruct temp = null;

		Stack<SQLConstruct> tempStack = new Stack<SQLConstruct>();
		while (!((temp = sqlConstructStack.pop()).getActualType() == SQLConstructType.SUBSELECT)) {
			tempStack.push(temp);
		}

		SQLSubSelect subSelect = (SQLSubSelect) temp;

		while (!tempStack.isEmpty()) {
			SQLConstruct sqlConstruct = tempStack.pop();
			if (sqlConstruct.getActualType() == SQLConstructType.WHERE) {
				subSelect.withWhereClause((SQLWhere) sqlConstruct);
			} else if (sqlConstruct.getActualType() == SQLConstructType.FROM) {
				subSelect.withFromClause((SQLFrom) sqlConstruct);
			} else if (sqlConstruct.getActualType() == SQLConstructType.HAVING) {
				subSelect.withHavingClause((SQLHaving) sqlConstruct);
			} else if (sqlConstruct.getActualType() == SQLConstructType.ORDER_BY) {
				subSelect.withOrderByClause((SQLOrderBy) sqlConstruct);
			} else if (sqlConstruct.getActualType() == SQLConstructType.GROUP_BY) {
				subSelect.withGroupBy((SQLGroupBy) sqlConstruct);
			} else {
				subSelect.withSelectedItem(sqlConstruct);
			}
		}

		sqlConstructStack.push(subSelect);

		ignoreQuerySpecificationCount--;

		queryContextStack.pop();

	}

	@Override
	public void enterUpdate_statement_searched(@NotNull final SQLGrammarParser.Update_statement_searchedContext ctx) {
		this.withinUpdateStatement = true;
		queryContextStack.push(SQLQueryContext.UPDATE);
	}

	@Override
	public void exitUpdate_statement_searched(@NotNull final SQLGrammarParser.Update_statement_searchedContext ctx) {

		SQLUpdate update = SQLBuilder.build().update().withSQLQueryContext(queryContextStack.peek());

		while (!sqlConstructStack.isEmpty()) {
			SQLConstruct temp = sqlConstructStack.pop();

			if (temp.getActualType() == SQLConstructType.TABLE) {
				update.onTable((SQLTable) temp);
			} else if (temp.getActualType() == SQLConstructType.WHERE) {
				update.withWhereClause((SQLWhere) temp);
			} else {
				update.withSetItem(temp);
			}
		}

		sql = update;

		queryContextStack.pop();
		this.withinUpdateStatement = false;
	}

	@Override
	public void exitSet_clause(@NotNull final SQLGrammarParser.Set_clauseContext ctx) {
		if (null != ctx.Equals_Operator()) {

			SQLConstruct operand2 = sqlConstructStack.pop();
			SQLConstruct operand1 = sqlConstructStack.pop();

			SQLAssignmentFunction compFunc = SQLBuilder.function().assignment(operand1, operand2)
					.withSQLQueryContext(queryContextStack.peek());

			sqlConstructStack.push(compFunc);
		}

	}

	@Override
	public void enterInsert_statement(@NotNull final SQLGrammarParser.Insert_statementContext ctx) {
		this.withinInsertStatement = true;
		queryContextStack.push(SQLQueryContext.INSERT);
	}

	@Override
	public void exitInsert_statement(@NotNull final SQLGrammarParser.Insert_statementContext ctx) {
		SQLInsert insert = SQLBuilder.build().insert().withSQLQueryContext(queryContextStack.peek());

		// embedded query
		if (sqlConstructStack.peek().getActualType() == SQLConstructType.SELECT) {
			insert.withEmbeddedQuery(sqlConstructStack.pop());
		}

		Stack<SQLConstruct> tempStack = new Stack<SQLConstruct>();

		//handle both values and NULL Token
		while (sqlConstructStack.peek().getGeneralType() == SQLConstructType.VALUE) {
			tempStack.push(sqlConstructStack.pop());
		}

		while (!tempStack.isEmpty()) {
			if (tempStack.peek().getActualType() == SQLConstructType.LIST_VALUE) {
				SQLListValue listValue = (SQLListValue) tempStack.pop();
				insert.withNewRowOfValues(listValue.toValues());
			}
			//values 
			else {
				insert.withValue((SQLValue) tempStack.pop());
			}
		}

		while (sqlConstructStack.peek().getGeneralType() == SQLConstructType.COLUMN) {
			tempStack.push(sqlConstructStack.pop());
		}

		while (!tempStack.isEmpty()) {
			insert.withColumn((SQLColumn) tempStack.pop());
		}

		while (!sqlConstructStack.isEmpty()) {
			SQLConstruct temp = sqlConstructStack.pop();

			if (temp.getActualType() == SQLConstructType.TABLE) {
				insert.intoTable((SQLTable) temp);
			}
		}

		sql = insert;

		queryContextStack.pop();
		this.withinInsertStatement = false;
	}

	@Override
	public void enterContextually_typed_row_value_expression(
			@NotNull final SQLGrammarParser.Contextually_typed_row_value_expressionContext ctx) {
		sqlConstructStack.push(SQLBuilder.tokens().ignore().withSQLQueryContext(queryContextStack.peek()));
	}

	@Override
	public void exitContextually_typed_row_value_expression(
			@NotNull final SQLGrammarParser.Contextually_typed_row_value_expressionContext ctx) {

		Stack<SQLConstruct> tempStack = getTempStack();

		SQLListValue listValue = SQLBuilder.init().listValue().withSQLQueryContext(queryContextStack.peek());

		while (!tempStack.isEmpty()) {
			listValue.withValue(tempStack.pop());
		}

		sqlConstructStack.push(listValue);
	}

	@Override
	public void enterDelete_statement_searched(@NotNull final SQLGrammarParser.Delete_statement_searchedContext ctx) {
		this.withinDeleteStatement = true;
		queryContextStack.push(SQLQueryContext.DELETE);
	}

	@Override
	public void exitDelete_statement_searched(@NotNull final SQLGrammarParser.Delete_statement_searchedContext ctx) {

		SQLDelete delete = SQLBuilder.build().delete().withSQLQueryContext(queryContextStack.peek());

		while (!sqlConstructStack.isEmpty()) {
			SQLConstruct temp = sqlConstructStack.pop();

			if (temp.getActualType() == SQLConstructType.WHERE) {
				delete.withWhereClause((SQLWhere) temp);
			} else if (temp.getActualType() == SQLConstructType.TABLE) {
				delete.fromTable((SQLTable) temp);
			}
		}

		sql = delete;

		queryContextStack.pop();
		this.withinDeleteStatement = false;
	}

	@Override
	public void enterFrom_clause(@NotNull final SQLGrammarParser.From_clauseContext ctx) {
		sqlConstructStack.push(SQLBuilder.build().from().withSQLQueryContext(queryContextStack.peek()));
	}

	@Override
	public void exitFrom_clause(@NotNull final SQLGrammarParser.From_clauseContext ctx) {
		SQLConstruct temp = null;

		Stack<SQLConstruct> tempStack = new Stack<SQLConstruct>();
		while (((temp = sqlConstructStack.pop()).getActualType() != SQLConstructType.FROM)) {
			tempStack.push(temp);
		}

		SQLFrom from = (SQLFrom) temp;

		while (!tempStack.isEmpty()) {
			temp = tempStack.pop();
			if (temp.getActualType() == SQLConstructType.TABLE && ((SQLTable) temp).getTableName() == null) {
				continue;
			}
			from.withSource(temp);
		}
		sqlConstructStack.push(from);
	}

	@Override
	public void enterWhere_clause(@NotNull final SQLGrammarParser.Where_clauseContext ctx) {
		sqlConstructStack.push(SQLBuilder.build().where().withSQLQueryContext(queryContextStack.peek()));
	}

	@Override
	public void exitWhere_clause(@NotNull final SQLGrammarParser.Where_clauseContext ctx) {
		SQLSearchConditions conditions = (SQLSearchConditions) sqlConstructStack.pop();

		SQLWhere where = (SQLWhere) sqlConstructStack.peek();

		where.withConditions(conditions);
	}

	@Override
	public void enterHaving_clause(@NotNull final SQLGrammarParser.Having_clauseContext ctx) {
		sqlConstructStack.push(SQLBuilder.build().having().withSQLQueryContext(queryContextStack.peek()));
	}

	@Override
	public void exitHaving_clause(@NotNull final SQLGrammarParser.Having_clauseContext ctx) {
		SQLConstruct SQL = sqlConstructStack.pop();

		SQLHaving having = (SQLHaving) sqlConstructStack.peek();

		having.withConditions((SQLSearchConditions) SQL);

	}

	@Override
	public void enterGroup_by_clause(@NotNull final SQLGrammarParser.Group_by_clauseContext ctx) {
		sqlConstructStack.push(SQLBuilder.build().groupBy().withSQLQueryContext(queryContextStack.peek()));
	}

	@Override
	public void exitGroup_by_clause(@NotNull final SQLGrammarParser.Group_by_clauseContext ctx) {
		Stack<SQLConstruct> stack = new Stack<SQLConstruct>();

		SQLConstruct SQL = null;
		while ((SQL = sqlConstructStack.pop()).getActualType() != SQLConstructType.GROUP_BY) {
			stack.push(SQL);
		}

		SQLGroupBy groupBy = (SQLGroupBy) SQL;

		while (!stack.isEmpty()) {
			groupBy.byColumn(stack.pop());
		}

		sqlConstructStack.push(groupBy);
	}

	@Override
	public void enterOrder_by_clause(@NotNull final SQLGrammarParser.Order_by_clauseContext ctx) {
		SQLOrderBy orderBy = SQLBuilder.build().orderBy();
		orderBy.withSQLQueryContext(queryContextStack.peek());
		sqlConstructStack.push(orderBy);
	}

	@Override
	public void exitOrder_by_clause(@NotNull final SQLGrammarParser.Order_by_clauseContext ctx) {
		Stack<SQLConstruct> stack = new Stack<SQLConstruct>();

		SQLConstruct SQL = null;
		while ((SQL = sqlConstructStack.pop()).getActualType() != SQLConstructType.ORDER_BY) {
			stack.push(SQL);
		}

		SQLOrderBy orderBy = (SQLOrderBy) SQL;

		while (!stack.isEmpty()) {
			orderBy.byColumn(stack.pop());
		}

		sqlConstructStack.push(orderBy);
	}

	@Override
	public void exitOrdering_specification(@NotNull final SQLGrammarParser.Ordering_specificationContext ctx) {
		Stack<SQLConstruct> stack = new Stack<SQLConstruct>();

		SQLConstruct SQL = null;
		while ((SQL = sqlConstructStack.pop()).getActualType() != SQLConstructType.ORDER_BY) {
			stack.push(SQL);
		}

		SQLOrderBy orderBy = (SQLOrderBy) SQL;

		if (null != ctx.ASC()) {
			orderBy.inAscendingOrder();
		} else if (null != ctx.DESC()) {
			orderBy.inDescendingOrder();
		}

		sqlConstructStack.push(orderBy);
		while (!stack.isEmpty()) {
			sqlConstructStack.push(stack.pop());
		}
	}

	@Override
	public void enterAs_clause(@NotNull final SQLGrammarParser.As_clauseContext ctx) {
		ignoreColumnName = true;
	}

	@Override
	public void exitAs_clause(@NotNull final SQLGrammarParser.As_clauseContext ctx) {

		SQLCorrelationName correlationName = SQLBuilder.build().correlationName(ctx.column_name().getText());
		correlationName.withSQLQueryContext(queryContextStack.peek());

		SQLConstruct temp = sqlConstructStack.peek();

		if (temp.getActualType() == SQLConstructType.NUMERIC_VALUE_EXPRESSION) {
			((SQLNumericValueExpression) temp).withAlias(correlationName);
		} else if (temp.getActualType() == SQLConstructType.COLUMN) {
			((SQLColumn) temp).withAlias(correlationName);
		} else {
			sqlConstructStack.push(correlationName);
		}

		ignoreColumnName = false;
	}

	@Override
	public void enterJoined_table(@NotNull final SQLGrammarParser.Joined_tableContext ctx) {
		sqlConstructStack.push(SQLBuilder.tokens().ignore().withSQLQueryContext(queryContextStack.peek()));
	}

	@Override
	public void exitJoined_table(@NotNull final SQLGrammarParser.Joined_tableContext ctx) {

		int childCount = ctx.getChildCount();

		List<SQLJoinType> joinTypes = new ArrayList<SQLJoinType>();

		for (int i = 0; i < childCount; i++) {
			ParseTree child = ctx.getChild(i);
			String text = child.getText();

			if (text.equalsIgnoreCase("LEFT")) {
				joinTypes.add(SQLJoinType.LEFT_JOIN);
			} else if (text.equalsIgnoreCase("RIGHT")) {
				joinTypes.add(SQLJoinType.RIGHT_JOIN);
			} else if (text.equalsIgnoreCase("FULL")) {
				joinTypes.add(SQLJoinType.FULL_JOIN);
			} else if (text.equalsIgnoreCase("INNER")) {
				joinTypes.add(SQLJoinType.INNER_JOIN);
			} else if (text.equalsIgnoreCase("NATURALLEFT")) {
				joinTypes.add(SQLJoinType.NATURAL_LEFT_JOIN);
			} else if (text.equalsIgnoreCase("NATURALRIGHT")) {
				joinTypes.add(SQLJoinType.NATURAL_RIGHT_JOIN);
			} else if (text.equalsIgnoreCase("NATURALFULL")) {
				joinTypes.add(SQLJoinType.NATURAL_FULL_JOIN);
			} else if (text.equalsIgnoreCase("NATURALINNER")) {
				joinTypes.add(SQLJoinType.NATURAL_INNER_JOIN);
			} else if (text.equalsIgnoreCase("CROSS")) {
				joinTypes.add(SQLJoinType.CROSS_JOIN);
			} else if (text.equalsIgnoreCase("UNION")) {
				joinTypes.add(SQLJoinType.UNION_JOIN);
			} else if (text.equalsIgnoreCase("JOIN")) {
				ParseTree prevChild = ctx.getChild(i - 1);
				String prevText = prevChild.getText();
				if (!prevText.equalsIgnoreCase("LEFT") && !prevText.equalsIgnoreCase("RIGHT")
						&& !prevText.equalsIgnoreCase("FULL") && !prevText.equalsIgnoreCase("NATURALLEFT")
						&& !prevText.equalsIgnoreCase("NATURALRIGHT") && !prevText.equalsIgnoreCase("NATURALFULL")
						&& !prevText.equalsIgnoreCase("NATURALINNER") && !prevText.equalsIgnoreCase("INNER")
						&& !prevText.equalsIgnoreCase("CROSS") && !prevText.equalsIgnoreCase("UNION")) {
					joinTypes.add(SQLJoinType.JOIN);
				}
			}

		}

		Stack<SQLConstruct> tempStack = getTempStack();

		SQLTargetResource targetResource = (SQLTargetResource) tempStack.pop();
		SQLTargetResource front = targetResource;

		int joinIndex = 0;
		while (!tempStack.isEmpty()) {

			SQLTargetResource onTargetResource = (SQLTargetResource) tempStack.pop();

			SQLSearchConditions conditions = null;

			if (!tempStack.isEmpty() && tempStack.peek().getActualType() == SQLConstructType.SEARCH_COND) {
				conditions = (SQLSearchConditions) tempStack.pop();
			}

			SQLJoin join = null;

			if (joinTypes.get(joinIndex) == SQLJoinType.NATURAL_LEFT_JOIN) {
				join = SQLBuilder.build().naturalLeftJoin();
			} else if (joinTypes.get(joinIndex) == SQLJoinType.NATURAL_RIGHT_JOIN) {
				join = SQLBuilder.build().naturalRightJoin();
			} else if (joinTypes.get(joinIndex) == SQLJoinType.NATURAL_FULL_JOIN) {
				join = SQLBuilder.build().naturalFullJoin();
			} else if (joinTypes.get(joinIndex) == SQLJoinType.NATURAL_INNER_JOIN) {
				join = SQLBuilder.build().naturalInnerJoin();
			} else if (joinTypes.get(joinIndex) == SQLJoinType.LEFT_JOIN) {
				join = SQLBuilder.build().leftJoin();
			} else if (joinTypes.get(joinIndex) == SQLJoinType.RIGHT_JOIN) {
				join = SQLBuilder.build().rightJoin();
			} else if (joinTypes.get(joinIndex) == SQLJoinType.FULL_JOIN) {
				join = SQLBuilder.build().fullJoin();
			} else if (joinTypes.get(joinIndex) == SQLJoinType.INNER_JOIN) {
				join = SQLBuilder.build().innerJoin();
			} else if (joinTypes.get(joinIndex) == SQLJoinType.UNION_JOIN) {
				join = SQLBuilder.build().unionJoin();
			} else if (joinTypes.get(joinIndex) == SQLJoinType.CROSS_JOIN) {
				join = SQLBuilder.build().crossJoin();
			} else {
				join = SQLBuilder.build().join();
			}

			join.withSQLQueryContext(queryContextStack.peek());

			if (null != conditions) {
				join.withConditions(conditions);
			}
			targetResource.withJoin(join.onTable(onTargetResource));
			joinIndex++;
		}
		sqlConstructStack.push(front);
	}

	@Override
	public void enterSearch_condition(@NotNull final SQLGrammarParser.Search_conditionContext ctx) {
		SQLSearchConditions conditions = SQLBuilder.build().conditions();
		conditions.withSQLQueryContext(queryContextStack.peek());
		sqlConstructStack.push(conditions);
	}

	@Override
	public void exitSearch_condition(@NotNull final SQLGrammarParser.Search_conditionContext ctx) {
		if (sqlConstructStack.peek().getGeneralType() != SQLConstructType.SEARCH_COND) {
			SQLConstruct SQL = sqlConstructStack.pop();
			SQLSearchConditions conditions = SQLBuilder.build().conditions();
			conditions.withCondition(SQL);
			conditions.withSQLQueryContext(queryContextStack.peek());
		} else {
			SQLSearchConditions conditions = (SQLSearchConditions) sqlConstructStack.pop();

			// remove reduncant SQLSearchConditions
			while (sqlConstructStack.peek().getGeneralType() == SQLConstructType.SEARCH_COND) {
				sqlConstructStack.pop();
			}

			sqlConstructStack.push(conditions);
		}
	}

	@Override
	public void enterSelect_list(@NotNull final SQLGrammarParser.Select_listContext ctx) {
		if (null != ctx.Asterisk()) {
			sqlConstructStack.push(SQLBuilder.init().asterisk().withSQLQueryContext(queryContextStack.peek()));
		}
	}

	@Override
	public void enterColumn_name(@NotNull final SQLGrammarParser.Column_nameContext ctx) {
		ignoreIdentifierChain = true;
	}

	@Override
	public void exitColumn_name(@NotNull final SQLGrammarParser.Column_nameContext ctx) {
		if (!ignoreColumnName) {
			sqlConstructStack.push(SQLBuilder.build().column().withColumnName(ctx.getText())
					.withSQLQueryContext(queryContextStack.peek()));
		}
		ignoreIdentifierChain = false;
	}

	@Override
	public void enterTable_primary(@NotNull final SQLGrammarParser.Table_primaryContext ctx) {
		// we don't create SQLTable when TargetSource is embedded query (i.e., RSBSubSelect)
		if (null == ctx.derived_table() && null == ctx.joined_table()) {
			sqlConstructStack.push(SQLBuilder.build().table().withSQLQueryContext(queryContextStack.peek()));
		}
	}

	@Override
	public void enterCorrelation_name(@NotNull final SQLGrammarParser.Correlation_nameContext ctx) {

		if (sqlConstructStack.peek().getGeneralType() == SQLConstructType.TARGET_RESOURCE) {
			((SQLTargetResource) sqlConstructStack.peek()).withCorrelationName(SQLBuilder.build()
					.correlationName(ctx.getText()).withSQLQueryContext(queryContextStack.peek()));
		}

		ignoreIdentifierChain = true;
	}

	@Override
	public void exitCorrelation_name(@NotNull final SQLGrammarParser.Correlation_nameContext ctx) {
		ignoreIdentifierChain = false;
	}

	@Override
	public void enterTable_name(@NotNull final SQLGrammarParser.Table_nameContext ctx) {
		((SQLTable) sqlConstructStack.peek()).withTableName(ctx.getText());
		ignoreIdentifierChain = true;
	}

	@Override
	public void exitTable_name(@NotNull final SQLGrammarParser.Table_nameContext ctx) {
		ignoreIdentifierChain = false;
	}

	@Override
	public void enterInsertion_target(@NotNull final SQLGrammarParser.Insertion_targetContext ctx) {
		sqlConstructStack.push(SQLBuilder.build().table().withSQLQueryContext(queryContextStack.peek()));
	}

	@Override
	public void enterTarget_table(@NotNull final SQLGrammarParser.Target_tableContext ctx) {
		sqlConstructStack.push(SQLBuilder.build().table().withSQLQueryContext(queryContextStack.peek()));
	}

	@Override
	public void exitIdentifier_chain(@NotNull final SQLGrammarParser.Identifier_chainContext ctx) {

		if (!ignoreIdentifierChain) {

			String identifierChain = ctx.getText();

			String[] identifiers = identifierChain.split("\\.");

			SQLColumn column = SQLBuilder.build().column().withSQLQueryContext(queryContextStack.peek());

			if (identifiers.length == 3) {
				// with schema name

			} else if (identifiers.length == 2) {
				// with table correlation name
				column.withColumnName(identifiers[1]).withCorrelationName(
						SQLBuilder.build().correlationName(identifiers[0])
								.withSQLQueryContext(queryContextStack.peek()));
			} else if (identifiers.length == 1) {
				// only column name
				column.withColumnName(identifiers[0]);
			}

			sqlConstructStack.push(column);
		}

	}

	@Override
	public void enterParenthesized_boolean_value_expression(
			@NotNull final SQLGrammarParser.Parenthesized_boolean_value_expressionContext ctx) {
		withinParenthesis = true;
	}

	@Override
	public void exitParenthesized_boolean_value_expression(
			@NotNull final SQLGrammarParser.Parenthesized_boolean_value_expressionContext ctx) {
		withinParenthesis = false;
	}

	@Override
	public void exitBoolean_factor(@NotNull final SQLGrammarParser.Boolean_factorContext ctx) {
		if (null != ctx.NOT()) {
			SQLNotFunction notFunc = SQLBuilder.function().not(sqlConstructStack.pop())
					.withSQLQueryContext(queryContextStack.peek());
			sqlConstructStack.push(notFunc);
		}

	}

	@Override
	public void enterBoolean_value_expression(@NotNull final SQLGrammarParser.Boolean_value_expressionContext ctx) {
		sqlConstructStack.push(SQLBuilder.tokens().ignore().withSQLQueryContext(queryContextStack.peek()));
	}

	@Override
	public void exitBoolean_value_expression(@NotNull final SQLGrammarParser.Boolean_value_expressionContext ctx) {

		List<String> tempList = new ArrayList<String>();

		// get all AND and OR Terminal Node
		int childCount = ctx.getChildCount();
		for (int i = 0; i < childCount; i++) {
			if (ctx.getChild(i) instanceof Boolean_termContext) {
				Boolean_termContext booleanTerm = (Boolean_termContext) ctx.getChild(i);
				int numberOfAnd = booleanTerm.AND().size();
				for (int j = 0; j < numberOfAnd; j++) {
					tempList.add(SQLGrammarTokens.AND);
				}
			}
		}
		int numberOfOr = ctx.OR().size();
		for (int i = 0; i < numberOfOr; i++) {
			tempList.add(SQLGrammarTokens.OR);
		}

		if (tempList.size() > 0) {
			// get all predicates in the original order
			Stack<SQLConstruct> tempStack = getTempStack();

			// create search condition
			if (!tempStack.isEmpty()) {
				SQLSearchConditions conditions = SQLBuilder.build().conditions()
						.withSQLQueryContext(queryContextStack.peek());
				conditions.withCondition(tempStack.pop());
				for (int i = 0; i < tempList.size(); i++) {

					if (tempList.get(i) == SQLGrammarTokens.AND) {
						if (withinParenthesis && i + 1 == tempList.size()) {
							conditions.andWithParen(tempStack.pop());
						} else {
							conditions.and(tempStack.pop());
						}
					} else {
						if (withinParenthesis && i + 1 == tempList.size()) {
							conditions.orWithParen(tempStack.pop());
						} else {
							conditions.or(tempStack.pop());
						}
					}
				}
				sqlConstructStack.push(conditions);
			}
		} else {
			// get all predicates in the original order

			Stack<SQLConstruct> tempStack = getTempStack();

			if (sqlConstructStack.peek().getGeneralType() == SQLConstructType.SEARCH_COND) {
				SQLSearchConditions conditions = (SQLSearchConditions) sqlConstructStack.peek();
				conditions.withCondition(tempStack.pop());
			} else {
				while (!tempStack.isEmpty()) {
					sqlConstructStack.push(tempStack.pop());
				}
			}
		}

	}

	@Override
	public void enterGeneral_set_function(@NotNull final SQLGrammarParser.General_set_functionContext ctx) {
		Set_function_typeContext setFuncTypeContext = ctx.getChild(SQLGrammarParser.Set_function_typeContext.class, 0);

		String type = setFuncTypeContext.getText();
		if (type.equalsIgnoreCase(SQLGrammarTokens.SUM)) {
			sqlConstructStack.push(SQLBuilder.function().sum().withSQLQueryContext(queryContextStack.peek()));
		} else if (type.equalsIgnoreCase(SQLGrammarTokens.COUNT)) {
			sqlConstructStack.push(SQLBuilder.function().count().withSQLQueryContext(queryContextStack.peek()));
		} else if (type.equalsIgnoreCase(SQLGrammarTokens.MAX)) {
			sqlConstructStack.push(SQLBuilder.function().max().withSQLQueryContext(queryContextStack.peek()));
		} else if (type.equalsIgnoreCase(SQLGrammarTokens.MIN)) {
			sqlConstructStack.push(SQLBuilder.function().min().withSQLQueryContext(queryContextStack.peek()));
		} else if (type.equalsIgnoreCase(SQLGrammarTokens.AVG)) {
			sqlConstructStack.push(SQLBuilder.function().avg().withSQLQueryContext(queryContextStack.peek()));
		}

		withinGeneralSetFunction = true;
	}

	@Override
	public void exitGeneral_set_function(@NotNull final SQLGrammarParser.General_set_functionContext ctx) {
		withinGeneralSetFunction = false;
	}

	@Override
	public void exitValue_expression(@NotNull final SQLGrammarParser.Value_expressionContext ctx) {

		if (withinGeneralSetFunction) {
			Stack<SQLConstruct> tempStack = new Stack<SQLConstruct>();
			while (!(sqlConstructStack.peek().getGeneralType() == SQLConstructType.FUNCTION)) {
				tempStack.push(sqlConstructStack.pop());
			}
			SQLFunction func = (SQLFunction) sqlConstructStack.peek();

			if (func.getActualType() == SQLConstructType.SUM_FUNCTION) {
				((SQLSumFunction) func).withOperand(tempStack.pop());
			} else if (func.getActualType() == SQLConstructType.COUNT_FUNCTION) {
				((SQLCountFunction) func).withOperand(tempStack.pop());
			} else if (func.getActualType() == SQLConstructType.MAX_FUNCTION) {
				((SQLMaxFunction) func).withOperand(tempStack.pop());
			} else if (func.getActualType() == SQLConstructType.MIN_FUNCTION) {
				((SQLMinFunction) func).withOperand(tempStack.pop());
			} else if (func.getActualType() == SQLConstructType.AVG_FUNCTION) {
				((SQLAvgFunction) func).withOperand(tempStack.pop());
			}

		}
	}

	@Override
	public void enterComparison_predicate(@NotNull final SQLGrammarParser.Comparison_predicateContext ctx) {
		sqlConstructStack.push(SQLBuilder.function().comparison().withSQLQueryContext(queryContextStack.peek()));
	}

	@Override
	public void exitComparison_predicate(@NotNull final SQLGrammarParser.Comparison_predicateContext ctx) {
		SQLConstruct operand2 = sqlConstructStack.pop();
		SQLConstruct operator = sqlConstructStack.pop();
		SQLConstruct operand1 = sqlConstructStack.pop();

		SQLComparisonFunction comparsionFunction = (SQLComparisonFunction) sqlConstructStack.peek();
		comparsionFunction.withOperand1(operand1).withOperand2(operand2).withOperator((SQLOperator) operator);
	}

	@Override
	public void enterComp_op(@NotNull final SQLGrammarParser.Comp_opContext ctx) {
		sqlConstructStack.push(SQLBuilder.init().operator(ctx.getText()).withSQLQueryContext(queryContextStack.peek()));
	}

	@Override
	public void enterUnsigned_numeric_literal(@NotNull final SQLGrammarParser.Unsigned_numeric_literalContext ctx) {

		SQLSign sign = null;

		if (sqlConstructStack.peek().getActualType() == SQLConstructType.SIGN_TOKEN) {
			sign = (SQLSign) sqlConstructStack.pop();
			sign.withSQLQueryContext(queryContextStack.peek());
		}

		String value = ctx.getText();

		if (Utility.isNumber(value)) {
			if (Utility.isFloatNumber(value)) {
				SQLFloatValue SQLFloatValue = SQLBuilder.init().floatValue(Float.valueOf(ctx.getText()))
						.withSQLQueryContext(queryContextStack.peek());
				if (null != sign) {
					SQLFloatValue.withSign(sign);
				}
				sqlConstructStack.push(SQLFloatValue);
			} else {
				SQLIntValue SQLIntValue = SQLBuilder.init().intValue(Integer.valueOf(ctx.getText()))
						.withSQLQueryContext(queryContextStack.peek());
				if (null != sign) {
					SQLIntValue.withSign(sign);
				}
				sqlConstructStack.push(SQLIntValue);
			}
		} else {
			LOG.error("unsigned numberic literal [" + ctx.getText() + "] not supported yet");
		}

	}

	@Override
	public void enterIn_value_list(@NotNull final SQLGrammarParser.In_value_listContext ctx) {
		sqlConstructStack.push(SQLBuilder.init().listValue().withSQLQueryContext(queryContextStack.peek()));
	}

	@Override
	public void exitIn_value_list(@NotNull final SQLGrammarParser.In_value_listContext ctx) {
		SQLConstruct temp = null;

		Stack<SQLConstruct> tempStack = new Stack<SQLConstruct>();
		while (sqlConstructStack.peek().getGeneralType() == SQLConstructType.VALUE
				&& sqlConstructStack.peek().getActualType() != SQLConstructType.LIST_VALUE) {
			temp = sqlConstructStack.pop();
			tempStack.add(temp);
		}
		temp = sqlConstructStack.pop();

		while (!tempStack.isEmpty()) {
			((SQLListValue) temp).withValue(tempStack.pop());
		}

		sqlConstructStack.push(temp);
	}

	@Override
	public void enterIn_predicate(@NotNull final SQLGrammarParser.In_predicateContext ctx) {
		sqlConstructStack.push(SQLBuilder.function().in().withSQLQueryContext(queryContextStack.peek()));
	}

	@Override
	public void exitIn_predicate(@NotNull final SQLGrammarParser.In_predicateContext ctx) {
		SQLConstruct operand2 = sqlConstructStack.pop();
		SQLConstruct operand1 = sqlConstructStack.pop();
		SQLInFunction inFunction = (SQLInFunction) sqlConstructStack.peek();

		inFunction.withOperand1(operand1).withOperand2(operand2);

		if (null != ctx.in_predicate_part_2().NOT()) {
			inFunction.withNot(true);
		}
	}

	@Override
	public void enterLike_predicate(@NotNull final SQLGrammarParser.Like_predicateContext ctx) {
		sqlConstructStack.push(SQLBuilder.function().like().withSQLQueryContext(queryContextStack.peek()));
	}

	@Override
	public void exitLike_predicate(@NotNull final SQLGrammarParser.Like_predicateContext ctx) {
		SQLConstruct operand2 = sqlConstructStack.pop();
		SQLConstruct operand1 = sqlConstructStack.pop();

		SQLLikeFunction likeFunction = (SQLLikeFunction) sqlConstructStack.peek();
		likeFunction.withOperand1(operand1).withOperand2((SQLStringValue) operand2);
	}

	@Override
	public void enterBetween_predicate(@NotNull final SQLGrammarParser.Between_predicateContext ctx) {
		sqlConstructStack.push(SQLBuilder.function().between().withSQLQueryContext(queryContextStack.peek()));
	}

	@Override
	public void exitBetween_predicate(@NotNull final SQLGrammarParser.Between_predicateContext ctx) {
		SQLConstruct predicate2 = sqlConstructStack.pop();
		SQLConstruct predicate1 = sqlConstructStack.pop();
		SQLConstruct predicand = sqlConstructStack.pop();
		SQLBetweenFunction betweenFunc = (SQLBetweenFunction) sqlConstructStack.peek();
		if (null != ctx.between_predicate_part_2().NOT()) {
			betweenFunc.notBetween(predicate1, predicate2);
		} else {
			betweenFunc.between(predicate1, predicate2);
		}

		if (null != ctx.between_predicate_part_2().ASYMMETRIC()) {
			betweenFunc.asymmetric();
		} else if (null != ctx.between_predicate_part_2().SYMMETRIC()) {
			betweenFunc.symmetric();
		}

		betweenFunc.withPredicand(predicand);
	}

	@Override
	public void exitNull_predicate(@NotNull final SQLGrammarParser.Null_predicateContext ctx) {
		SQLNullFunction nullFunc = null;
		if (null == ctx.null_predicate_part_2().NOT()) {
			nullFunc = SQLBuilder.function().isNull().withOperand(sqlConstructStack.pop())
					.withSQLQueryContext(queryContextStack.peek());
		} else {
			nullFunc = SQLBuilder.function().notNull().withOperand(sqlConstructStack.pop())
					.withSQLQueryContext(queryContextStack.peek());
		}
		sqlConstructStack.push(nullFunc);
	}

	@Override
	public void enterExists_predicate(@NotNull final SQLGrammarParser.Exists_predicateContext ctx) {
		sqlConstructStack.push(SQLBuilder.function().exists().withSQLQueryContext(queryContextStack.peek()));
	}

	@Override
	public void exitExists_predicate(@NotNull final SQLGrammarParser.Exists_predicateContext ctx) {
		SQLConstruct subquery = sqlConstructStack.pop();
		SQLExistsFunction existsFunc = (SQLExistsFunction) sqlConstructStack.peek();

		existsFunc.withSubQuery(subquery);
	}

	@Override
	public void enterGeneral_literal(@NotNull final SQLGrammarParser.General_literalContext ctx) {
		ignoreIdentifierChain = true;
	}

	@Override
	public void exitGeneral_literal(@NotNull final SQLGrammarParser.General_literalContext ctx) {
		ignoreIdentifierChain = false;

		String value = ctx.getText();

		if (Utility.isBooleanValue(value)) {
			if (Boolean.valueOf(value)) {
				sqlConstructStack.push(SQLBuilder.init().truthValue().withSQLQueryContext(queryContextStack.peek()));
			} else {
				sqlConstructStack.push(SQLBuilder.init().falseValue().withSQLQueryContext(queryContextStack.peek()));
			}
		} else if (Utility.isNumber(value)) {
			if (Utility.isFloatNumber(value)) {
				sqlConstructStack.push(SQLBuilder.init().floatValue(Float.valueOf(value))
						.withSQLQueryContext(queryContextStack.peek()));
			} else {
				sqlConstructStack.push(SQLBuilder.init().intValue(Integer.valueOf(value))
						.withSQLQueryContext(queryContextStack.peek()));
			}
		} else {
			sqlConstructStack.push(SQLBuilder.init().stringValue(String.valueOf(value))
					.withSQLQueryContext(queryContextStack.peek()));
		}
	}

	@Override
	public void enterFrom_subquery(@NotNull final SQLGrammarParser.From_subqueryContext ctx) {
		withInFromSubquery = true;
	}

	@Override
	public void exitFrom_subquery(@NotNull final SQLGrammarParser.From_subqueryContext ctx) {
		withInFromSubquery = false;
	}

	@Override
	public void enterNumeric_value_expression(@NotNull final SQLGrammarParser.Numeric_value_expressionContext ctx) {
		if (null != ctx.sign() && ctx.sign().size() > 0) {
			sqlConstructStack.push(SQLBuilder.tokens().ignore().withSQLQueryContext(queryContextStack.peek()));
		}
	}

	@Override
	public void exitNumeric_value_expression(@NotNull final SQLGrammarParser.Numeric_value_expressionContext ctx) {
		if (null != ctx.sign() && ctx.sign().size() > 0) {
			List<SignContext> signList = ctx.sign();

			Stack<SQLConstruct> tempStack = getTempStack();

			SQLNumericValueExpression numericValueExpr = SQLBuilder.build().numericValueExpr()
					.withSQLQueryContext(queryContextStack.peek());
			numericValueExpr.withTerm(tempStack.pop());
			int index = 0;
			while (!tempStack.isEmpty()) {
				SQLOperator operator = SQLBuilder.init().operator(signList.get(index++).getText())
						.withSQLQueryContext(queryContextStack.peek());
				numericValueExpr.withSign(operator);
				numericValueExpr.withTerm(tempStack.pop());
			}
			sqlConstructStack.push(numericValueExpr);
		}
	}

	@Override
	public void enterSign(@NotNull final SQLGrammarParser.SignContext ctx) {
		// for numeric value expression, we handle signs in exitNumeric_value_expression method
		if (!(ctx.getParent() instanceof SQLGrammarParser.Numeric_value_expressionContext)) {
			sqlConstructStack.push(SQLBuilder.tokens().sign(ctx.getText()));
		}
	}

	@Override
	public void enterDynamic_parameter_specification(
			@NotNull final SQLGrammarParser.Dynamic_parameter_specificationContext ctx) {
		sqlConstructStack.push(SQLBuilder.init().dynamicParameter().withSQLQueryContext(queryContextStack.peek()));
	}

	private Stack<SQLConstruct> getTempStack() {
		Stack<SQLConstruct> tempStack = new Stack<SQLConstruct>();
		while (sqlConstructStack.peek().getActualType() != SQLConstructType.IGNORE_TOKEN) {
			tempStack.push(sqlConstructStack.pop());
		}
		sqlConstructStack.pop(); // remove IGNORE TOKEN
		return tempStack;
	}

	@Override
	public void enterNull_specification(@NotNull final SQLGrammarParser.Null_specificationContext ctx) {
		sqlConstructStack.push(SQLBuilder.init().nullValue());
	}
}
