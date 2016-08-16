package com.openthinks.others.googledesign;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.logging.LogManager;

import com.openthinks.libs.utilities.CommonUtilities;
import com.openthinks.libs.utilities.logger.ProcessLogger;
import com.openthinks.others.webpages.HtmlPageTransfer;
import com.openthinks.others.webpages.WebPagesConfigure;
import com.openthinks.others.webpages.WebPagesLaunch;
import com.openthinks.others.webpages.exception.LostConfigureItemException;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class GoogleDesignLaunch extends WebPagesLaunch {

	public GoogleDesignLaunch(WebPagesConfigure config) {
		super(config);
	}

	public static void main(String[] args) throws SecurityException, IOException {
		LogManager.getLogManager().readConfiguration(
				GoogleDesignLaunch.class.getResourceAsStream("/logging.properties"));
		WebPagesConfigure config = initialConfig(args);
		ProcessLogger.currentLevel = config.getLoggerLevel();
		GoogleDesignLaunch launcher = new GoogleDesignLaunch(config);
		try {
			launcher.launch();
		} catch (SecurityException | IOException | LostConfigureItemException e) {
			ProcessLogger.fatal(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
		}
	}

	private static WebPagesConfigure initialConfig(String[] args) throws InvalidPropertiesFormatException,
			FileNotFoundException, IOException {
		//return WebPagesConfigure.readXML(new FileInputStream(args[0]));
		return WebPagesConfigure.readXML(GoogleDesignLaunch.class.getResourceAsStream("/config_google.xml"));
	}

	@Override
	public HtmlPageTransfer getHtmlPageTransfer(HtmlPage htmlPage, File file) {
		return HtmlPageTransfer.create(htmlPage, file);
	}

}
