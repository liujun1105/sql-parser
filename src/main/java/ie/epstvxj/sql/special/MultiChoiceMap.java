/**
 * xAdd Project
 * 
 * @author jun 
 * @since 23 Apr 2014
 */
package ie.epstvxj.sql.special;

import ie.epstvxj.sql.SQLConstruct;

/**
 * @author jun
 *
 */
public class MultiChoiceMap {

	private SQLConstruct	originalRSC;
	private SQLConstruct	alternativeRSC;

	/**
	 * This field is relevent only when <code>RSCBatchRAPlugin</code> is used. </br>
	 * For <code>RSCBatchRAPlugin</code> we want to replace a list of RSC constructs with a new RSC </br>
	 * construct, therefore, we cannot determine the <code>originalRSC</code> (because there're multiple of them). </br>
	 * Instead, the <code>RSCQueryRewriter</code> remove them from the original query and instead of setting the </br>
	 * originalRSC field, the newRSC filed is set.
	 */
	private SQLConstruct	newRSC;

	public void setOriginalRSC(final SQLConstruct originalRSC) {
		this.originalRSC = originalRSC;
	}

	public SQLConstruct getOriginalRSC() {
		return this.originalRSC;
	}

	public void setAlternativeRSC(final SQLConstruct alternativeRSC) {
		this.alternativeRSC = alternativeRSC;
	}

	public SQLConstruct getAlternativeRSC() {
		return this.alternativeRSC;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append(originalRSC.getIdentifier()).append(" ").append(originalRSC.toSql()).append("\n");
		stringBuilder.append(alternativeRSC.getIdentifier()).append(" ").append(alternativeRSC.toSql()).append("\n");
		stringBuilder.append(newRSC.getIdentifier()).append(" ").append(newRSC.toSql());

		return stringBuilder.toString();
	}

	/**
	 * @return the newRSC
	 */
	public SQLConstruct getNewRSC() {
		return newRSC;
	}

	/**
	 * @param newRSC the newRSC to set
	 */
	public void setNewRSC(final SQLConstruct newRSC) {
		this.newRSC = newRSC;
	}
}
