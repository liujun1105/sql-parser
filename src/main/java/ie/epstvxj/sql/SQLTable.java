/**
 * xAdd Project
 * 
 * @author jun 
 * @since 10 Mar 2014
 */
package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jun
 *
 */
public class SQLTable extends AbstractSQLConstruct implements SQLTargetResource {

	private String				tableName;
	private int					correlationNameIndex	= -1;
	private final List<Integer>	joinIdentifierList		= new ArrayList<Integer>();

	/**
	 * Optional field
	 */
	private String				schemaName;

	public SQLTable() {
	}

	public SQLTable(final String tableName, final SQLCorrelationName correlationName) {
		withTableName(tableName);
		withCorrelationName(correlationName);
	}

	@Override
	public SQLCorrelationName getTableCorrelation() {
		return (SQLCorrelationName) this.getRSCRepository().getRSC(correlationNameIndex);
	}

	@Override
	public String getTableCorrelationName() {
		if (null != getTableCorrelation()) {
			return getTableCorrelation().getName();
		}
		return null;
	}

	@Override
	public List<SQLJoin> getJoins() {
		List<SQLJoin> joinList = new ArrayList<SQLJoin>();
		for (Integer joinIndex : joinIdentifierList) {
			joinList.add((SQLJoin) this.getRSCRepository().getRSC(joinIndex));
		}
		return joinList;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.TARGET_RESOURCE;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.TABLE;
	}

	@Override
	public SQLTable withJoin(final SQLJoin join) {
		this.joinIdentifierList.add(this.getRSCRepository().addRSC(join));
		join.setReferencingRSC(this);
		return this;
	}

	@Override
	public SQLTable withCorrelationName(final SQLCorrelationName correlationName) {
		this.correlationNameIndex = this.getRSCRepository().addRSC(correlationName);
		correlationName.setReferencingRSC(this);
		return this;
	}

	public SQLTable withSchemaName(final String schemaName) {
		this.schemaName = schemaName;
		return this;
	}

	public String getSchemaName() {
		if (null != this.schemaName) {
			return this.schemaName;
		}
		return null;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		if (null != this.schemaName) {
			sql.append(this.schemaName).append(SQLGrammarTokens.DOT);
		}

		sql.append(tableName);

		if (null != getTableCorrelation()) {
			sql.append(SQLGrammarTokens.SPACE);
			sql.append(SQLGrammarTokens.AS).append(SQLGrammarTokens.SPACE);
			sql.append(getTableCorrelation().toSql());
		}

		int numOfJoins = getJoins().size();
		if (numOfJoins > 0) {
			for (SQLJoin join : getJoins()) {
				sql.append(SQLGrammarTokens.SPACE);
				sql.append(join.toSql());
			}
		}

		return sql.toString();
	}

	public SQLTable withTableName(final String tableName) {
		this.tableName = tableName;
		return this;
	}

	public String getTableName() {
		return tableName;
	}

	@Override
	public SQLTable withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLTable withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLTable deepClone() {
		SQLTable cloneTable = new SQLTable().withTableName(getTableName()).withSQLQueryContext(this.sqlQueryContext);

		if (null != this.schemaName) {
			cloneTable.withSchemaName(this.schemaName);
		}

		if (null != this.getTableCorrelation()) {
			cloneTable.withCorrelationName(getTableCorrelation().deepClone());
		}
		cloneTable.withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this));

		for (SQLJoin join : getJoins()) {
			cloneTable.withJoin(join.deepClone());
		}

		return cloneTable;
	}

	@Override
	public SQLTable deepCloneWithPreservedIdentifier() {
		SQLTable cloneTable = new SQLTable().withTableName(getTableName()).withSQLQueryContext(this.sqlQueryContext);

		if (null != this.schemaName) {
			cloneTable.withSchemaName(this.schemaName);
		}

		if (null != this.getTableCorrelation()) {
			cloneTable.withCorrelationName(getTableCorrelation().deepCloneWithPreservedIdentifier());
		}
		cloneTable.withCloneId(this.getIdentifier());

		for (SQLJoin join : getJoins()) {
			cloneTable.withJoin(join.deepCloneWithPreservedIdentifier());
		}

		return cloneTable;
	}

	@Override
	public SQLTable withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
