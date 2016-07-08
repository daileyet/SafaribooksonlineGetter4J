import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import openthinks.others.safaribook.SafariBookConfigure;
import openthinks.others.safaribook.SafariBookLaunch;
import openthinks.others.webpages.exception.LaunchFailedException;

public class SafariStarter {

	public static void main(String[] args) throws InvalidPropertiesFormatException, FileNotFoundException, IOException,
			LaunchFailedException {
		SafariBookConfigure bookConfigure = SafariBookConfigure.readXML(new FileInputStream(
				"W:\\Book2\\config_default.xml"));
		SafariBookLaunch bookLaunch = new SafariBookLaunch(bookConfigure);
		bookLaunch.start();
	}

}
