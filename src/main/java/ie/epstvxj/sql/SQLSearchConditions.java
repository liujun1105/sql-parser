package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;

/**
 * @author jun
 *
 */
public class SQLSearchConditions extends AbstractSQLConstruct {

	private int		rootIndex	= -1;
	private boolean	withParenthesis;

	public SQLSearchConditions() {

	}

	public SQLConstruct getRoot() {
		return this.getRSCRepository().getRSC(rootIndex);
	}

	public boolean hasCondition() {
		return rootIndex != -1;
	}

	public void withParenthesis(final boolean withParenthesis) {
		this.withParenthesis = withParenthesis;
	}

	public SQLSearchConditions withCondition(final SQLConstruct rsc) {
		this.rootIndex = this.getRSCRepository().addRSC(rsc);
		rsc.setReferencingRSC(this);
		return this;
	}

	public SQLSearchConditions and(final SQLConstruct rsc) {

		if (-1 != rootIndex) {
			getRoot().removeReferencingRSC();
		}
		SQLLogicalAND and = SQLBuilder.build().and().withLeftOperand(getRoot()).withRightOperand(rsc)
				.withSQLQueryContext(getRoot().getSQLQueryContext());

		rootIndex = this.getRSCRepository().addRSC(and);
		and.setReferencingRSC(this);

		return this;
	}

	public SQLSearchConditions andWithParen(final SQLConstruct rsc) {

		if (-1 != rootIndex) {
			getRoot().removeReferencingRSC();
		}

		SQLLogicalAND and = SQLBuilder.build().and().withLeftOperand(getRoot()).withRightOperand(rsc)
				.withSQLQueryContext(getRoot().getSQLQueryContext());
		rootIndex = this.getRSCRepository().addRSC(and);
		and.withParenthesis(true);
		and.setReferencingRSC(this);

		return this;
	}

	public SQLSearchConditions or(final SQLConstruct rsc) {

		getRoot().removeReferencingRSC();

		SQLLogicalOR or = SQLBuilder.build().or().withLeftOperand(getRoot()).withRightOperand(rsc)
				.withSQLQueryContext(getRoot().getSQLQueryContext());
		rootIndex = this.getRSCRepository().addRSC(or);
		or.setReferencingRSC(this);

		return this;
	}

	public SQLSearchConditions orWithParen(final SQLConstruct rsc) {

		getRoot().removeReferencingRSC();

		SQLLogicalOR or = SQLBuilder.build().or().withLeftOperand(getRoot()).withRightOperand(rsc)
				.withSQLQueryContext(getRoot().getSQLQueryContext());
		rootIndex = this.getRSCRepository().addRSC(or);
		or.withParenthesis(true);
		or.setReferencingRSC(this);

		return this;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.SEARCH_COND;
	}

	@Override
	public SQLConstructType getActualType() {
		return this.getGeneralType();
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		if (withParenthesis) {
			sql.append(SQLGrammarTokens.LEFT_PAREN);
		}
		sql.append(getRoot().toSql());
		if (withParenthesis) {
			sql.append(SQLGrammarTokens.RIGHT_PAREN);
		}
		return sql.toString();
	}

	@Override
	public SQLSearchConditions withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLSearchConditions withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLSearchConditions deepClone() {
		SQLSearchConditions cloneConditions = new SQLSearchConditions().withCloneId(
				SQLConstructIdentifierManager.getRSCCloneIdentifier(this)).withSQLQueryContext(this.sqlQueryContext);

		cloneConditions.withCondition(getRoot().deepClone()).withParenthesis(this.withParenthesis);

		return cloneConditions;
	}

	@Override
	public SQLSearchConditions deepCloneWithPreservedIdentifier() {
		SQLSearchConditions cloneConditions = new SQLSearchConditions().withCloneId(this.getIdentifier())
				.withSQLQueryContext(this.sqlQueryContext);

		cloneConditions.withCondition(getRoot().deepCloneWithPreservedIdentifier()).withParenthesis(
				this.withParenthesis);

		return cloneConditions;
	}

	@Override
	public SQLSearchConditions withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
