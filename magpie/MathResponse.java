import java.util.regex.*;
public class MathResponse extends Response {
	String[] syms;
	double[] symVals;
	
	public MathResponse(String d, String[] arr, String[] kw, String[] syms, double[] symVals) {
		super(d, arr, kw, new String[] {"\\d+\\.?\\d*"});
		this.syms = syms;
		this.symVals = symVals;
	}

	public MathResponse(String d) { // For general calculations
		super(d, null, null, new String[]{"([()-+=/*^\s]*\\d+(\\.\\d+)?\\)*)+"});
	}

	@Override
	public Matcher[] parseKeyword(String in) {
		if (keywords == null)
			return new Matcher[] {parseNumber(in)};

		Matcher[] parsKw = new Matcher[keywords.length];
		for (int i = 0; i < parsKw.length; ++i) {
			String kw = "\\b"+keywords[i].replace("|", "\\b|\\b")+"\\b";
			parsKw[i] = Pattern.compile(kw, Pattern.CASE_INSENSITIVE).matcher(in);
		}
		return parsKw;
	}

	public Matcher parseNumber(String in) {
		return Pattern.compile(regexKw[0], Pattern.CASE_INSENSITIVE).matcher(in);
	}

	@Override
	public String composeAns(String in) {
		if (!respondsTo(in))
			return null;

		Matcher m = parseNumber(in);
		// Case1: General calculations
		if (respArr == null) {
			m.find();
			return eval(m.group(0));
		}

		// Case 2: Formula
		// e.g: {%1, %2*%1^2}

		String[] out = new String[respArr.length];
		for (int i = 0; i < out.length; ++i)
			out[i] = respArr[i];

		int enteredVals = 0;
		for (int i = 0; i < syms.length; ++i) {
			if (symVals[i] == 0)
				--enteredVals;
			if (m.find())
				++enteredVals;
		}

		m.reset();
		if (enteredVals < 0)
			for (int i = 0; i < syms.length; ++i)
				for (int j = 1; j < out.length; ++j)
					out[j] = out[j].replace("%"+i, syms[i]);
		else {
			for (int i = 0; i < syms.length; ++i) {
				String repl = ""+symVals[i]; // Start with default
				if (symVals[i] == 0 && m.find()) // if input required
					repl = m.group(0);

				for (int j = 1; j < out.length; ++j)
					out[j] = out[j].replace("%"+i, repl);
			}
			for (int j = 1; j < out.length; ++j)
				if (out[j].matches("([()-+=/*^\s]*\\d+(\\.\\d+)?\\)*)+"))
					out[j] = eval(out[j]);
		}

		for (int j = 1; j < out.length; ++j)
			out[0] = out[0].replaceFirst("%s", out[j]);
		
		return out[0];
	}

	// Math parser
	public static String eval(final String str) {
		return ""+new Object() {
			int pos = -1, ch;

			void nextChar() {
				ch = (++pos < str.length()) ? str.charAt(pos) : -1;
			}

			boolean eat(int charToEat) {
				while (ch == ' ') nextChar();
				if (ch == charToEat) {
					nextChar();
					return true;
				}
				return false;
			}

			double parse() {
				nextChar();
				double x = parseExpression();
				if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
				return x;
			}

			double parseExpression() {
				double x = parseTerm();
				for (;;) {
					if      (eat('+')) x += parseTerm(); // addition
					else if (eat('-')) x -= parseTerm(); // subtraction
					else return x;
				}
			}

			double parseTerm() {
				double x = parseFactor();
				for (;;) {
					if      (eat('*')) x *= parseFactor(); // multiplication
					else if (eat('/')) x /= parseFactor(); // division
					else return x;
				}
			}

			double parseFactor() {
				if (eat('+')) return parseFactor(); // unary plus
				if (eat('-')) return -parseFactor(); // unary minus

				double x;
				int startPos = this.pos;
				if (eat('(')) { // parentheses
					x = parseExpression();
					eat(')');
				} else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
					while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
					x = Double.parseDouble(str.substring(startPos, this.pos));
				} else if (ch >= 'a' && ch <= 'z') { // functions
					while (ch >= 'a' && ch <= 'z') nextChar();
					String func = str.substring(startPos, this.pos);
					x = parseFactor();
					if (func.equals("sqrt")) x = Math.sqrt(x);
					else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
					else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
					else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
					else throw new RuntimeException("Unknown function: " + func);
				} else {
					throw new RuntimeException("Unexpected: " + (char)ch);
				}

				if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

				return x;
			}
		}.parse();
	}
}
