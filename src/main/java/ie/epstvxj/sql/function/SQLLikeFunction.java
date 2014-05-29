/**
 * xAdd Project
 * 
 * @author jun 
 * @since 28 Mar 2014
 */
package ie.epstvxj.sql.function;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.SQLConstruct;
import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;
import ie.epstvxj.sql.value.SQLStringValue;

/**
 * @author jun
 *
 */
public class SQLLikeFunction extends SQLFunction {

	private int	operandIndex1	= -1;
	private int	operandIndex2	= -1;

	public SQLLikeFunction() {

	}

	public SQLLikeFunction(final SQLConstruct operand1, final SQLStringValue operand2) {
		withOperand1(operand1);
		withOperand2(operand2);
	}

	public SQLLikeFunction withOperand2(final SQLStringValue operand2) {
		this.operandIndex2 = this.getRSCRepository().addRSC(operand2);
		operand2.setReferencingRSC(this);
		return this;
	}

	public SQLLikeFunction withOperand1(final SQLConstruct operand1) {
		this.operandIndex1 = this.getRSCRepository().addRSC(operand1);
		operand1.setReferencingRSC(this);
		return this;
	}

	public SQLConstruct getOperand1() {
		return this.getRSCRepository().getRSC(operandIndex1);
	}

	public SQLStringValue getOperand2() {
		return (SQLStringValue) this.getRSCRepository().getRSC(operandIndex2);
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.FUNCTION;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.LIKE_FUNCTION;
	}

	@Override
	public SQLLikeFunction withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLLikeFunction withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(getOperand1().toSql()).append(SQLGrammarTokens.SPACE).append(SQLGrammarTokens.LIKE).append(SQLGrammarTokens.SPACE)
				.append(getOperand2().toSql());

		return sql.toString();
	}

	@Override
	public SQLLikeFunction deepClone() {
		return new SQLLikeFunction(getOperand1().deepClone(), getOperand2().deepClone()).withCloneId(
				SQLConstructIdentifierManager.getRSCCloneIdentifier(this)).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLLikeFunction deepCloneWithPreservedIdentifier() {
		return new SQLLikeFunction(getOperand1().deepCloneWithPreservedIdentifier(), getOperand2()
				.deepCloneWithPreservedIdentifier()).withCloneId(this.getIdentifier()).withSQLQueryContext(
				this.sqlQueryContext);
	}

	@Override
	public SQLLikeFunction withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
