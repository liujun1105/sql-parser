/**
 * xAdd Project
 * 
 * @author jun 
 * @since 2 Apr 2014
 */
package ie.epstvxj.sql;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jun
 *
 */
public final class SQLConstructIdentifierManager {

	private static final AtomicInteger	seq	= new AtomicInteger();

	public static synchronized void reset() {
		seq.set(0);
	}

	public static synchronized int getRSCIdentifier() {
		return seq.getAndIncrement();
	}

	public static synchronized String getRSCCloneIdentifier(final SQLConstruct rsc) {
		return "o_" + rsc.getIdentifier() + "_c_" + seq.getAndIncrement();
	}
}
