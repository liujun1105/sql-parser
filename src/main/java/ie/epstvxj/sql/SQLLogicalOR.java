package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.function.SQLFunction;

/**
 * @author jun
 *
 */
public class SQLLogicalOR extends SQLFunction {

	private int		leftOperandIndex	= -1;
	private int		rightOperandIndex	= -1;
	private boolean	withParenthesis;

	public SQLLogicalOR() {

	}

	public SQLLogicalOR(final SQLConstruct leftOperand, final SQLConstruct rightOperand) {
		withLeftOperand(leftOperand);
		withRightOperand(rightOperand);
	}

	public SQLLogicalOR withLeftOperand(final SQLConstruct leftOperand) {
		this.leftOperandIndex = this.getRepository().addSQLConstruct(leftOperand);
		leftOperand.setReferencingConstruct(this);
		return this;
	}

	public SQLLogicalOR withRightOperand(final SQLConstruct rightOperand) {
		this.rightOperandIndex = this.getRepository().addSQLConstruct(rightOperand);
		rightOperand.setReferencingConstruct(this);
		return this;
	}

	public SQLLogicalOR withParenthesis(final boolean withParenthesis) {
		this.withParenthesis = withParenthesis;
		return this;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		if (withParenthesis) {
			sql.append(SQLGrammarTokens.LEFT_PAREN);
		}

		sql.append(getLeftOperand().toSql()).append(SQLGrammarTokens.SPACE);
		sql.append(SQLGrammarTokens.OR).append(SQLGrammarTokens.SPACE);
		sql.append(getRightOperand().toSql());

		if (withParenthesis) {
			sql.append(SQLGrammarTokens.RIGHT_PAREN);
		}

		return sql.toString();
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.SEARCH_COND;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.SEARCH_COND_OR;
	}

	public SQLConstruct getLeftOperand() {
		return getRepository().getSQLConstruct(leftOperandIndex);
	}

	public SQLConstruct getRightOperand() {
		return getRepository().getSQLConstruct(rightOperandIndex);
	}

	public boolean isWithParenthesis() {
		return this.withParenthesis;
	}

	@Override
	public SQLLogicalOR withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLLogicalOR withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLLogicalOR deepClone() {
		return new SQLLogicalOR(getLeftOperand().deepClone(), getRightOperand().deepClone())
				.withParenthesis(withParenthesis).withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLLogicalOR deepCloneWithPreservedIdentifier() {
		return new SQLLogicalOR(getLeftOperand().deepCloneWithPreservedIdentifier(), getRightOperand()
				.deepCloneWithPreservedIdentifier()).withParenthesis(withParenthesis).withCloneId(this.getIdentifier())
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLLogicalOR withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
