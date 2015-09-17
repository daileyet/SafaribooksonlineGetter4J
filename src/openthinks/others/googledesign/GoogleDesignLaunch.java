package openthinks.others.googledesign;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.logging.LogManager;

import openthinks.libs.utilities.CommonUtilities;
import openthinks.libs.utilities.logger.ProcessLogger;
import openthinks.others.webpages.WebPagesConfigure;
import openthinks.others.webpages.WebPagesLaunch;
import openthinks.others.webpages.exception.LostConfigureItemException;

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

}
