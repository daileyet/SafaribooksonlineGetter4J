package openthinks.others.webpages.safaribook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;

import openthinks.libs.utilities.CommonUtilities;
import openthinks.others.webpages.WebPagesLaunch;
import openthinks.others.webpages.exception.LostConfigureItemException;
import openthinks.others.webpages.util.ProcessLoger;
import openthinks.others.webpages.util.ProcessLoger.PLLevel;

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
		ProcessLoger.currentLevel = PLLevel.DEBUG;
		SafariBookConfigure config = initialConfig(args);
		SafariBookLaunch bookLaunch = new SafariBookLaunch(config);
		try {
			bookLaunch.launch();
		} catch (SecurityException | IOException | LostConfigureItemException e) {
			ProcessLoger.fatal(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
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
		String config_path = "W:\\book\\config_default.xml";
		if (args != null && args.length > 0) {
			if ("-help".equalsIgnoreCase(args[0])) {
				showUsage();
			}
			if ("-config".equalsIgnoreCase(args[0])) {
				if (args.length > 2) {
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
			ProcessLoger.error(e.getMessage());
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
