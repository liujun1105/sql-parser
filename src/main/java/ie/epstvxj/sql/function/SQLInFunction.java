package ie.epstvxj.sql.function;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.SQLConstruct;
import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;

public class SQLInFunction extends SQLFunction {

	private int		operandIndex1	= -1;
	private int		operandIndex2	= -1;

	private boolean	isNot;

	public SQLInFunction() {

	}

	public SQLInFunction(final SQLConstruct operand1, final SQLConstruct operand2) {
		withOperand1(operand1);
		withOperand2(operand2);
	}

	public SQLInFunction notIn(final SQLConstruct operand1, final SQLConstruct operand2) {
		withOperand1(operand1);
		withOperand2(operand2);
		withNot(true);
		return this;
	}

	public SQLInFunction withNot(final boolean isNot) {
		this.isNot = isNot;
		return this;
	}

	public SQLInFunction withOperand1(final SQLConstruct operand1) {
		this.operandIndex1 = this.getRepository().addSQLConstruct(operand1);
		operand1.setReferencingConstruct(this);
		return this;
	}

	public SQLConstruct getOperand1() {
		return this.getRepository().getSQLConstruct(operandIndex1);
	}

	public SQLInFunction withOperand2(final SQLConstruct operand2) {
		if (null != operand2) {
			this.operandIndex2 = this.getRepository().addSQLConstruct(operand2);
			operand2.setReferencingConstruct(this);
		}
		return this;
	}

	public SQLConstruct getOperand2() {
		return this.getRepository().getSQLConstruct(operandIndex2);
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(getOperand1().toSql()).append(SQLGrammarTokens.SPACE);
		if (isNot) {
			sql.append(SQLGrammarTokens.NOT).append(SQLGrammarTokens.SPACE);
		}
		sql.append(SQLGrammarTokens.IN).append(SQLGrammarTokens.SPACE);
		sql.append(getOperand2().toSql());

		return sql.toString();
	}

	@Override
	public SQLInFunction withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLInFunction withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.FUNCTION;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.IN_FUNCTION;
	}

	@Override
	public SQLInFunction deepClone() {
		if (isNot) {
			return new SQLInFunction().notIn(getOperand1().deepClone(), getOperand2().deepClone())
					.withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
					.withSQLQueryContext(this.sqlQueryContext);
		} else {
			return new SQLInFunction(getOperand1().deepClone(), getOperand2().deepClone()).withCloneId(
					SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
					.withSQLQueryContext(this.sqlQueryContext);
		}
	}

	@Override
	public SQLInFunction deepCloneWithPreservedIdentifier() {
		if (isNot) {
			return new SQLInFunction()
					.notIn(getOperand1().deepCloneWithPreservedIdentifier(),
							getOperand2().deepCloneWithPreservedIdentifier()).withCloneId(this.getIdentifier())
					.withSQLQueryContext(this.sqlQueryContext);
		} else {
			return new SQLInFunction(getOperand1().deepCloneWithPreservedIdentifier(), getOperand2()
					.deepCloneWithPreservedIdentifier()).withCloneId(this.getIdentifier()).withSQLQueryContext(
					this.sqlQueryContext);
		}
	}

	@Override
	public SQLInFunction withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
