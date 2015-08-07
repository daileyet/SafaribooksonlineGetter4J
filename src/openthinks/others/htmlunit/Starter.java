package openthinks.others.htmlunit;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class Starter {
	static String startBookURL = "http://techbus.safaribooksonline.com/book/programming/java/9781617291999/copyright/kindle_split_001_html";
	static String loginURL = "https://www.safaribooksonline.com/accounts/login";
	static File saveDir = new File("D:\\keeper");
	static String authName = "dailey.dai@oracle.com";
	static String authPass = "dmj123";

	public static void main(String[] args) throws Exception {

		try (final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_38, "cn-proxy.jp.oracle.com", 80)) {
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setTimeout(35000);
			System.out.println("Open login page for safaribooksonline...");

			loginAndAuth(webClient);

			travelWholeBook(webClient);

			System.out.println("All book has been download.");
		}
	}

	/**
	 * @param webClient
	 * @throws IOException
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws ElementNotFoundException
	 */
	protected static void loginAndAuth(final WebClient webClient) throws IOException, FailingHttpStatusCodeException,
			MalformedURLException, ElementNotFoundException {
		final HtmlPage loginPage = webClient.getPage(loginURL);

		List<HtmlForm> forms = loginPage.getForms();

		HtmlForm loginForm = forms.get(0);

		HtmlSubmitInput button = loginForm.getInputByName("login");

		HtmlTextInput userName = loginForm.getInputByName("email");
		userName.setValueAttribute(authName);

		HtmlPasswordInput userPass = loginForm.getInputByName("password1");
		userPass.setValueAttribute(authPass);
		System.out.println("Simulate the login action...");
		button.click();
		System.out.println("Login success.");
	}

	/**
	 * @param webClient
	 * @param baseURI
	 * @throws IOException
	 * @throws FailingHttpStatusCodeException
	 * @throws MalformedURLException
	 * @throws ElementNotFoundException
	 */
	protected static void travelWholeBook(final WebClient webClient) throws IOException,
			FailingHttpStatusCodeException, MalformedURLException, ElementNotFoundException {
		System.out.println("Change to download book first page...");

		HtmlPage currentPage;
		String nextURL = startBookURL;
		HtmlAnchor nextAnchor;
		do {
			try {
				currentPage = webClient.getPage(nextURL);
				nextAnchor = currentPage.getAnchorByText("Next (Key: n)");
				nextURL = currentPage.getFullyQualifiedUrl(nextAnchor.getHrefAttribute()).toString();
				PageKeeper.save(currentPage, saveDir);
				System.out.println("Change to download book next page:" + nextAnchor.getHrefAttribute());
			} catch (Exception e) {
				e.printStackTrace();
				nextAnchor = null;
			}
		} while (nextAnchor != null);

	}
}

//		System.setProperty("http.proxyHost", "cn-proxy.jp.oracle.com");
//		System.setProperty("http.proxyPort", "80");
// 			Jsoup auth with cookies
//			Map<String, String> cookies = new HashMap<String, String>();
//			webClient.getCookieManager().getCookies().stream().forEach((cookie) -> {
//				cookies.put(cookie.getName(), cookie.getValue());
//			});
//
//			Response response = Jsoup
//					.connect(
//							"http://techbus.safaribooksonline.com/book/programming/java/9781617291999/copyright/kindle_split_001_html")
//					.cookies(cookies).ignoreContentType(true).execute();
//
//			FileOutputStream out = (new FileOutputStream(new java.io.File("D:\\kindle_split_001_html")));
//			out.write(response.bodyAsBytes()); // resultImageResponse.body() is where the image's contents are.
//			out.close();