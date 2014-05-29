package ie.epstvxj.sql;

/**
 * @author jun
 *
 */
public interface SQLConstruct extends SQL {

	/**
	 * Replace <code>oldRSC</code> with <code>newRSC</code>
	 * @param oldRSC existing RSC construct
	 * @param newRSC new RSC construct
	 */
	void replace(SQLConstruct oldRSC, SQLConstruct newRSC);

}
