/**
 * xAdd Project
 * 
 * @author jun 
 * @since 27 May 2014
 */
package ie.epstvxj.sql.value;

import ie.epstvxj.sql.tokens.SQLSign;

/**
 * @author jun
 *
 */
public abstract class SQLNumericValue extends SQLValue {

	private int	signIndex;

	public SQLNumericValue withSign(final SQLSign rscSign) {
		this.signIndex = this.getRSCRepository().addRSC(rscSign);
		rscSign.setReferencingRSC(this);
		return this;
	}

	public SQLSign sign() {
		return (SQLSign) this.getRSCRepository().getRSC(signIndex);
	}
}
