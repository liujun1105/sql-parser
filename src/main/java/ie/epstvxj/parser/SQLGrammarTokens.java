package ie.epstvxj.parser;

public class SQLGrammarTokens {

	public enum SetQuantifier {
		DISTINCT, ALL;
	};

	public enum OrderingSpecification {
		ASC, DESC;
	};

	public enum NullOrdering {
		NULLS_FIRST("NULLS FIRST"), NULLS_LAST("NULLS LAST");

		private final String	value;

		NullOrdering(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return this.getValue();
		}

	}

	public static final String	AND				= "AND";
	public static final String	AS				= "AS";
	public static final String	ASYMMETRIC		= "ASYMMETRIC";
	public static final String	AVG				= "AVG";

	public static final String	BETWEEN			= "BETWEEN";

	public static final String	COMMA			= ",";
	public static final String	COUNT			= "COUNT";

	public static final String	DOT				= ".";
	public static final String	DELETE			= "DELETE";

	public static final String	EQUAL			= "=";
	public static final String	EXISTS			= "EXISTS";

	public static final String	FROM			= "FROM";

	public static final String	GREAT			= ">";
	public static final String	GREAT_EQUAL		= ">=";
	public static final String	GROUP_BY		= "GROUP BY";

	public static final String	HAVING			= "HAVING";

	public static final String	IN				= "IN";
	public static final String	INSERT			= "INSERT";
	public static final String	INTO			= "INTO";
	public static final String	IS				= "IS";

	public static final String	LEFT_PAREN		= "(";
	public static final String	LESS			= "<";
	public static final String	LESS_EQUAL		= "<=";
	public static final String	LIKE			= "LIKE";

	public static final String	MAX				= "MAX";
	public static final String	MIN				= "MIN";
	public static final String	MINUS			= "-";

	public static final String	NOT				= "NOT";
	public static final String	NULL			= "NULL";

	public static final String	SPACE			= " ";
	public static final String	SELECT			= "SELECT";

	public static final String	WHERE			= "WHERE";

	public static final String	ON				= "ON";
	public static final String	OR				= "OR";
	public static final String	ORDER_BY		= "ORDER BY";

	public static final String	PLUS			= "+";

	public static final String	QUESTION_MARK	= "?";

	public static final String	RIGHT_PAREN		= ")";

	public static final String	SET				= "SET";
	public static final String	SUM				= "SUM";
	public static final String	SYMMETRIC		= "SYMMETRIC";

	public static final String	TRUTH			= "true";

	public static final String	UPDATE			= "UPDATE";

	public static final String	VALUES			= "VALUES";
	public static final String	UNION			= "UNION";

}
