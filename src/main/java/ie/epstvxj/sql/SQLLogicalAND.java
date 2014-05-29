package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.function.SQLFunction;

/**
 * @author jun
 *
 */
public class SQLLogicalAND extends SQLFunction {

	private int		leftOperandIndex	= -1;
	private int		rightOperandIndex	= -1;

	private boolean	withParenthesis;

	public SQLLogicalAND() {

	}

	public SQLLogicalAND(final SQLConstruct leftOperand, final SQLConstruct rightOperand) {
		withLeftOperand(leftOperand);
		withRightOperand(rightOperand);
	}

	public SQLLogicalAND withLeftOperand(final SQLConstruct leftOperand) {
		this.leftOperandIndex = this.getRSCRepository().addRSC(leftOperand);
		leftOperand.setReferencingRSC(this);

		return this;
	}

	public SQLLogicalAND withRightOperand(final SQLConstruct rightOperand) {
		this.rightOperandIndex = this.getRSCRepository().addRSC(rightOperand);
		rightOperand.setReferencingRSC(this);
		return this;
	}

	public SQLLogicalAND withParenthesis(final boolean withParenthesis) {
		this.withParenthesis = withParenthesis;
		return this;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.SEARCH_COND;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.SEARCH_COND_AND;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		if (withParenthesis) {
			sql.append(SQLGrammarTokens.LEFT_PAREN);
		}

		sql.append(getLeftOperand().toSql()).append(SQLGrammarTokens.SPACE);
		sql.append(SQLGrammarTokens.AND).append(SQLGrammarTokens.SPACE);
		sql.append(getRightOperand().toSql());

		if (withParenthesis) {
			sql.append(SQLGrammarTokens.RIGHT_PAREN);
		}

		return sql.toString();
	}

	public SQLConstruct getLeftOperand() {
		return getRSCRepository().getRSC(leftOperandIndex);
	}

	public SQLConstruct getRightOperand() {
		return getRSCRepository().getRSC(rightOperandIndex);
	}

	public boolean isWithParenthesis() {
		return this.withParenthesis;
	}

	@Override
	public SQLLogicalAND withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLLogicalAND withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLLogicalAND deepClone() {
		return new SQLLogicalAND(getLeftOperand().deepClone(), getRightOperand().deepClone())
				.withParenthesis(withParenthesis).withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLLogicalAND deepCloneWithPreservedIdentifier() {
		return new SQLLogicalAND(getLeftOperand().deepCloneWithPreservedIdentifier(), getRightOperand()
				.deepCloneWithPreservedIdentifier()).withParenthesis(withParenthesis).withCloneId(this.getIdentifier())
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLLogicalAND withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
