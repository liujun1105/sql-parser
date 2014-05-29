/**
 * xAdd Project
 * 
 * @author jun 
 * @since 27 Mar 2014
 */
package ie.epstvxj.sql.value;

import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;

/**
 * @author jun
 *
 */
public class SQLOperator extends SQLValue {

	private String	operator;

	public SQLOperator() {

	}

	public SQLOperator(final String operator) {
		withOperator(operator);
	}

	private void withOperator(final String operator) {
		this.operator = operator;
	}

	@Override
	public String toSql() {
		return this.operator;
	}

	@Override
	public SQLOperator withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLOperator withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.OPERATOR;
	}

	@Override
	public String getValue() {
		return operator;
	}

	@Override
	public SQLOperator deepClone() {
		return new SQLOperator(operator).withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLOperator deepCloneWithPreservedIdentifier() {
		return new SQLOperator(operator).withCloneId(this.getIdentifier()).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLOperator withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
