/**
 * xAdd Project
 * 
 * @author jun 
 * @since 18 Mar 2014
 */
package ie.epstvxj.sql.value;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.SQLColumn;
import ie.epstvxj.sql.SQLConstruct;
import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;
import ie.epstvxj.sql.SQLQueryContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jun
 *
 */
public class SQLListValue extends SQLValue {

	private final List<Integer>	valueIndexList;

	public SQLListValue() {
		this.valueIndexList = new ArrayList<Integer>();
	}

	public SQLListValue(final SQLConstruct... values) {
		this();
		withValues(values);
	}

	public int size() {
		return valueIndexList.size();
	}

	public SQLListValue withValue(final SQLConstruct value) {
		this.valueIndexList.add(this.getRSCRepository().addRSC(value));
		value.setReferencingRSC(this);
		return this;
	}

	public SQLListValue withValues(final SQLConstruct... values) {
		for (SQLConstruct value : values) {
			withValue(value);
		}
		return this;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		if (null != valueIndexList) {
			int size = size();
			sql.append(SQLGrammarTokens.LEFT_PAREN);
			for (int i = 0; i < size; i++) {
				SQLConstruct value = this.getRSCRepository().getRSC(valueIndexList.get(i));
				sql.append(value.toSql());

				if (i + 1 != size) {
					sql.append(SQLGrammarTokens.COMMA).append(SQLGrammarTokens.SPACE);
				}
			}
			sql.append(SQLGrammarTokens.RIGHT_PAREN);
		}

		return sql.toString();
	}

	@Override
	public List<SQLConstruct> getValue() {
		List<SQLConstruct> list = new ArrayList<SQLConstruct>();
		for (Integer valueIndex : valueIndexList) {
			SQLConstruct value = this.getRSCRepository().getRSC(valueIndex);
			list.add(value);
		}
		return list;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.LIST_VALUE;
	}

	@Override
	public SQLListValue withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLListValue withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	public SQLValue[] toValues() {
		SQLValue[] array = new SQLValue[size()];

		int index = 0;

		for (Integer valueIndex : valueIndexList) {
			SQLConstruct value = this.getRSCRepository().getRSC(valueIndex);
			array[index++] = (SQLValue) value;
		}

		return array;
	}

	public SQLColumn[] toColumns() {
		SQLColumn[] array = new SQLColumn[size()];

		int index = 0;
		for (Integer valueIndex : valueIndexList) {
			SQLConstruct value = this.getRSCRepository().getRSC(valueIndex);
			array[index++] = (SQLColumn) value;
		}

		return array;
	}

	@Override
	public SQLListValue deepClone() {
		SQLListValue clone = new SQLListValue().withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);

		for (Integer valueIndex : valueIndexList) {
			SQLConstruct value = this.getRSCRepository().getRSC(valueIndex);
			clone.withValue(value.deepClone());
		}

		return clone;
	}

	@Override
	public SQLListValue deepCloneWithPreservedIdentifier() {
		SQLListValue clone = new SQLListValue().withCloneId(this.getIdentifier()).withSQLQueryContext(
				this.sqlQueryContext);

		for (Integer valueIndex : valueIndexList) {
			SQLConstruct value = this.getRSCRepository().getRSC(valueIndex);
			clone.withValue(value.deepCloneWithPreservedIdentifier());
		}

		return clone;
	}

	@Override
	public SQLListValue withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
