package openthinks.others.webpages.safaribook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

import openthinks.libs.utilities.CommonUtilities;
import openthinks.libs.utilities.logger.ProcessLogger;
import openthinks.others.webpages.WebPagesLaunch;
import openthinks.others.webpages.additional.AdditionalBooks;
import openthinks.others.webpages.additional.AdditionalProcessor;
import openthinks.others.webpages.agent.HtmlPageResourceAgent;
import openthinks.others.webpages.exception.LostConfigureItemException;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Safari book online getter entry
 * @author dailey.yet@outlook.com
 *
 */
public final class SafariBookLaunch extends WebPagesLaunch {

	public SafariBookLaunch(SafariBookConfigure config) {
		super();
		this.config = config;
	}

	public static void main(String[] args) throws SecurityException, IOException {
		LogManager.getLogManager().readConfiguration(SafariBookLaunch.class.getResourceAsStream("/logging.properties"));

		SafariBookConfigure config = initialConfig(args);

		ProcessLogger.currentLevel = config.getLoggerLevel();
		//remove script 
		/*
		 <script language="javascript" type="text/javascript">
		//<![CDATA[

		    var CookieState = 'uicode=oracle&PromoCode=&itemsperpage=10&view=book&xmlid=9781783988020%2findex_html&reader=html&displaygrbooks=0&isbn=9781783988020&portal=techbus&omniturexmlidown=1';
		    var Xsl =
		    {
		         _1 : '/static/201508\u002D7989\u002Dtechbus',
		         _2 : '2015/08/21T08/35/37',
		         _3 : '9781783988020/index_html',
		         _4 : 'techbus',
		         _5 : '4F027A71A5A648D480DF1A1FE9C361B3129ADEF308D44217FC0C6F9580BC3428FB971F59A3002BDA0853EE835C29AB929EBBAEB21BF2FEC6A0D3A600F9410F10C3E0EBDE4AF6334092FA557971869710F7726285917F8F6FA903DB29917EDB160CA9E04CFABDD38A380F7F07C52F2830715EF5466791611AA1AC9C8C6AF9AD98',
		         _6 : '9b908e61\u002Dc2a7\u002D4dc4\u002Db216\u002Dd49d03df90cf',
		         _7 : 'Undefined',
		         _8 : '6.0.3',
		         _9 : '',
		         _10 : '0',
		         _11 : '/book',
		         _12 : '',
		         _13 : '1',
		         _14 : '0',
		         _15 : '3645443',
		         _16 : '',
		         _17 : '',
		         _18 : '1',
		         _19 : ''
		    };
		    var BvD_StaticPath = Xsl._1;

		//]]>
		    </script>
		*/
		AdditionalBooks.register(HtmlPageResourceAgent.class, new AdditionalProcessor() {

			@Override
			public void process(HtmlElement element) {
				HtmlPage page = (HtmlPage) element.getPage();
				HtmlElement head = page.getHead();
				head.getElementsByTagName("script")
						.stream()
						.filter((HtmlElement script) -> {
							return !script.hasAttribute("src")
									&& "text/javascript".equalsIgnoreCase(script.getAttribute("type"))
									&& script.getTextContent().contains("CookieState");
						}).forEach((HtmlElement script) -> {
							script.remove();
						});
			}
		});

		SafariBookLaunch bookLaunch = new SafariBookLaunch(config);
		try {
			bookLaunch.launch();
		} catch (SecurityException | IOException | LostConfigureItemException e) {
			ProcessLogger.fatal(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
		}
	}

	protected static SafariBookConfigure initialConfig(String[] args) {
		//		config.setBrowserVersion(BrowserVersion.FIREFOX_38);
		//		config.setNeedLogin(true);
		//		config.setKeepDir("W:\\book\\");
		//		config.setLoginPageUrl("");
		//		config.setLoginAuthInputName("");
		//		config.setLoginAuthInputValue("");
		//		config.setLoginAuthPassInputName("");
		//		config.setLoginAuthPassInputValue("");
		//		config.setLoginFormSelector("");
		//		config.setLoginFormIndex(0);
		//		config.setLoginSubmitBtnName("");
		//		config.setCatalogPageUrl("");
		//		config.setPageLinkOfCatalogSelector("");
		//		config.setStartChainPageUrl("");
		//		config.setNextChainPageAnchorSelector("");
		//		config.setProxyHost("");
		//		config.setProxyPort(80);
		//		config.setLoggerLevel(PLLevel.INFO);
		String config_path = null;
		if (args != null && args.length > 0) {
			if ("-help".equalsIgnoreCase(args[0])) {
				showUsage();
			}
			if ("-config".equalsIgnoreCase(args[0])) {
				if (args.length >= 2) {
					config_path = args[1];
				} else {
					System.out.println("miss configure file path!");
					showUsage();
				}
			}
		} else {
			showUsage();
		}

		try {
			return SafariBookConfigure.readXML(new FileInputStream(config_path));
		} catch (IOException e) {
			ProcessLogger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 */
	protected static void showUsage() {
		System.out.println("Usage: <option> [args]");
		System.out.println("option:");
		System.out.println("\t -help");
		System.out.println("\t -config");
		System.out.println("example:");
		System.out.println(" -config W:\\keeper\\configure.xml");
	}
}
