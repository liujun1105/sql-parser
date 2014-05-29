package ie.epstvxj.sql.function;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.SQLConstruct;
import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;

public class SQLUserDefinedFunction extends SQLFunction {

	private int		operandIndex	= -1;
	private String	functionName	= null;

	public SQLUserDefinedFunction() {

	}

	public SQLUserDefinedFunction(final String functionName, final SQLConstruct operand) {
		this.operandIndex = this.getRSCRepository().addRSC(operand);
		this.functionName = functionName;
		operand.setReferencingRSC(this);
	}

	public SQLConstruct getOperand() {
		return this.getRSCRepository().getRSC(operandIndex);
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(functionName).append(SQLGrammarTokens.LEFT_PAREN).append(getOperand().toSql())
				.append(SQLGrammarTokens.RIGHT_PAREN);

		return sql.toString();
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.USER_DEFINED_FUNCTION;
	}

	@Override
	public SQLUserDefinedFunction withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLUserDefinedFunction withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLUserDefinedFunction deepClone() {
		return new SQLUserDefinedFunction(this.functionName, getOperand().deepClone()).withCloneId(
				SQLConstructIdentifierManager.getRSCCloneIdentifier(this)).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLUserDefinedFunction deepCloneWithPreservedIdentifier() {
		return new SQLUserDefinedFunction(this.functionName, getOperand().deepCloneWithPreservedIdentifier())
				.withCloneId(this.getIdentifier());
	}

	@Override
	public SQLUserDefinedFunction withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
