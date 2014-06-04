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

import org.apache.log4j.Logger;

/**
 * @author jun
 *
 */
public abstract class AbstractSQLConstruct implements SQLConstruct {

	private static Logger			LOG				= Logger.getLogger(AbstractSQLConstruct.class);

	protected String				identifier;

	/**
	 * index in the RSCRepository of the directly impacted referencing RSC construct
	 */
	private Integer					index			= -1;

	protected List<SQLConstruct>	subRSCList		= new ArrayList<SQLConstruct>();
	protected SQLConstruct			referencingRSC;

	private final SQLConstructRepository		rscRepository	= new SQLConstructRepository();

	protected SQLQueryContext		sqlQueryContext	= SQLQueryContext.UNKNWON;

	protected void logAddInvalidSubSQLConstruct(final Logger log, final SQLConstruct sqlConstruct,
			final SQLConstruct subSQLConstruct) {
		log.error("trying to add invalid sql construct [" + subSQLConstruct.getGeneralType() + " {"
				+ subSQLConstruct.toSql() + "}" + "] to [" + this.getActualType() + "] construct");
	}

	@Override
	public SQLQueryContext getSQLQueryContext() {
		return sqlQueryContext;
	}

	@Override
	public SQLConstruct withSQLQueryContext(final SQLQueryContext sqlQueryContext) {
		this.sqlQueryContext = sqlQueryContext;
		return this;
	}

	@Override
	public void replace(final SQLConstruct oldRSC, final SQLConstruct newRSC) {
		if (oldRSC.getIndex() != -1) {
			this.rscRepository.replace(oldRSC.getIndex(), newRSC);
		} else {
			subRSCList.remove(oldRSC);
			subRSCList.add(newRSC);
		}
		newRSC.setReferencingConstruct(this);
	}

	@Override
	public SQLConstructRepository getRepository() {
		return this.rscRepository;
	}

	@Override
	public SQLConstruct getReferencingRSC() {
		return referencingRSC;
	}

	@Override
	public void setReferencingConstruct(final SQLConstruct referencingRSC) {
		this.referencingRSC = referencingRSC;
	}

	@Override
	public void removeReferencingRSC() {
		this.referencingRSC = null;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public boolean isValidSubRSC(final SQLConstruct subSQLConstruct) {
		return false;
	}

	@Override
	public SQLConstructType getGeneralType() {
		return SQLConstructType.UNKNOWN;
	}

	@Override
	public SQLConstructType getActualType() {
		return getGeneralType();
	}

	@Override
	public void addSubRSC(final SQLConstruct subRSC) {
		if (isValidSubRSC(subRSC)) {
			subRSCList.add(subRSC);
			subRSC.setReferencingConstruct(this);
		} else {
			logAddInvalidSubSQLConstruct(LOG, this, subRSC);
		}
	}

	@Override
	public List<SQLConstruct> getSubRSCList() {
		return this.subRSCList;
	}

	@Override
	public SQLConstruct withIndex(final int index) {
		this.index = index;
		return this;
	}

	@Override
	public int getIndex() {
		return this.index;
	}

	@Override
	public String toSql() {

		StringBuilder stringBuilder = new StringBuilder();

		int size = this.subRSCList.size();
		for (int i = 0; i < size; i++) {
			stringBuilder.append(this.subRSCList.get(i).toSql());
			if (i + 1 != size) {
				stringBuilder.append(SQLGrammarTokens.COMMA).append(SQLGrammarTokens.SPACE);
			}
		}

		return stringBuilder.toString();
	}

	@Override
	public boolean equals(final Object that) {
		if (that instanceof SQLConstruct) {
			return this.getIdentifier().equals(((SQLConstruct) that).getIdentifier());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.getIdentifier().hashCode();
	}

	@Override
	public String toString() {
		return getIdentifier();
	}

}
