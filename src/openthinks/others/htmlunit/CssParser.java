package openthinks.others.htmlunit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CssParser {

	public CssParser() {

	}

	public static void main(String[] args) throws IOException {
		StringBuffer styleCtx = new StringBuffer();
		try (BufferedReader fileReader = new BufferedReader(new FileReader("D:\\keeper\\style\\global_r.css"))) {
			String line = fileReader.readLine();
			while (line != null) {
				styleCtx.append(line);
				line = fileReader.readLine();
			}

		} catch (Exception e) {

		}

		String REGEX = "url\\(['\"]?([^\\(\\)'\"]+)['\"]?\\)";
		Pattern p = Pattern.compile(REGEX);
		Matcher m = p.matcher(styleCtx.toString()); // 获取 matcher 对象
		int count = 0;

		while (m.find()) {
			count++;
			System.out.println("Match number " + count);
			System.out.println("start(): " + m.start());
			System.out.println("end(): " + m.end());
			System.out.println(m.toString());
			System.out.println(m.group(1));
		}
	}
}
