/**
 * xAdd Project
 * 
 * @author jun 
 * @since 13 Mar 2014
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
public class SQLExistsFunction extends SQLFunction {

	private int	subQueryIndex	= -1;

	public SQLExistsFunction() {

	}

	public SQLExistsFunction(final SQLConstruct subquery) {
		withSubQuery(subquery);
	}

	public SQLExistsFunction withSubQuery(final SQLConstruct subquery) {
		this.subQueryIndex = this.getRSCRepository().addRSC(subquery);
		subquery.setReferencingRSC(this);
		return this;
	}

	public SQLConstruct getSubquery() {
		return this.getRSCRepository().getRSC(subQueryIndex);
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(SQLGrammarTokens.EXISTS).append(SQLGrammarTokens.SPACE);
		sql.append(getSubquery().toSql());

		return sql.toString();
	}

	@Override
	public SQLExistsFunction withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLExistsFunction withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.FUNCTION;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.EXISTS_FUNCTION;
	}

	@Override
	public SQLExistsFunction deepClone() {
		return new SQLExistsFunction(getSubquery().deepClone()).withCloneId(
				SQLConstructIdentifierManager.getRSCCloneIdentifier(this)).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLExistsFunction deepCloneWithPreservedIdentifier() {
		return new SQLExistsFunction(getSubquery().deepClone()).withCloneId(this.getIdentifier()).withSQLQueryContext(
				this.sqlQueryContext);
	}

	@Override
	public SQLExistsFunction withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
