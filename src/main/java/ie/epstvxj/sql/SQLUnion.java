/**
 * SQL Parser
 * 
 * @author jun 
 * @since 30 May 2014
 */
package ie.epstvxj.sql;

import ie.epstvxj.parser.SQLGrammarTokens;

public class SQLUnion extends AbstractSQLConstruct {

	private int	unionConstructIndex1	= -1;
	private int	unionConstructIndex2	= -1;

	public SQLUnion unionConstructs(final SQLConstruct c1, final SQLConstruct c2) {
		withConstruct(c1);
		withConstruct(c2);
		return this;
	}

	public SQLUnion withConstruct(final SQLConstruct c) {
		if (-1 == unionConstructIndex1) {
			unionConstructIndex1 = this.getRepository().addSQLConstruct(c);
			c.setReferencingConstruct(this);
		} else {
			unionConstructIndex2 = this.getRepository().addSQLConstruct(c);
			c.setReferencingConstruct(this);
		}
		return this;
	}

	public SQLConstruct getUnionConstruct1() {
		return this.getRepository().getSQLConstruct(unionConstructIndex1);
	}

	public SQLConstruct getUnionConstruct2() {
		return this.getRepository().getSQLConstruct(unionConstructIndex2);
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		if (getSQLQueryContext() != SQLQueryContext.TOP_UNION) {
			sql.append(SQLGrammarTokens.LEFT_PAREN);
		}

		if (-1 != unionConstructIndex1 && -1 != unionConstructIndex2) {
			sql.append(getUnionConstruct1().toSql()).append(SQLGrammarTokens.SPACE);
			sql.append(SQLGrammarTokens.UNION).append(SQLGrammarTokens.SPACE);
			sql.append(getUnionConstruct2().toSql());
		}

		if (getSQLQueryContext() != SQLQueryContext.TOP_UNION) {
			sql.append(SQLGrammarTokens.RIGHT_PAREN);
		}

		return sql.toString();
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.UNION;
	}

	@Override
	public SQLUnion deepClone() {
		SQLUnion clonedUnion = new SQLUnion().unionConstructs(getUnionConstruct1().deepClone(), getUnionConstruct2()
				.deepClone());
		clonedUnion.withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this)).withSQLQueryContext(
				this.sqlQueryContext);
		return clonedUnion;
	}

	@Override
	public SQLUnion deepCloneWithPreservedIdentifier() {
		SQLUnion clonedUnion = new SQLUnion().unionConstructs(getUnionConstruct1().deepCloneWithPreservedIdentifier(),
				deepCloneWithPreservedIdentifier().deepClone());
		clonedUnion.withCloneId(this.getIdentifier()).withSQLQueryContext(this.sqlQueryContext);
		return clonedUnion;
	}

	@Override
	public SQLUnion withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLUnion withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public SQLUnion withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}

}
