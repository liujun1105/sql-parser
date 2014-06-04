/**
 * xAdd Project
 * 
 * @author jun 
 * @since 3 Apr 2014
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
public class SQLMinFunction extends SQLFunction {

	private int	operandIndex	= -1;

	public SQLMinFunction() {

	}

	public SQLMinFunction(final SQLConstruct operand) {
		withOperand(operand);
	}

	public SQLMinFunction withOperand(final SQLConstruct operand) {
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

		sql.append(SQLGrammarTokens.MIN).append(SQLGrammarTokens.LEFT_PAREN).append(getOperand().toSql())
				.append(SQLGrammarTokens.RIGHT_PAREN);

		return sql.toString();
	}

	@Override
	public SQLMinFunction withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLMinFunction withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.FUNCTION;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.MIN_FUNCTION;
	}

	@Override
	public SQLMinFunction deepClone() {
		return new SQLMinFunction(getOperand().deepClone()).withCloneId(
				SQLConstructIdentifierManager.getRSCCloneIdentifier(this)).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLMinFunction deepCloneWithPreservedIdentifier() {
		return new SQLMinFunction(getOperand().deepCloneWithPreservedIdentifier()).withCloneId(this.getIdentifier())
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLMinFunction withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
