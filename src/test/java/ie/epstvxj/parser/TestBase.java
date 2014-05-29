/**
 * SQL Parser
 * 
 * @author jun 
 * @since 29 May 2014
 */
package ie.epstvxj.parser;

import ie.epstvxj.utility.ValidationUtility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class TestBase {

	private static Logger	LOG	= Logger.getLogger(TestBase.class);

	public Map<String, File> getInputFileMap() {

		Map<String, File> map = new HashMap<String, File>();

		String dirPath = "src/test/resources/sql";

		File dir = new File(dirPath);

		if (dir.exists()) {
			File[] files = dir.listFiles(new FileFilter() {

				@Override
				public boolean accept(final File pathname) {
					if (pathname.getName().endsWith(".sql"))
						return true;
					return false;
				}

			});

			for (File file : files) {
				map.put(file.getName(), file);
			}
		} else {
			LOG.error(String.format("directory [%s] does not exist", dirPath));
		}

		return map;
	}

	public Map<String, File> getExpectedFileMap() {
		Map<String, File> map = new HashMap<String, File>();

		String dirPath = "src/test/resources/sql";

		File dir = new File(dirPath);

		if (dir.exists()) {

			File[] files = dir.listFiles(new FileFilter() {
				@Override
				public boolean accept(final File pathname) {
					if (pathname.getName().endsWith(".sql.expected"))
						return true;
					return false;
				}

			});

			for (File file : files) {
				map.put(file.getName().replace(".expected", ""), file);
			}
		} else {
			LOG.error(String.format("directory [%s] does not exist", dirPath));
		}

		return map;
	}

	public String getQuery(final File file) {
		String query = null;

		if (null != file) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));

				query = reader.readLine();

				reader.close();
			} catch (Exception ex) {
				LOG.error(ex.getMessage(), ex);
				ValidationUtility.safeClose(reader);
			}
		} else {
			LOG.error("NULL received");
		}

		return query;
	}

}
