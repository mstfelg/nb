import java.util.Date;
public class DateResponse extends Response {
	DateResponse(String d, String[] resps, String[] kw) {
		super(d, resps, kw);
	}
	@Override
	public String composeAns(String in) {
		Date date = new Date();
		return String.format("%1$s %2$tB %2$td, %2$tY", respArr[0], date);
	}
}
