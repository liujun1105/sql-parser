/**
 * xAdd Project
 * 
 * @author jun 
 * @since 11 Mar 2014
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
public class SQLBetweenFunction extends SQLFunction {

	private int			predicandIndex	= -1;
	private int			predicateIndex1	= -1;
	private int			predicateIndex2	= -1;

	private BetweenType	betweenType;
	private boolean		isNotBetween;

	public SQLBetweenFunction() {

	}

	public SQLBetweenFunction(final SQLConstruct predicand, final SQLConstruct predicate1, final SQLConstruct predicate2) {
		withPredicand(predicand);
		withPredicate1(predicate1);
		withPredicate2(predicate2);
	}

	public SQLBetweenFunction(final SQLConstruct predicand, final SQLConstruct predicate1,
			final SQLConstruct predicate2, final BetweenType betweenType) {
		this(predicand, predicate1, predicate2);
		this.betweenType = betweenType;
	}

	public SQLBetweenFunction asymmetric() {
		this.betweenType = BetweenType.ASYMMETRIC;
		return this;
	}

	public SQLBetweenFunction symmetric() {
		this.betweenType = BetweenType.SYMMETRIC;
		return this;
	}

	public boolean isAsymmetric() {
		return this.betweenType == BetweenType.ASYMMETRIC;
	}

	public boolean isSymmetric() {
		return this.betweenType == BetweenType.SYMMETRIC;
	}

	public SQLBetweenFunction between(final SQLConstruct predicate1, final SQLConstruct predicate2) {
		withPredicate1(predicate1);
		withPredicate2(predicate2);
		return this;
	}

	public SQLBetweenFunction notBetween(final SQLConstruct predicate1, final SQLConstruct predicate2) {
		withPredicate1(predicate1);
		withPredicate2(predicate2);
		this.isNotBetween = true;
		return this;
	}

	@Override
	public SQLBetweenFunction withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLBetweenFunction withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(getPredicand().toSql()).append(SQLGrammarTokens.SPACE);
		if (isNotBetween) {
			sql.append(SQLGrammarTokens.NOT).append(SQLGrammarTokens.SPACE);
		}
		sql.append(SQLGrammarTokens.BETWEEN).append(SQLGrammarTokens.SPACE);
		if (null != betweenType) {
			if (isAsymmetric()) {
				sql.append(SQLGrammarTokens.ASYMMETRIC).append(SQLGrammarTokens.SPACE);
			} else {
				sql.append(SQLGrammarTokens.SYMMETRIC).append(SQLGrammarTokens.SPACE);
			}
		}
		sql.append(getPredicate1().toSql()).append(SQLGrammarTokens.SPACE);
		sql.append(SQLGrammarTokens.AND).append(SQLGrammarTokens.SPACE);
		sql.append(getPredicate2().toSql()).append(SQLGrammarTokens.SPACE);

		return sql.toString();
	}

	public void setBetweenType(final BetweenType betweenType) {
		this.betweenType = betweenType;
	}

	public BetweenType getBetweenType() {
		return betweenType;
	}

	public SQLConstruct getPredicand() {
		return this.getRSCRepository().getRSC(predicandIndex);
	}

	public SQLBetweenFunction withPredicand(final SQLConstruct predicand) {
		this.predicandIndex = this.getRSCRepository().addRSC(predicand);
		predicand.setReferencingRSC(this);
		return this;
	}

	public SQLConstruct getPredicate1() {
		return this.getRSCRepository().getRSC(predicateIndex1);
	}

	public SQLBetweenFunction withPredicate1(final SQLConstruct predicate1) {
		this.predicateIndex1 = this.getRSCRepository().addRSC(predicate1);
		predicate1.setReferencingRSC(this);
		return this;
	}

	public SQLConstruct getPredicate2() {
		return this.getRSCRepository().getRSC(predicateIndex2);
	}

	public SQLBetweenFunction withPredicate2(final SQLConstruct predicate2) {
		this.predicateIndex2 = this.getRSCRepository().addRSC(predicate2);
		predicate2.setReferencingRSC(this);
		return this;
	}

	public boolean isNotBetween() {
		return isNotBetween;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.FUNCTION;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.BETWEEN_FUNCTION;
	}

	@Override
	public SQLBetweenFunction deepClone() {
		SQLBetweenFunction cloneBetweenFunc = new SQLBetweenFunction().withCloneId(
				SQLConstructIdentifierManager.getRSCCloneIdentifier(this)).withSQLQueryContext(this.sqlQueryContext);

		if (isNotBetween) {
			cloneBetweenFunc.notBetween(getPredicate1().deepClone(), getPredicate2().deepClone()).withPredicand(
					getPredicand().deepClone());
		} else {
			cloneBetweenFunc.between(getPredicate1().deepClone(), getPredicate2().deepClone()).withPredicand(
					getPredicand().deepClone());
		}

		if (null != betweenType) {
			if (isAsymmetric()) {
				cloneBetweenFunc.asymmetric();
			} else {
				cloneBetweenFunc.symmetric();
			}
		}

		return cloneBetweenFunc;
	}

	@Override
	public SQLBetweenFunction deepCloneWithPreservedIdentifier() {
		SQLBetweenFunction cloneBetweenFunc = new SQLBetweenFunction().withCloneId(this.getIdentifier())
				.withSQLQueryContext(this.sqlQueryContext);

		if (isNotBetween) {
			cloneBetweenFunc.notBetween(getPredicate1().deepCloneWithPreservedIdentifier(),
					getPredicate2().deepCloneWithPreservedIdentifier()).withPredicand(
					getPredicand().deepCloneWithPreservedIdentifier());
		} else {
			cloneBetweenFunc.between(getPredicate1().deepCloneWithPreservedIdentifier(),
					getPredicate2().deepCloneWithPreservedIdentifier()).withPredicand(
					getPredicand().deepCloneWithPreservedIdentifier());
		}

		if (null != betweenType) {
			if (isAsymmetric()) {
				cloneBetweenFunc.asymmetric();
			} else {
				cloneBetweenFunc.symmetric();
			}
		}

		return cloneBetweenFunc;
	}

	@Override
	public SQLBetweenFunction withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
