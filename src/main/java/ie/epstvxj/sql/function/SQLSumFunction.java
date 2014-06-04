/**
 * xAdd Project
 * 
 * @author jun 
 * @since 14 Mar 2014
 */
package ie.epstvxj.sql.function;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.SQLConstruct;
import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;

/**
 * @author jun
 *
 */
public final class SQLSumFunction extends SQLFunction {

	private int	operandIndex	= -1;

	public SQLSumFunction() {

	}

	public SQLSumFunction(final SQLConstruct operand) {
		withOperand(operand);
	}

	public SQLSumFunction withOperand(final SQLConstruct operand) {
		this.operandIndex = this.getRepository().addSQLConstruct(operand);
		operand.setReferencingConstruct(this);
		return this;
	}

	public SQLConstruct getOperand() {
		return this.getRepository().getSQLConstruct(operandIndex);
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(SQLGrammarTokens.SUM).append(SQLGrammarTokens.LEFT_PAREN).append(getOperand().toSql())
				.append(SQLGrammarTokens.RIGHT_PAREN);

		return sql.toString();
	}

	@Override
	public SQLSumFunction withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLSumFunction withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.FUNCTION;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.SUM_FUNCTION;
	}

	@Override
	public SQLSumFunction deepClone() {
		return new SQLSumFunction(getOperand().deepClone()).withCloneId(
				SQLConstructIdentifierManager.getRSCCloneIdentifier(this)).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLSumFunction deepCloneWithPreservedIdentifier() {
		return new SQLSumFunction(getOperand().deepCloneWithPreservedIdentifier()).withCloneId(this.getIdentifier())
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLSumFunction withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
