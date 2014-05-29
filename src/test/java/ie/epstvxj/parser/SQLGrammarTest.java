/**
 * SQL Parser
 * 
 * @author jun 
 * @since 29 May 2014
 */
package ie.epstvxj.parser;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jun
 *
 */
public class SQLGrammarTest extends TestBase {

	private static Logger		LOG	= Logger.getLogger(TestBase.class);

	private Map<String, File>	inputFileMap;
	private Map<String, File>	expectedFileMap;
	private SQLAnalyser			sqlAnalyser;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		sqlAnalyser = new SQLAnalyser();
		inputFileMap = getInputFileMap();
		expectedFileMap = getExpectedFileMap();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	private void processQuery(final String query) {
		ParseTree tree = SQLHandler.getParseTree(query);
		if (null != tree) {
			ParseTreeWalker walker = new ParseTreeWalker();
			sqlAnalyser.preProcess();
			walker.walk(sqlAnalyser, tree);
		}
	}

	@Test
	public void checkAndVerify() {

		for (Entry<String, File> entry : inputFileMap.entrySet()) {
			String query = getQuery(entry.getValue());
			if (null == query) {
				fail(String.format("no query in the input file [%s]", entry.getValue()));
			}

			String expected = getQuery(expectedFileMap.get(entry.getKey()));
			if (null == expected) {
				fail(String.format("no expected query in the expected file [%s.expected]", entry.getValue()));
			}

			processQuery(query);

			String actual = sqlAnalyser.getSQLConstruct().toSql();

			if (actual.equals(expected)) {
				LOG.info(String.format("sql query in [%s] passed", entry.getKey()));
			} else {
				LOG.error(String.format("sql query in [%s] failed, expected [%s], actual [%s]", entry.getKey(),
						expected, actual));
				fail(String.format("sql query in [%s] failed, expected [%s], actual [%s]", entry.getKey(), expected,
						actual));
			}

		}
	}

}
