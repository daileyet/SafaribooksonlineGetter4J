package openthinks.others.safaribook;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.LogManager;

import openthinks.libs.utilities.CommonUtilities;
import openthinks.others.safaribook.exception.LostConfigureItemException;
import openthinks.others.safaribook.util.ProcessLoger;
import openthinks.others.safaribook.util.ProcessLoger.PLLevel;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * Safari book online getter entry
 * @author dailey.yet@outlook.com
 *
 */
public final class SafariBookLaunch {
	private SafariBookConfigure config;

	public SafariBookLaunch(SafariBookConfigure config) {
		super();
		this.config = config;
	}

	/**
	 * start to download action
	 * @throws SecurityException
	 * @throws IOException
	 */
	public void launch() throws SecurityException, IOException {
		if (!config.getKeepDir().isPresent())
			throw new LostConfigureItemException("Lost configuration for save dir for this book.");
		LogManager.getLogManager().readConfiguration(SafariBookLaunch.class.getResourceAsStream("/logging.properties"));
		if (!config.getKeepDir().isPresent())
			throw new LostConfigureItemException("Lost configuration for save dir for this book.");
		try (final WebClient webClient = createWebClient()) {
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setTimeout(35000);
			if (config.needLogin().isPresent() && config.needLogin().get())
				loginAndAuth(webClient);
			travelWholeBook(webClient);
			ProcessLoger.info("All book has been download.");
		} catch (Exception e) {
			ProcessLoger.error(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
		}
	}

	private final void travelWholeBook(WebClient webClient) {
		if (config.getCatalogPageUrl().isPresent()) {
			catalogResolver(webClient);
		} else if (config.getStartChainPageUrl().isPresent()) {
			chainResolver(webClient);
		} else {
			ProcessLoger.warn("No url configuration for this book.");
		}

	}

	void catalogResolver(WebClient webClient) {
		String catalogURL = config.getCatalogPageUrl().get();
		ProcessLoger.info("Go to download the catalog page of this book:" + catalogURL);
		if (!config.getPageLinkOfCatalogSelector().isPresent())
			throw new LostConfigureItemException("Lost configuration for page link selector on catalog page.");
		try {
			HtmlPage catalogPage = webClient.getPage(catalogURL);
			DomNodeList<DomNode> links = catalogPage.getBody().querySelectorAll(
					config.getPageLinkOfCatalogSelector().get());

			links.stream()
					.filter((domNode) -> {
						DomElement el = (DomElement) domNode;
						return el.hasAttribute("href") && !el.getAttribute("href").isEmpty()
								&& !el.getAttribute("href").startsWith("#");
					}).forEach((domNode) -> {
						HtmlAnchor el = (HtmlAnchor) domNode;
						String relativeUrl = el.getHrefAttribute();
						HtmlPage currentPage = null;
						try {
							ProcessLoger.debug(relativeUrl);
							URL currentUrl = catalogPage.getFullyQualifiedUrl(relativeUrl);
							ProcessLoger.debug(currentUrl.toString());
							ProcessLoger.info("Go to download page of this book:" + currentUrl);
							currentPage = webClient.getPage(currentUrl);
							HtmlPageTransfer.create(currentPage, config.getKeepDir().get()).transfer();
						} catch (Exception e) {
							ProcessLoger.error(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
						}
					});

		} catch (FailingHttpStatusCodeException | IOException | LostConfigureItemException e) {
			ProcessLoger.fatal(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
		}
	}

	/**
	 * @param webClient
	 */
	void chainResolver(WebClient webClient) {
		String nextURL = config.getStartChainPageUrl().get();
		HtmlAnchor nextAnchor = null;
		ProcessLoger.info("Go to download the start page of this book:" + nextURL);
		if (!config.getNextChainPageAnchorSelector().isPresent())
			throw new LostConfigureItemException("Lost configuration for next page link selector on each page.");
		do {
			try {
				HtmlPage currentPage = webClient.getPage(nextURL);
				DomNode anchors = currentPage.getBody().querySelector(config.getNextChainPageAnchorSelector().get());
				nextAnchor = (HtmlAnchor) anchors;
				nextURL = currentPage.getFullyQualifiedUrl(nextAnchor.getHrefAttribute()).toString();
				HtmlPageTransfer.create(currentPage, config.getKeepDir().get()).transfer();
				ProcessLoger.info("Go to download next page of this book:" + nextURL);
			} catch (Exception e) {
				ProcessLoger.fatal(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
				nextAnchor = null;
			}
		} while (nextAnchor != null);
	}

	private final void loginAndAuth(WebClient webClient) throws FailingHttpStatusCodeException, MalformedURLException,
			IOException {
		webClient.getOptions().setJavaScriptEnabled(true);
		ProcessLoger.info("Open login page for safaribooksonline...");
		if (!config.getLoginPageUrl().isPresent())
			throw new LostConfigureItemException("Lost configuration for login page url.");
		if (!config.getLoginFormSelector().isPresent())
			throw new LostConfigureItemException("Lost configuration for login form selector.");
		if (!config.getLoginAuthInputName().isPresent())
			throw new LostConfigureItemException("Lost configuration for the input name of login name.");
		if (!config.getLoginAuthInputValue().isPresent())
			throw new LostConfigureItemException("Lost configuration for the input value of login name.");
		if (!config.getLoginAuthPassInputName().isPresent())
			throw new LostConfigureItemException("Lost configuration for the input name of login password.");
		if (!config.getLoginAuthPassInputValue().isPresent())
			throw new LostConfigureItemException("Lost configuration for the input value of login password.");
		if (!config.getLoginSubmitBtnName().isPresent())
			throw new LostConfigureItemException("Lost configuration for the input name of login submit.");

		final HtmlPage loginPage = webClient.getPage(config.getLoginPageUrl().get());
		HtmlForm loginForm = null;
		String formSel = config.getLoginFormSelector().get();
		DomNodeList<DomNode> elements = loginPage.getBody().querySelectorAll(formSel);

		loginForm = elements.isEmpty() ? null : (HtmlForm) elements.get(config.getLoginFormIndex());
		if (loginForm == null) {
			int index = config.getLoginFormIndex();
			List<HtmlForm> forms = loginPage.getForms();
			if (index >= 0 && forms.size() > index) {
				loginForm = forms.get(0);
			}
		}
		if (loginForm == null) {
			throw new IllegalArgumentException("Cannot found the login form.");
		}
		HtmlSubmitInput button = loginForm.getInputByName(config.getLoginSubmitBtnName().get());
		HtmlTextInput userName = loginForm.getInputByName(config.getLoginAuthInputName().get());
		userName.setValueAttribute(config.getLoginAuthInputValue().get());
		HtmlPasswordInput userPass = loginForm.getInputByName(config.getLoginAuthPassInputName().get());
		userPass.setValueAttribute(config.getLoginAuthPassInputValue().get());
		ProcessLoger.info("Simulate the login action...");
		button.click();
		ProcessLoger.info("Login success.");
	}

	protected WebClient createWebClient() {
		if (config.getProxyHost() == null) {
			return new WebClient(config.getBrowserVersion());
		} else {
			return new WebClient(config.getBrowserVersion(), config.getProxyHost().get(), config.getProxyPort());
		}
	}

	public static void main(String[] args) {
		ProcessLoger.currentLevel = PLLevel.INFO;
		SafariBookConfigure config = new SafariBookConfigure();
		SafariBookLaunch bookLaunch = new SafariBookLaunch(config);
		try {
			bookLaunch.launch();
		} catch (SecurityException | IOException | LostConfigureItemException e) {
			ProcessLoger.fatal(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
		}
	}
}
