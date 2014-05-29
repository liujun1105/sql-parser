/**
 * xAdd Project
 * 
 * @author jun 
 * @since 3 Apr 2014
 */
package ie.epstvxj.sql.value;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;

/**
 * @author jun
 *
 */
public class SQLDynamicParameter extends SQLValue {

	@Override
	public String toSql() {
		return getValue();
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.DYNAMIC_PARAMETER;
	}

	@Override
	public SQLDynamicParameter withId(final int identifier) {
		this.identifier = getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLDynamicParameter withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public String getValue() {
		return SQLGrammarTokens.QUESTION_MARK;
	}

	@Override
	public SQLDynamicParameter deepClone() {
		return new SQLDynamicParameter().withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLDynamicParameter deepCloneWithPreservedIdentifier() {
		return new SQLDynamicParameter().withCloneId(this.getIdentifier()).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLDynamicParameter withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
