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
public final class SQLMaxFunction extends SQLFunction {

	private int	operandIndex	= -1;

	public SQLMaxFunction() {

	}

	public SQLMaxFunction(final SQLConstruct operand) {
		withOperand(operand);
	}

	public SQLMaxFunction withOperand(final SQLConstruct operand) {
		this.operandIndex = this.getRSCRepository().addRSC(operand);
		operand.setReferencingRSC(this);
		return this;
	}

	public SQLConstruct getOperand() {
		return this.getRSCRepository().getRSC(operandIndex);
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(SQLGrammarTokens.MAX).append(SQLGrammarTokens.LEFT_PAREN).append(getOperand().toSql())
				.append(SQLGrammarTokens.RIGHT_PAREN);

		return sql.toString();
	}

	@Override
	public SQLMaxFunction withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLMaxFunction withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.FUNCTION;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.MAX_FUNCTION;
	}

	@Override
	public SQLMaxFunction deepClone() {
		return new SQLMaxFunction(getOperand().deepClone()).withCloneId(
				SQLConstructIdentifierManager.getRSCCloneIdentifier(this)).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLMaxFunction deepCloneWithPreservedIdentifier() {
		return new SQLMaxFunction(getOperand().deepCloneWithPreservedIdentifier()).withCloneId(this.getIdentifier())
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLMaxFunction withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
