package ie.epstvxj.sql;

import java.util.List;

public interface SQL {

	String toSql();

	SQLConstruct deepClone();

	SQLConstruct deepCloneWithPreservedIdentifier();

	SQLConstructType getGeneralType();

	SQLConstructType getActualType();

	boolean isValidSubRSC(SQLConstruct subRSC);

	void addSubRSC(SQLConstruct subRSC);

	List<SQLConstruct> getSubRSCList();

	/**
	 * This method is called when initialise a new instance of the RSC construct
	 * @param identifier
	 * @return
	 */
	SQLConstruct withId(int identifier);

	/**
	 * This method is used when cloning the original RSC construct
	 * @param identifier
	 * @return
	 */
	SQLConstruct withCloneId(String identifier);

	/**
	 * Return the identifier of this RSC construct
	 * @return
	 */
	String getIdentifier();

	void setReferencingRSC(SQLConstruct rsc);

	SQLConstruct getReferencingRSC();

	void removeReferencingRSC();

	SQLConstruct withIndex(int index);

	int getIndex();

	public SQLConstructRepository getRSCRepository();

	SQLQueryContext getSQLQueryContext();

	SQLConstruct withSQLQueryContext(SQLQueryContext sqlQueryContext);
}
