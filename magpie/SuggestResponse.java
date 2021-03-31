import java.util.*;
import java.util.regex.*;
public class SuggestResponse extends Response {
	SuggestResponse(String[] arr) {
		super(arr);
	}

	private static boolean[] searchAllResponses(String in) {
		boolean[] respHasKw = new boolean[Magpie.resps.length];
		for (int i = 0; i < respHasKw.length; ++i) {
			if (hasKeyword(Magpie.resps[i], in) && Magpie.resps[i].toString() != "") {
				respHasKw[i] = true;
			}
		}
		return respHasKw;
	}

	private static boolean hasKeyword(Response r, String in) {
		boolean hasKeyword = false;
		if (r.keywords != null) {
			for (int i = 0; i < r.keywords.length; ++i) {
				String kw = "\\b"+r.keywords[i].replace("|", "\\b|\\b")+"\\b";
				hasKeyword = hasKeyword || Pattern.compile(kw, Pattern.CASE_INSENSITIVE).matcher(in).find();
			}
		}
		if (r.regexKw != null)
			for (int i = 0; i < r.regexKw.length; ++i)
				hasKeyword = hasKeyword || Pattern.compile(r.regexKw[i], Pattern.CASE_INSENSITIVE).matcher(in).find();
		return hasKeyword;
	}

	@Override
	// Determine when to respond with a suggestion
	public boolean respondsTo(String in) {
		for (int i = 0; i < Magpie.resps.length; ++i)
			// if response has keywords and description
			if (hasKeyword(Magpie.resps[i], in) && Magpie.resps[i].toString() != "")
				return true;
		return false;
	}

	@Override
	public String composeAns(String in) {
		String out = getRandomResponse(this.respArr);
		boolean[] respHasKw = searchAllResponses(in);
		for (int i = 0; i < respHasKw.length; ++i) {
			if (respHasKw[i])
				out += Magpie.resps[i] + "\n";
		}
		return out;
	}
}
