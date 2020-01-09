import java.io.FileInputStream;
import com.openthinks.others.safaribook.SafariBookLaunch;
import com.openthinks.others.webpages.conf.WebPagesConfigure;

public class SafariStarter {

  public static void main(String[] args) throws Exception {
    WebPagesConfigure bookConfigure =
        WebPagesConfigure.readXML(new FileInputStream("W:\\Book2\\config_default.xml"));
    SafariBookLaunch bookLaunch = new SafariBookLaunch(bookConfigure);
    bookLaunch.start();
  }

}
