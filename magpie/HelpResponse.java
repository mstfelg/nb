public class HelpResponse extends Response {
	HelpResponse(String d, String[] resps, String[] kw) {
		super(d, resps, kw);
	}
	@Override
	public String composeAns(String in) {
		String out = respArr[0];
		for (int i = 0; i < Magpie.resps.length; ++i)
			if (!Magpie.resps[i].toString().equals(""))
				out += "\t"+Magpie.resps[i].toString() +"\n";
		return out;
	}
}
