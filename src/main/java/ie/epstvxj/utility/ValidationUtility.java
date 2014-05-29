/**
 * SQL Parser
 * 
 * @author jun 
 * @since 29 May 2014
 */
package ie.epstvxj.utility;

import java.io.IOException;
import java.io.Reader;

import org.apache.log4j.Logger;

public class ValidationUtility {

	private static Logger	LOG	= Logger.getLogger(ValidationUtility.class);

	public static void safeClose(final Reader reader) {
		if (null != reader) {
			try {
				reader.close();
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

}
