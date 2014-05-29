/**
 * xAdd Project
 * 
 * @author jun 
 * @since 29 May 2014
 */
package ie.epstvxj.parser;

import ie.epstvxj.parser.SQLGrammarParser.SqlParserContext;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.atn.PredictionMode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.log4j.Logger;

/**
 * @author jun
 *
 */
public class SQLHandler {

	private static Logger	LOG				= Logger.getLogger(SQLHandler.class);
	private static boolean	isDebugEnabled	= LOG.isDebugEnabled();

	public static ParseTree getParseTree(final String query) {
		ANTLRInputStream input = new ANTLRInputStream(query);
		SQLGrammarLexer lexer = new SQLGrammarLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		SQLGrammarParser parser = new SQLGrammarParser(tokens);

		parser.getInterpreter().setPredictionMode(PredictionMode.SLL);
		parser.removeErrorListeners();
		parser.setErrorHandler(new IgnoreErrorStrategy());

		SqlParserContext tree = null;
		boolean hasException = false;
		try {
			if (isDebugEnabled) {
				ParsingLogger.LOG.debug("parsing query using SLL(*) algorithm");
			}
			tree = parser.sqlParser();
			// if we get here, there was no syntax error and SLL(*) was enough;
			// there is no need to try full LL(*)
		} catch (RuntimeException ex) {
			if (ex.getClass() == RuntimeException.class && ex.getCause() instanceof RecognitionException) {
				ParsingLogger.LOG.error("failed to use SLL algorithm, retry with LL algorithm");
				((CommonTokenStream) parser.getTokenStream()).reset();
				parser.getInterpreter().setPredictionMode(PredictionMode.LL);
				try {
					if (isDebugEnabled) {
						ParsingLogger.LOG.debug("parsing query using LL(*) algorithm");
					}
					tree = parser.sqlParser();
				} catch (RuntimeException e) {
					hasException = true;
					if (e.getClass() == RuntimeException.class && e.getCause() instanceof RecognitionException) {
						ParsingLogger.LOG.error("failed to use LL algorithm");
					}
				} finally {
					if (!hasException) {
						ParsingLogger.LOG.debug("sucessfully parsed using LL algorithm");
					} else {
						ParsingLogger.LOG.error("!!! SLL and LL algorithms were both failed !!!");
					}
				}
			}
		}

		return tree;
	}
}
