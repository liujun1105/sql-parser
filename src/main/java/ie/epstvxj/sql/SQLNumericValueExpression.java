package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.value.SQLOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jun
 *
 */
public class SQLNumericValueExpression extends AbstractSQLConstruct {

	private int					termIndex		= -1;
	private final List<Integer>	signIndexList	= new ArrayList<Integer>();
	private int					aliasIndex		= -1;

	public SQLNumericValueExpression withTerm(final SQLConstruct term) {
		if (-1 == this.termIndex) {
			this.termIndex = this.getRSCRepository().addRSC(term);
			term.setReferencingRSC(this);
		} else {
			this.addSubRSC(term);
		}

		return this;
	}

	public SQLNumericValueExpression withSign(final SQLOperator sign) {
		this.signIndexList.add(this.getRSCRepository().addRSC(sign));
		sign.setReferencingRSC(this);
		return this;
	}

	public SQLNumericValueExpression withAlias(final SQLCorrelationName correlationName) {
		this.aliasIndex = this.getRSCRepository().addRSC(correlationName);
		correlationName.setReferencingRSC(this);
		return this;
	}

	@Override
	public boolean isValidSubRSC(final SQLConstruct rsc) {
		if (rsc.getGeneralType() == SQLConstructType.COLUMN) {
			return true;
		} else if (rsc.getGeneralType() == SQLConstructType.VALUE) {
			return true;
		}

		return false;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.EXPRESSION;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.NUMERIC_VALUE_EXPRESSION;
	}

	@Override
	public SQLNumericValueExpression withId(final int identifier) {
		this.identifier = getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLNumericValueExpression withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		if (-1 != termIndex) {

			sql.append(this.getRSCRepository().getRSC(termIndex).toSql());

			int index = 0;
			for (int signIndex : signIndexList) {
				sql.append(SQLGrammarTokens.SPACE).append(getSign(signIndex).toSql()).append(SQLGrammarTokens.SPACE)
						.append(getSubRSCList().get(index++).toSql());
			}

			if (-1 != aliasIndex) {
				sql.append(SQLGrammarTokens.SPACE).append(SQLGrammarTokens.AS).append(SQLGrammarTokens.SPACE).append(getAlias().toSql());
			}
		}

		return sql.toString();
	}

	public SQLCorrelationName getAlias() {
		return (SQLCorrelationName) getRSCRepository().getRSC(aliasIndex);
	}

	public List<SQLConstruct> getTerms() {
		List<SQLConstruct> list = new ArrayList<SQLConstruct>();
		list.add(this.getRSCRepository().getRSC(termIndex));
		list.addAll(this.subRSCList);
		return list;
	}

	public SQLOperator getSign(final int index) {
		return (SQLOperator) getRSCRepository().getRSC(index);
	}

	public List<SQLOperator> getSigns() {
		List<SQLOperator> list = new ArrayList<SQLOperator>();
		for (Integer signIndex : signIndexList) {
			list.add((SQLOperator) getRSCRepository().getRSC(signIndex));
		}
		return list;
	}

	@Override
	public SQLConstruct deepClone() {
		SQLNumericValueExpression nve = new SQLNumericValueExpression().withCloneId(
				SQLConstructIdentifierManager.getRSCCloneIdentifier(this)).withSQLQueryContext(this.sqlQueryContext);

		if (-1 != termIndex) {
			nve.withTerm(this.getRSCRepository().getRSC(termIndex).deepClone());
		}

		for (SQLConstruct rsc : getSubRSCList()) {
			nve.withTerm(rsc.deepClone());
		}

		for (Integer signIndex : signIndexList) {
			nve.withSign(getSign(signIndex).deepClone());
		}

		if (-1 != aliasIndex) {
			nve.withAlias(getAlias().deepClone());
		}

		return nve;
	}

	@Override
	public SQLNumericValueExpression deepCloneWithPreservedIdentifier() {
		SQLNumericValueExpression nve = new SQLNumericValueExpression().withCloneId(this.getIdentifier())
				.withSQLQueryContext(this.sqlQueryContext);

		if (-1 != termIndex) {
			nve.withTerm(this.getRSCRepository().getRSC(termIndex).deepCloneWithPreservedIdentifier());
		}

		for (SQLConstruct rsc : getSubRSCList()) {
			nve.withTerm(rsc.deepCloneWithPreservedIdentifier());
		}

		for (Integer signIndex : signIndexList) {
			nve.withSign(getSign(signIndex).deepCloneWithPreservedIdentifier());
		}

		if (-1 != aliasIndex) {
			nve.withAlias(getAlias().deepCloneWithPreservedIdentifier());
		}

		return nve;
	}

	@Override
	public SQLNumericValueExpression withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}

}
