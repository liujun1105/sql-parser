package ie.epstvxj.sql.function;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.SQLBuilder;
import ie.epstvxj.sql.SQLConstruct;
import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;

/**
 * @author Jun Liu
 * 
 * 
 * Used in UPDATE SET clause to indicate the assignment nature of this function
 * 
 * 
 * UPDATE T1 SET a=value1
 * 
 * RSCAssigmnetFunction will represent a=value1
 *
 */
public class SQLAssignmentFunction extends SQLComparisonFunction {

	public SQLAssignmentFunction() {

	}

	public SQLAssignmentFunction(final SQLConstruct operand1, final SQLConstruct operand2) {
		withOperand1(operand1);
		withOperand2(operand2);
		withOperator(SQLBuilder.init().operator(SQLGrammarTokens.EQUAL).withSQLQueryContext(super.getSQLQueryContext()));
	}

	@Override
	public SQLAssignmentFunction withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLAssignmentFunction withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.FUNCTION;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.ASSIGNMENT_FUNCTION;
	}

	@Override
	public SQLAssignmentFunction deepClone() {
		return new SQLAssignmentFunction(getOperand1().deepClone(), getOperand2().deepClone()).withCloneId(
				SQLConstructIdentifierManager.getRSCCloneIdentifier(this)).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLAssignmentFunction deepCloneWithPreservedIdentifier() {
		String identifier = this.getIdentifier();

		return new SQLAssignmentFunction(getOperand1().deepCloneWithPreservedIdentifier(), getOperand2()
				.deepCloneWithPreservedIdentifier()).withCloneId(identifier).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLAssignmentFunction withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
