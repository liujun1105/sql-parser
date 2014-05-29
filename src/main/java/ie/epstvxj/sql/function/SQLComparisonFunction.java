/**
 * xAdd Project
 * 
 * @author jun 
 * @since 27 Mar 2014
 */
package ie.epstvxj.sql.function;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.SQLConstruct;
import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;
import ie.epstvxj.sql.value.SQLOperator;

/**
 * @author jun
 *
 */
public class SQLComparisonFunction extends SQLFunction {

	private int	operandIndex1	= -1;
	private int	operandIndex2	= -1;
	private int	operatorIndex	= -1;

	public SQLComparisonFunction() {

	}

	public SQLComparisonFunction(final SQLConstruct operand1, final SQLOperator operator, final SQLConstruct operand2) {
		withOperand1(operand1);
		withOperand2(operand2);
		withOperator(operator);
	}

	public SQLComparisonFunction withOperand1(final SQLConstruct operand1) {
		this.operandIndex1 = this.getRSCRepository().addRSC(operand1);
		operand1.setReferencingRSC(this);
		return this;
	}

	public SQLComparisonFunction withOperand2(final SQLConstruct operand2) {
		this.operandIndex2 = this.getRSCRepository().addRSC(operand2);
		operand2.setReferencingRSC(this);
		return this;
	}

	public SQLComparisonFunction withOperator(final SQLOperator operator) {
		this.operatorIndex = this.getRSCRepository().addRSC(operator);
		operator.setReferencingRSC(this);
		return this;
	}

	public SQLConstruct getOperand1() {
		return this.getRSCRepository().getRSC(operandIndex1);
	}

	public SQLConstruct getOperand2() {
		return this.getRSCRepository().getRSC(operandIndex2);
	}

	public SQLOperator getOperator() {
		return (SQLOperator) this.getRSCRepository().getRSC(operatorIndex);
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(getOperand1().toSql()).append(SQLGrammarTokens.SPACE);
		sql.append(getOperator().toSql()).append(SQLGrammarTokens.SPACE);
		sql.append(getOperand2().toSql());

		return sql.toString();
	}

	@Override
	public SQLComparisonFunction withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLComparisonFunction withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.FUNCTION;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.COMPARISON_FUNCTION;
	}

	@Override
	public SQLComparisonFunction deepClone() {
		return new SQLComparisonFunction(getOperand1().deepClone(), getOperator().deepClone(), getOperand2()
				.deepClone()).withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLComparisonFunction deepCloneWithPreservedIdentifier() {
		return new SQLComparisonFunction(getOperand1().deepCloneWithPreservedIdentifier(), getOperator()
				.deepCloneWithPreservedIdentifier(), getOperand2().deepCloneWithPreservedIdentifier()).withCloneId(
				this.getIdentifier()).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLComparisonFunction withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
