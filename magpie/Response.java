import java.util.*;
import java.util.regex.*;
public class Response {
	public String[] respArr;
	public String[] keywords;
	public String[] regexKw;
	private String desc = "";

	public Response() {}

	public Response(String[] arr) {
		respArr = arr;
	}

	public Response(String d, String[] arr) {
		respArr = arr;
	}

	public Response(String d, String[] arr, String[] kw) {
		this(arr);
		keywords = kw;
		desc = d;
	}

	public Response(String d, String[] arr, String[] kw, String[] regex) {
		this(d, arr, kw);
		regexKw = regex;
		desc = d;
	}

	public Response(String[] arr, String[] kw) {
		this(arr);
		keywords = kw;
	}

	public Response(String[] arr, String[] kw, String[] regex) {
		this(arr, kw);
		regexKw = regex;
	}

	public Matcher[] parseKeyword(String in) {
		int kwLen = (keywords == null ? 0 : keywords.length);
		int regLen = (regexKw == null ? 0 : regexKw.length);
		Matcher[] parsKw = new Matcher[regLen+kwLen];

		for (int i = 0; i < regLen; ++i)
			parsKw[i] = Pattern.compile(regexKw[i], Pattern.CASE_INSENSITIVE).matcher(in);
		for (int i = regLen; i < parsKw.length; ++i) {
			String kw = "\\b"+keywords[i-regLen].replace("|", "\\b|\\b")+"\\b";
			parsKw[i] = Pattern.compile(kw, Pattern.CASE_INSENSITIVE).matcher(in);
		}

		return parsKw;
	}

	public boolean respondsTo(String in) {
		Matcher[] parsKw = parseKeyword(in);
		boolean hasKeyword = true;
		for (Matcher m : parsKw) {
			if (m != null)
				hasKeyword = hasKeyword && m.find();
			m.reset();
		}
		return hasKeyword;
	}

	public String composeAns(String in) {
		if (!respondsTo(in))
			return null;
		return getRandomResponse(respArr);
	}

	public static String getRandomResponse(String[] s) {
		return s[(int)((s.length)*Math.random())];
	}

	public String toString() {
		return desc;
		// String out = "";
		// if (keywords != null)
		// for (String s : keywords)
		// 	out += s;
	}
}
