package ie.epstvxj.sql.function;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.SQLConstruct;
import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;

public class SQLCountFunction extends SQLFunction {

	private int	operandIndex	= -1;

	public SQLCountFunction() {

	}

	public SQLCountFunction(final SQLConstruct operand) {
		withOperand(operand);
	}

	public SQLCountFunction withOperand(final SQLConstruct operand) {
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

		sql.append(SQLGrammarTokens.COUNT).append(SQLGrammarTokens.LEFT_PAREN).append(getOperand().toSql())
				.append(SQLGrammarTokens.RIGHT_PAREN);

		return sql.toString();
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.COUNT_FUNCTION;
	}

	@Override
	public SQLCountFunction withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLCountFunction withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLCountFunction deepClone() {
		return new SQLCountFunction(getOperand().deepClone()).withCloneId(
				SQLConstructIdentifierManager.getRSCCloneIdentifier(this)).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLCountFunction deepCloneWithPreservedIdentifier() {
		return new SQLCountFunction(getOperand().deepCloneWithPreservedIdentifier()).withCloneId(this.getIdentifier())
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLCountFunction withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
