package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;
import ie.epstvxj.sql.value.SQLValue;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author jun
 *
 */
public class SQLInsert extends AbstractSQLConstruct {

	private static Logger				LOG					= Logger.getLogger(SQLInsert.class);

	private int							tableIndex			= -1;

	private final List<Integer>			columnIndexList		= new ArrayList<Integer>();
	private final List<List<Integer>>	multiRowIndexList	= new ArrayList<List<Integer>>();
	private int							multiRowIndex		= -1;

	private Integer						embeddedQueryIndex	= -1;

	public SQLInsert intoTable(final SQLTable table) {
		this.tableIndex = getRSCRepository().addRSC(table);
		table.setReferencingRSC(this);
		return this;
	}

	public SQLTable getTable() {
		return (SQLTable) getRSCRepository().getRSC(tableIndex);
	}

	public SQLInsert withNewRow() {
		multiRowIndex++;
		multiRowIndexList.add(new ArrayList<Integer>());
		return this;
	}

	public SQLInsert withNewRowOfValues(final SQLValue... values) {
		withNewRow();
		withValues(values);
		return this;
	}

	public SQLInsert withValue(final SQLValue value) {

		if (multiRowIndex == -1 || null == multiRowIndexList.get(multiRowIndex)) {
			withNewRow();
		}

		multiRowIndexList.get(multiRowIndex).add(this.getRSCRepository().addRSC(value));
		value.setReferencingRSC(this);

		return this;
	}

	public SQLInsert withValues(final SQLValue... values) {

		if (multiRowIndex == -1 || null == multiRowIndexList.get(multiRowIndex)) {
			withNewRow();
		}

		for (SQLValue value : values) {
			withValue(value);
		}

		return this;
	}

	public SQLInsert withColumn(final SQLColumn column) {
		columnIndexList.add(getRSCRepository().addRSC(column));
		column.setReferencingRSC(this);
		return this;
	}

	public SQLInsert withColumns(final SQLColumn... columns) {

		for (SQLColumn column : columns) {
			withColumn(column);
		}

		return this;
	}

	public List<SQLColumn> getColumns() {

		List<SQLColumn> columnList = new ArrayList<SQLColumn>();

		for (Integer index : columnIndexList) {
			columnList.add((SQLColumn) this.getRSCRepository().getRSC(index));
		}
		return columnList;
	}

	/**
	 * Returns value position based on column index
	 * @param columnIndex
	 * @return
	 */
	public int getValuePosition(final SQLColumn column) {
		return columnIndexList.indexOf(column.getIndex());
	}

	public int getNumberOfRows() {
		return this.multiRowIndex + 1;
	}

	public List<SQLValue> getValues(final int index) {

		List<SQLValue> valueList = new ArrayList<SQLValue>();

		List<Integer> valueIndexList = multiRowIndexList.get(index);

		for (Integer valueIndex : valueIndexList) {
			valueList.add((SQLValue) this.getRSCRepository().getRSC(valueIndex));
		}

		return valueList;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		if (-1 == tableIndex) {
			LOG.error("table is not set for the insert clause");
		} else {

			sql.append(SQLGrammarTokens.INSERT).append(SQLGrammarTokens.SPACE);
			sql.append(SQLGrammarTokens.INTO).append(SQLGrammarTokens.SPACE);
			sql.append(getTable().toSql()).append(SQLGrammarTokens.SPACE);

			int columnSize = columnIndexList.size();

			if (columnSize > 0) {
				List<SQLColumn> columns = getColumns();
				sql.append(SQLGrammarTokens.LEFT_PAREN);
				for (int i = 0; i < columnSize; i++) {
					sql.append(columns.get(i).toSql());
					if (i + 1 != columnSize) {
						sql.append(SQLGrammarTokens.COMMA);
						sql.append(SQLGrammarTokens.SPACE);
					}
				}
				sql.append(SQLGrammarTokens.RIGHT_PAREN);
				sql.append(SQLGrammarTokens.SPACE);
			}

			if (-1 != embeddedQueryIndex) {
				sql.append(getEmbeddedQuery().toSql());
			} else {
				sql.append(SQLGrammarTokens.VALUES).append(SQLGrammarTokens.SPACE);

				for (int i = 0; i <= multiRowIndex; i++) {

					if (i > 0) {
						sql.append(SQLGrammarTokens.COMMA);
						sql.append(SQLGrammarTokens.SPACE);
					}

					sql.append(SQLGrammarTokens.LEFT_PAREN);
					List<SQLValue> valueList = getValues(i);

					int valueSize = valueList.size();

					for (int j = 0; j < valueSize; j++) {
						sql.append(valueList.get(j).toSql());
						if (j + 1 != valueSize) {
							sql.append(SQLGrammarTokens.COMMA);
							sql.append(SQLGrammarTokens.SPACE);
						}
					}
					sql.append(SQLGrammarTokens.RIGHT_PAREN);
				}
			}

		}
		return sql.toString();
	}

	@Override
	public SQLInsert withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLInsert withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	public SQLInsert withEmbeddedQuery(final SQLConstruct embeddedQuery) {
		this.embeddedQueryIndex = this.getRSCRepository().addRSC(embeddedQuery);
		embeddedQuery.setReferencingRSC(this);
		return this;
	}

	public SQLConstruct getEmbeddedQuery() {
		return this.getRSCRepository().getRSC(this.embeddedQueryIndex);
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.INSERT;
	}

	@Override
	public SQLInsert deepClone() {
		SQLInsert cloneInsert = new SQLInsert().withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);

		cloneInsert.intoTable(getTable().deepClone());

		List<SQLColumn> columnList = getColumns();
		for (SQLColumn column : columnList) {
			cloneInsert.withColumn(column.deepClone());
		}

		if (-1 != embeddedQueryIndex) {
			cloneInsert.withEmbeddedQuery(getEmbeddedQuery().deepClone());
		} else {
			for (int i = 0; i < multiRowIndex; i++) {
				List<SQLValue> row = getValues(i);
				cloneInsert.withNewRow();
				for (SQLValue value : row) {
					cloneInsert.withValue(value.deepClone());
				}
			}
		}

		return cloneInsert;
	}

	@Override
	public SQLInsert deepCloneWithPreservedIdentifier() {
		SQLInsert cloneInsert = new SQLInsert().withCloneId(this.getIdentifier()).withSQLQueryContext(
				this.sqlQueryContext);

		cloneInsert.intoTable(getTable().deepCloneWithPreservedIdentifier());

		List<SQLColumn> columnList = getColumns();
		for (SQLColumn column : columnList) {
			cloneInsert.withColumn(column.deepCloneWithPreservedIdentifier());
		}

		if (-1 != embeddedQueryIndex) {
			cloneInsert.withEmbeddedQuery(getEmbeddedQuery().deepCloneWithPreservedIdentifier());
		} else {
			for (int i = 0; i < multiRowIndex; i++) {
				List<SQLValue> row = getValues(i);
				cloneInsert.withNewRow();
				for (SQLValue value : row) {
					cloneInsert.withValue(value.deepCloneWithPreservedIdentifier());
				}
			}
		}
		return cloneInsert;
	}

	@Override
	public SQLInsert withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
