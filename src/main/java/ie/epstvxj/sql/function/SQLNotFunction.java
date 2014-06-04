/**
 * xAdd Project
 * 
 * @author jun 
 * @since 31 Mar 2014
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
public class SQLNotFunction extends SQLFunction {

	private int	operandIndex	= -1;

	public SQLNotFunction() {

	}

	public SQLNotFunction(final SQLConstruct operand) {
		withOperand(operand);
	}

	public SQLNotFunction withOperand(final SQLConstruct operand) {
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

		sql.append(SQLGrammarTokens.NOT).append(SQLGrammarTokens.SPACE);
		sql.append(getOperand().toSql());

		return sql.toString();
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.NOT_FUNCTION;
	}

	@Override
	public SQLNotFunction withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLNotFunction withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLNotFunction deepClone() {
		return new SQLNotFunction().withOperand(getOperand().deepClone())
				.withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLNotFunction deepCloneWithPreservedIdentifier() {
		return new SQLNotFunction().withOperand(getOperand().deepCloneWithPreservedIdentifier())
				.withCloneId(this.getIdentifier()).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLNotFunction withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
