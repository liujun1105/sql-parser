/**
 * xAdd Project
 * 
 * @author jun 
 * @since 2 Apr 2014
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
public class SQLNullFunction extends SQLFunction {

	private int		operandIndex	= -1;
	private boolean	notNull;

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.FUNCTION;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.NULL_FUNCTION;
	}

	public SQLNullFunction withOperand(final SQLConstruct operand) {
		this.operandIndex = this.getRepository().addSQLConstruct(operand);
		operand.setReferencingConstruct(this);
		return this;
	}

	public SQLConstruct getOperand() {
		return this.getRepository().getSQLConstruct(operandIndex);
	}

	public SQLNullFunction notNull() {
		this.notNull = true;
		return this;
	}

	@Override
	public SQLNullFunction withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLNullFunction withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		if (-1 != operandIndex) {
			sql.append(getOperand().toSql()).append(SQLGrammarTokens.SPACE).append(SQLGrammarTokens.IS);
			if (notNull) {
				sql.append(SQLGrammarTokens.SPACE).append(SQLGrammarTokens.NOT).append(SQLGrammarTokens.SPACE).append(SQLGrammarTokens.NULL);
			} else {
				sql.append(SQLGrammarTokens.SPACE).append(SQLGrammarTokens.NULL);
			}
		}

		return sql.toString();

	}

	@Override
	public SQLNullFunction deepClone() {
		SQLNullFunction nullFunc = new SQLNullFunction().withCloneId(
				SQLConstructIdentifierManager.getRSCCloneIdentifier(this)).withSQLQueryContext(this.sqlQueryContext);
		nullFunc.withOperand(getOperand().deepClone());
		if (notNull) {
			nullFunc.notNull();
		}
		return nullFunc;
	}

	@Override
	public SQLNullFunction deepCloneWithPreservedIdentifier() {
		SQLNullFunction nullFunc = new SQLNullFunction().withCloneId(this.getIdentifier()).withSQLQueryContext(
				this.sqlQueryContext);
		nullFunc.withOperand(getOperand().deepCloneWithPreservedIdentifier());
		if (notNull) {
			nullFunc.notNull();
		}
		return nullFunc;
	}

	@Override
	public SQLNullFunction withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}
}
