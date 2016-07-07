import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class OutputTest {

	public static void main(String[] args) throws IOException {
		FileInputStream fr = new FileInputStream(new File(
				"W://Book//Beginning_Oracle_Application_Express_5//sec12_html"));

		byte[] buff = new byte[1024 * 1024];
		int len = -1;
		do {
			len = fr.read(buff);
			if (len != -1) {
				String line = new String(buff, 0, len, Charset.forName("UTF-8"));
				line = line.replace("��", "'");
				line = line.replace("??", "");
				System.out.println(line);
			}
		} while (len != -1);

	}
}
