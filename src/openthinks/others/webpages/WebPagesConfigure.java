package openthinks.others.webpages;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Optional;
import java.util.Properties;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * The configuration for {@link WebPagesLaunch}
 * @author dailey.yet@outlook.com
 *
 */
public class WebPagesConfigure extends Properties {

	private static final long serialVersionUID = 1571830072530562701L;
	public static final String BROWSERVERSION = "browser-version";
	public static final String PROXYHOST = "proxy-host";
	public static final String PROXYPORT = "proxy-port";
	public static final String LOGINPAGEURL = "login-url";
	public static final String LOGINFORMSELECTOR = "login-form-selector";
	public static final String LOGINFROMINDEX = "login-form-index";
	public static final String LOGINSUBMITBTNNAME = "login-form-submit-name";
	public static final String LOGINNAMEINPUTNAME = "login-form-username-input-name";
	public static final String LOGINPASSINPUTNAME = "login-form-password-input-name";
	public static final String LOGINNAMEVALUE = "login-form-username-input-value";
	public static final String LOGINPASSVALUE = "login-form-password-input-value";
	public static final String KEEPDIR = "save-dir";
	public static final String NEEDLOGIN = "need-login";
	public static final String CATALOGPAGEURL = "pages-catalog-url";
	public static final String PAGELINKOFCATALOGSELECTOR = "catalog-pagelinks-selector";
	public static final String STARTCHAINPAGEURL = "pages-first-url";
	public static final String NEXTCHAINPAGEANCHORSELECTOR = "pages-next-anchor-selector";

	///////////////////////////
	public static WebPagesConfigure create() {
		return new WebPagesConfigure();
	}

	public static WebPagesConfigure readXML(InputStream is) throws InvalidPropertiesFormatException, IOException {
		WebPagesConfigure instance = WebPagesConfigure.create();
		instance.loadFromXML(is);
		return instance;
	}

	public static WebPagesConfigure readProps(InputStream is) throws IOException {
		WebPagesConfigure instance = WebPagesConfigure.create();
		instance.load(is);
		return instance;
	}

	///////////////////////////

	public WebPagesConfigure() {
		super();
	}

	protected Optional<String> getProp(String propertyName) {
		return Optional.of(this.getProperty(propertyName));
	}

	@SuppressWarnings("deprecation")
	public BrowserVersion getBrowserVersion() {
		Optional<String> version = getProp(BROWSERVERSION);
		if (version.isPresent()) {
			switch (version.get()) {
			case "FF38":
				return BrowserVersion.FIREFOX_38;
			case "FF31":
				return BrowserVersion.FIREFOX_31;
			case "CHROME":
				return BrowserVersion.CHROME;
			case "IE11":
				return BrowserVersion.INTERNET_EXPLORER_11;
			case "IE8":
				return BrowserVersion.INTERNET_EXPLORER_8;
			}
		}
		return BrowserVersion.getDefault();
	}

	public void setBrowserVersion(BrowserVersion version) {
		BrowserVersion bversion = (version == null) ? BrowserVersion.getDefault() : version;
		this.setProperty(BROWSERVERSION, bversion.getNickname());
	}

	public Optional<String> getProxyHost() {
		return getProp(PROXYHOST);
	}

	public void setProxyHost(String host) {
		this.setProperty(PROXYHOST, host);
	}

	public int getProxyPort() {
		if (getProp(PROXYHOST).isPresent()) {
			try {
				return Integer.valueOf(getProp(PROXYHOST).get());
			} catch (NumberFormatException e) {
			}
		}
		return 80;
	}

	public void setProxyPort(int port) {
		setProperty(PROXYPORT, String.valueOf(port));
	}

	public Optional<String> getLoginPageUrl() {
		return getProp(LOGINPAGEURL);
	}

	public void setLoginPageUrl(String value) {
		setProperty(LOGINPAGEURL, value);
	}

	public Optional<String> getLoginFormSelector() {
		return getProp(LOGINFORMSELECTOR);
	}

	public void setLoginFormSelector(String value) {
		setProperty(LOGINFORMSELECTOR, value);
	}

	public int getLoginFormIndex() {
		if (getProp(LOGINFROMINDEX).isPresent()) {
			try {
				return Integer.valueOf(getProp(LOGINFROMINDEX).get());
			} catch (NumberFormatException e) {
			}
		}
		return 0;
	}

	public void setLoginFormIndex(int value) {
		setProperty(LOGINFROMINDEX, String.valueOf(value));
	}

	public Optional<String> getLoginSubmitBtnName() {
		//return Optional.of("login");
		return getProp(LOGINSUBMITBTNNAME);
	}

	public void setLoginSubmitBtnName(String value) {
		setProperty(LOGINSUBMITBTNNAME, value);
	}

	public Optional<String> getLoginAuthInputName() {
		//		return Optional.of("email");
		return getProp(LOGINNAMEINPUTNAME);
	}

	public void setLoginAuthInputName(String value) {
		setProperty(LOGINNAMEINPUTNAME, value);
	}

	public Optional<String> getLoginAuthInputValue() {
		return getProp(LOGINNAMEVALUE);
	}

	public void setLoginAuthInputValue(String value) {
		setProperty(LOGINNAMEVALUE, value);
	}

	public Optional<String> getLoginAuthPassInputName() {
		//		return Optional.of("password1");
		return getProp(LOGINPASSINPUTNAME);
	}

	public void setLoginAuthPassInputName(String value) {
		setProperty(LOGINPASSINPUTNAME, value);
	}

	public Optional<String> getLoginAuthPassInputValue() {
		return getProp(LOGINPASSVALUE);
	}

	public void setLoginAuthPassInputValue(String value) {
		setProperty(LOGINPASSVALUE, value);
	}

	public Optional<File> getKeepDir() {
		if (getProp(KEEPDIR).isPresent()) {
			return Optional.of(new File(getProp(KEEPDIR).get()));
		}
		return Optional.ofNullable(null);
	}

	public void setKeepDir(String value) {
		setProperty(KEEPDIR, value);
	}

	public Optional<Boolean> needLogin() {
		if (getProp(NEEDLOGIN).isPresent()) {
			return Optional.of(Boolean.valueOf(getProp(NEEDLOGIN).get()));
		}
		return Optional.ofNullable(true);
	}

	public void setNeedLogin(Boolean value) {
		setProperty(NEEDLOGIN, String.valueOf(value));
	}

	public Optional<String> getCatalogPageUrl() {
		return getProp(CATALOGPAGEURL);
	}

	public void setCatalogPageUrl(String value) {
		setProperty(CATALOGPAGEURL, value);
	}

	public Optional<String> getPageLinkOfCatalogSelector() {
		//		return Optional.of("div.catalog_container a[href*='9781491901939']");
		return getProp(PAGELINKOFCATALOGSELECTOR);
	}

	public void setPageLinkOfCatalogSelector(String value) {
		setProperty(PAGELINKOFCATALOGSELECTOR, value);
	}

	public Optional<String> getStartChainPageUrl() {

		return getProp(STARTCHAINPAGEURL);
	}

	public void setStartChainPageUrl(String value) {
		setProperty(STARTCHAINPAGEURL, value);
	}

	public Optional<String> getNextChainPageAnchorSelector() {
		//		return Optional.of("a.next[title*='Next (Key: n)']");
		return getProp(NEXTCHAINPAGEANCHORSELECTOR);
	}

	public void setNextChainPageAnchorSelector(String value) {
		setProperty(NEXTCHAINPAGEANCHORSELECTOR, value);
	}
}