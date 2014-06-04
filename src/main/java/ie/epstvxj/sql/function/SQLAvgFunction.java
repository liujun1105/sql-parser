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
public final class SQLAvgFunction extends SQLFunction {

	private int	operandIndex	= -1;

	public SQLAvgFunction() {

	}

	public SQLAvgFunction(final SQLConstruct operand) {
		withOperand(operand);
	}

	public SQLAvgFunction withOperand(final SQLConstruct operand) {
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

		sql.append(SQLGrammarTokens.AVG).append(SQLGrammarTokens.LEFT_PAREN).append(getOperand().toSql())
				.append(SQLGrammarTokens.RIGHT_PAREN);

		return sql.toString();
	}

	@Override
	public SQLAvgFunction withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLAvgFunction withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.FUNCTION;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.AVG_FUNCTION;
	}

	@Override
	public SQLAvgFunction deepClone() {
		return new SQLAvgFunction(getOperand().deepClone()).withCloneId(
				SQLConstructIdentifierManager.getRSCCloneIdentifier(this)).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLAvgFunction deepCloneWithPreservedIdentifier() {
		return new SQLAvgFunction(getOperand().deepCloneWithPreservedIdentifier()).withCloneId(this.getIdentifier())
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLAvgFunction withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
