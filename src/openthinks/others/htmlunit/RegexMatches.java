package openthinks.others.htmlunit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatches {
	private static String REGEX = "a*b";
	private static String INPUT = "ddddaabfooaabfooabfoob";
	private static String REPLACE = "-";

	public static void main(String[] args) {
		Pattern p = Pattern.compile(REGEX);
		// 获取 matcher 对象
		Matcher m = p.matcher(INPUT);
		StringBuffer sb = new StringBuffer();
		int count = 0;
		while (m.find()) {
			count++;
			m.appendReplacement(sb, count + "");
		}
		m.appendTail(sb);
		System.out.println(sb.toString());
		System.out.println(INPUT);
	}
}