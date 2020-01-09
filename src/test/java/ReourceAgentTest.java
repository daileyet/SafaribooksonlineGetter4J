import java.net.MalformedURLException;
import java.net.URL;
import com.openthinks.others.webpages.agent.HtmlResourceAgent;
import com.openthinks.others.webpages.agent.ResourceAgent;

public class ReourceAgentTest {

	public ReourceAgentTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws MalformedURLException {
		ResourceAgent agent = new HtmlResourceAgent(null) {
		};
		System.out
				.println(agent
						.resolve(new URL(
								"http://techbus.safaribooksonline.com//static/201508-7987-techbus/files/6.0/fonts/din-medium.svg#webfontOnVll9iZ")));
	}
}
