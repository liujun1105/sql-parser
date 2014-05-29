/**
 * xAdd Project
 * 
 * @author jun 
 * @since 27 May 2014
 */
package ie.epstvxj.sql.tokens;

import ie.epstvxj.sql.SQLConstructIdentifierManager;
import ie.epstvxj.sql.SQLConstructType;

/**
 * @author jun
 *
 */
public class SQLSign extends SQLTokens {

	private String	sign;

	public SQLSign() {

	}

	public SQLSign(final String sign) {
		this.sign = sign;
	}

	public SQLSign withSign(final String sign) {
		this.sign = sign;
		return this;
	}

	@Override
	public SQLConstructType getActualType() {
		return SQLConstructType.SIGN_TOKEN;
	}

	@Override
	public SQLSign deepClone() {
		return new SQLSign(sign).withCloneId(SQLConstructIdentifierManager.getRSCCloneIdentifier(this));
	}

	@Override
	public SQLSign deepCloneWithPreservedIdentifier() {
		return new SQLSign(sign).withCloneId(this.getIdentifier());
	}

	@Override
	public SQLSign withId(final int identifier) {
		this.identifier = this.getActualType() + "_" + identifier;
		return this;
	}

	@Override
	public SQLSign withCloneId(final String identifier) {
		this.identifier = identifier;
		return this;
	}

	@Override
	public String toSql() {
		StringBuilder sql = new StringBuilder();

		sql.append(sign);

		return sql.toString();
	}

}
