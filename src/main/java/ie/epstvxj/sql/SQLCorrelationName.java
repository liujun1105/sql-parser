/**
 * xAdd Project
 * 
 * @author jun 
 * @since 14 Mar 2014
 */
package ie.epstvxj.sql;

/**
 * @author jun
 *
 */
public class SQLCorrelationName extends AbstractSQLConstruct {

	private String	correlationName;

	public SQLCorrelationName(final String correlationName) {
		this.setCorrelationName(correlationName);
	}

	public void setCorrelationName(final String correlationName) {
		this.correlationName = correlationName;
	}

	public String getName() {
		return this.correlationName;
	}

	@Override
	public String toSql() {
		return correlationName;
	}

	@Override
	public SQLCorrelationName deepClone() {
		return new SQLCorrelationName(correlationName).withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLCorrelationName deepCloneWithPreservedIdentifier() {
		return new SQLCorrelationName(correlationName).withCloneId(this.getIdentifier()).withSQLQueryContext(
				this.sqlQueryContext);
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.CORRELATION_NAME;
	}

	@Override
	public SQLConstructType getActualType() {
		return this.getGeneralType();
	}

	@Override
	public SQLCorrelationName withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLCorrelationName withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLCorrelationName withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
