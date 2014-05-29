package ie.epstvxj.parser;

import org.antlr.v4.runtime.ANTLRErrorStrategy;
import org.antlr.v4.runtime.FailedPredicateException;
import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * @author jun
 * 
 * This class ignores all ANTLR default ERROR and RECORY
 * handling strategies for the purpose of reduing runtime overhead.
 */
public final class IgnoreErrorStrategy implements ANTLRErrorStrategy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.antlr.v4.runtime.ANTLRErrorStrategy#reset(org.antlr.v4.runtime.Parser)
	 */
	@Override
	public void reset(final Parser recognizer) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.antlr.v4.runtime.ANTLRErrorStrategy#recoverInline(org.antlr.v4.runtime.Parser)
	 */
	@Override
	public Token recoverInline(final Parser recognizer) throws RecognitionException {
		// TODO Auto-generated method stub
		ParserRuleContext ruleContext = recognizer.getRuleContext();
		ParsingLogger.LOG.error(ruleContext.toInfoString(recognizer));
		throw new RuntimeException(new InputMismatchException(recognizer));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.antlr.v4.runtime.ANTLRErrorStrategy#recover(org.antlr.v4.runtime.Parser, org.antlr.v4.runtime.RecognitionException)
	 */
	@Override
	public void recover(final Parser recognizer, final RecognitionException e) throws RecognitionException {
		// TODO Auto-generated method stub
		throw new RuntimeException(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.antlr.v4.runtime.ANTLRErrorStrategy#sync(org.antlr.v4.runtime.Parser)
	 */
	@Override
	public void sync(final Parser recognizer) throws RecognitionException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.antlr.v4.runtime.ANTLRErrorStrategy#inErrorRecoveryMode(org.antlr.v4.runtime.Parser)
	 */
	@Override
	public boolean inErrorRecoveryMode(final Parser recognizer) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.antlr.v4.runtime.ANTLRErrorStrategy#reportMatch(org.antlr.v4.runtime.Parser)
	 */
	@Override
	public void reportMatch(final Parser recognizer) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.antlr.v4.runtime.ANTLRErrorStrategy#reportError(org.antlr.v4.runtime.Parser, org.antlr.v4.runtime.RecognitionException)
	 */
	@Override
	public void reportError(final Parser recognizer, final RecognitionException e) {
		// TODO Auto-generated method stub
		ParsingLogger.LOG.error(e.getMessage());
		if (e instanceof NoViableAltException) {
			logNoViableAlternative(recognizer, (NoViableAltException) e);
		} else if (e instanceof InputMismatchException) {
			logInputMismatch(recognizer, (InputMismatchException) e);
		} else if (e instanceof FailedPredicateException) {
			logFailedPredicate(recognizer, (FailedPredicateException) e);
		} else {
			ParsingLogger.LOG.error("unknown recognition error type: " + e.getClass().getName());
		}
	}

	/**
	 * @param recognizer
	 * @param e
	 */
	private void logFailedPredicate(final Parser recognizer, final FailedPredicateException e) {
		String ruleName = recognizer.getRuleNames()[recognizer.getRuleContext().getRuleIndex()];
		String msg = "rule " + ruleName + " " + e.getMessage();
		ParsingLogger.LOG.error(msg);
	}

	/**
	 * @param recognizer
	 * @param e
	 */
	private void logInputMismatch(final Parser recognizer, final InputMismatchException e) {
		String msg = "mismatched input " + getTokenErrorDisplay(e.getOffendingToken()) + " expecting "
				+ e.getExpectedTokens().toString(recognizer.getTokenNames());
		ParsingLogger.LOG.error(msg);
	}

	/**
	 * @param recognizer
	 * @param e
	 */
	private void logNoViableAlternative(final Parser recognizer, final NoViableAltException e) {
		TokenStream tokens = recognizer.getInputStream();
		String input;
		if (tokens instanceof TokenStream) {
			if (e.getStartToken().getType() == Token.EOF)
				input = "<EOF>";
			else
				input = tokens.getText(e.getStartToken(), e.getOffendingToken());
		} else {
			input = "<unknown input>";
		}
		String msg = "no viable alternative at input " + escapeWSAndQuote(input);
		ParsingLogger.LOG.error(msg);
	}

	private String escapeWSAndQuote(@NotNull String s) {
		s = s.replace("\n", "\\n");
		s = s.replace("\r", "\\r");
		s = s.replace("\t", "\\t");
		return "'" + s + "'";
	}

	private String getTokenErrorDisplay(final Token t) {
		if (t == null)
			return "<no token>";
		String s = getSymbolText(t);
		if (s == null) {
			if (getSymbolType(t) == Token.EOF) {
				s = "<EOF>";
			} else {
				s = "<" + getSymbolType(t) + ">";
			}
		}
		return escapeWSAndQuote(s);
	}

	private String getSymbolText(@NotNull final Token symbol) {
		return symbol.getText();
	}

	private int getSymbolType(@NotNull final Token symbol) {
		return symbol.getType();
	}
}
