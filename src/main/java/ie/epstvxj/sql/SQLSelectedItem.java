package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;

/**
 * @author jun
 *
 */
public class SQLSelectedItem extends AbstractSQLConstruct {

	private int	selectedItemIndex		= -1;
	private int	correlationNameIndex	= -1;

	public SQLSelectedItem(final SQLConstruct selectedItem) {
		withSelectedItem(selectedItem);
	}

	public SQLSelectedItem withSelectedItem(final SQLConstruct selectedItem) {
		this.selectedItemIndex = this.getRSCRepository().addRSC(selectedItem);
		selectedItem.setReferencingRSC(this);
		return this;
	}

	public SQLSelectedItem withCorrelationName(final SQLCorrelationName correlationName) {
		this.correlationNameIndex = this.getRSCRepository().addRSC(correlationName);
		correlationName.setReferencingRSC(this);
		return this;
	}

	public boolean haveCorrelationName() {
		return -1 != correlationNameIndex;
	}

	public String getCorrelationName() {
		return this.getRSCRepository().getRSC(correlationNameIndex).toSql();
	}

	public SQLCorrelationName getRSCCorrelationName() {
		return (SQLCorrelationName) this.getRSCRepository().getRSC(correlationNameIndex);
	}

	public SQLConstruct getSelectedItem() {
		return this.getRSCRepository().getRSC(selectedItemIndex);
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.SELECTED_ITEM;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(getSelectedItem().toSql());

		if (-1 != correlationNameIndex) {
			sql.append(SQLGrammarTokens.SPACE).append(SQLGrammarTokens.AS).append(SQLGrammarTokens.SPACE)
					.append(getRSCCorrelationName().toSql());
		}

		return sql.toString();
	}

	@Override
	public SQLSelectedItem withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLSelectedItem withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLSelectedItem deepClone() {
		return new SQLSelectedItem(getSelectedItem().deepClone())
				.withCorrelationName(getRSCCorrelationName().deepClone())
				.withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this))
				.withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLConstruct deepCloneWithPreservedIdentifier() {
		return new SQLSelectedItem(getSelectedItem().deepCloneWithPreservedIdentifier())
				.withCorrelationName(getRSCCorrelationName().deepCloneWithPreservedIdentifier())
				.withCloneId(this.getIdentifier()).withSQLQueryContext(this.sqlQueryContext);
	}

	@Override
	public SQLSelectedItem withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
