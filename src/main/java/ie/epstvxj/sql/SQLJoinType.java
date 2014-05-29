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
//@formatter:off
public enum SQLJoinType {

	JOIN("JOIN"),
	INNER_JOIN("INNER JOIN"),
	LEFT_JOIN("LEFT JOIN"),
	RIGHT_JOIN("RIGHT JOIN"),
	FULL_JOIN("FULL JOIN"),
	UNION_JOIN("UNION JOIN"),
	CROSS_JOIN("CROSS JOIN"), 
	NATURAL_JOIN("NATURAL JOIN"),
	NATURAL_INNER_JOIN("NATURAL INNER JOIN"),
	NATURAL_LEFT_JOIN("NATURAL LEFT JOIN"),
	NATURAL_RIGHT_JOIN("NATURAL RIGHT JOIN"),
	NATURAL_FULL_JOIN("NATURAL FULL JOIN");

	private String	value;

	SQLJoinType(final String value) {
		this.value = value;
	}

	public String toSql() {
		return value;
	}
	
}
