/**
 * xAdd Project
 * 
 * @author jun 
 * @since 11 Mar 2014
 */
package ie.epstvxj.sql.value;

import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * @author jun
 *
 */
public class SQLDateValue extends SQLValue {

	private final Logger	LOG	= Logger.getLogger(SQLDateValue.class);

	private Date			date;
	private DateFormat		df	= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	public SQLDateValue() {

	}

	public SQLDateValue(final Date date) {
		this.date = date;
	}

	public SQLDateValue(final long valueInMillionSeconds) {
		this.date = new Date(valueInMillionSeconds);
	}

	public SQLDateValue withDateValue(final String dateInString, final String pattern) {
		try {
			df = new SimpleDateFormat(pattern);
			this.date = df.parse(dateInString);
		} catch (ParseException e) {
			LOG.error(e.getMessage());
		}
		return this;
	}

	public SQLDateValue withDateValue(final Date date) {
		this.date = date;
		return this;
	}

	public SQLDateValue withDateFormat(final String format) {
		df = new SimpleDateFormat(format);
		return this;
	}

	public SQLDateValue withDateFormat(final DateFormat df) {
		this.df = df;
		return this;
	}

	@Override
	public Date getValue() {
		return this.date;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.DATE_VALUE;
	}

	@Override
	public String toSql() {
		return "'" + df.format(date) + "'";
	}

	@Override
	public SQLDateValue withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLDateValue withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLDateValue deepClone() {
		return new SQLDateValue().withDateFormat(df).withDateValue(new Date(date.getTime()))
				.withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLDateValue deepCloneWithPreservedIdentifier() {
		return new SQLDateValue().withDateFormat(df).withDateValue(new Date(date.getTime()))
				.withCloneId(this.getIdentifier()).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLDateValue withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
