package com.openthinks.others.webpages;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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
import com.openthinks.libs.utilities.CommonUtilities;
import com.openthinks.libs.utilities.logger.ProcessLogger;
import com.openthinks.others.webpages.exception.LostConfigureItemException;

/**
 * The web pages download launcher
 * @author dailey.yet@outlook.com
 *
 */
public abstract class WebPagesLaunch {

	protected WebPagesConfigure config;

	public WebPagesLaunch() {
		super();
	}

	public WebPagesLaunch(WebPagesConfigure config) {
		super();
		this.config = config;
	}

	/**
	 * start to download action
	 * @throws SecurityException
	 * @throws IOException
	 */
	public final void launch() throws SecurityException, IOException {
		if (config == null)
			throw new LostConfigureItemException("No configuration.");
		if (!config.getKeepDir().isPresent())
			throw new LostConfigureItemException("Lost configuration for save dir.");

		try (final WebClient webClient = createWebClient()) {
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setTimeout(35000);
			if (config.needLogin().isPresent() && config.needLogin().get())
				loginAndAuth(webClient);
			travelWholePages(webClient);
			ProcessLogger.info("All pages has been download.");
		} catch (Exception e) {
			ProcessLogger.error(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
		}
	}

	protected void loginAndAuth(WebClient webClient)
			throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		webClient.getOptions().setJavaScriptEnabled(true);
		ProcessLogger.info("Open login page...");
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
		ProcessLogger.debug("Login page load success:" + (loginPage != null));
		HtmlForm loginForm = null;
		String formSel = config.getLoginFormSelector().get();
		DomNodeList<DomNode> elements = loginPage.getBody().querySelectorAll(formSel);
		ProcessLogger.debug("Login form in login page found(1):" + !elements.isEmpty());
		loginForm = elements.isEmpty() ? null : (HtmlForm) elements.get(config.getLoginFormIndex());
		if (loginForm == null) {
			int index = config.getLoginFormIndex();
			List<HtmlForm> forms = loginPage.getForms();
			ProcessLogger.debug("Login form in login page found(2):" + !forms.isEmpty());
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
		ProcessLogger.info("Simulate the login action...");
		button.click();
		ProcessLogger.info("Login success.");
	}

	protected final void travelWholePages(WebClient webClient) {
		if (config.getCatalogPageUrl().isPresent()) {
			catalogResolver(webClient);
		} else if (config.getStartChainPageUrl().isPresent()) {
			chainResolver(webClient);
		} else {
			ProcessLogger.warn("No url configuration.");
		}
	}

	protected void catalogResolver(WebClient webClient) {
		String catalogURL = config.getCatalogPageUrl().get();
		ProcessLogger.info("Go to download the catalog page:" + catalogURL);
		if (!config.getPageLinkOfCatalogSelector().isPresent())
			throw new LostConfigureItemException("Lost configuration for page link selector on catalog page.");
		try {
			HtmlPage catalogPage = webClient.getPage(catalogURL);
			HtmlPageTransfer htmlPageTransfer = getHtmlPageTransfer(catalogPage.cloneNode(true),
					config.getKeepDir().get());
			htmlPageTransfer.transfer();
			DomNodeList<DomNode> links = catalogPage.getBody()
					.querySelectorAll(config.getPageLinkOfCatalogSelector().get());

			links.stream().filter((domNode) -> {
				DomElement el = (DomElement) domNode;
				return el.hasAttribute("href") && !el.getAttribute("href").isEmpty()
						&& !el.getAttribute("href").startsWith("#");
			}).forEach((domNode) -> {
				DomElement el = (DomElement) domNode;
				String relativeUrl = el.getAttribute("href");
				HtmlPage currentPage = null;
				ProcessLogger.debug(relativeUrl);
				try {
					URL currentUrl = catalogPage.getFullyQualifiedUrl(relativeUrl);
					ProcessLogger.debug(currentUrl.toString());
					currentPage = webClient.getPage(currentUrl);
					ProcessLogger.info("Go to download page:" + currentUrl);
					HtmlPageTransfer pageTransfer = getHtmlPageTransfer(currentPage, config.getKeepDir().get());
					pageTransfer.transfer();
				} catch (Exception e) {
					ProcessLogger.error(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
				}
			});
		} catch (FailingHttpStatusCodeException | IOException | LostConfigureItemException e) {
			ProcessLogger.fatal(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
		}
	}

	/**
	 * @param htmlPage
	 * @param file 
	 * @return
	 */
	public abstract HtmlPageTransfer getHtmlPageTransfer(HtmlPage htmlPage, File file);

	protected void chainResolver(WebClient webClient) {
		String nextURL = config.getStartChainPageUrl().get();
		HtmlAnchor nextAnchor = null;
		ProcessLogger.info("Go to download the start page:" + nextURL);
		if (!config.getNextChainPageAnchorSelector().isPresent()) {//enhance for no next anchor
			HtmlPage currentPage;
			try {
				currentPage = webClient.getPage(nextURL);
				HtmlPageTransfer htmlPageTransfer = getHtmlPageTransfer(currentPage, config.getKeepDir().get());
				htmlPageTransfer.transfer();

				ProcessLogger.info("Go to download next page:" + nextURL);
			} catch (FailingHttpStatusCodeException | IOException e) {
				ProcessLogger.fatal(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
			}
			return;
			//throw new LostConfigureItemException("Lost configuration for next page link selector on each page.");
		}

		do {
			try {
				HtmlPage currentPage = webClient.getPage(nextURL);
				DomNode anchors = currentPage.getBody().querySelector(config.getNextChainPageAnchorSelector().get());
				nextAnchor = (HtmlAnchor) anchors;
				if (nextAnchor != null) {// issue for no found next anchor
					nextURL = currentPage.getFullyQualifiedUrl(nextAnchor.getHrefAttribute()).toString();
				}
				HtmlPageTransfer pageTransfer = getHtmlPageTransfer(currentPage, config.getKeepDir().get());
				pageTransfer.transfer();
				ProcessLogger.info("Go to download next page:" + nextURL);
			} catch (Exception e) {
				ProcessLogger.fatal(CommonUtilities.getCurrentInvokerMethod(), e.getMessage());
				nextAnchor = null;
			}
		} while (nextAnchor != null);
	}

	protected WebClient createWebClient() {
		if (config.getProxyHost() == null) {
			return new WebClient(config.getBrowserVersion());
		} else {
			return new WebClient(config.getBrowserVersion(), config.getProxyHost().get(), config.getProxyPort());
		}
	}

}
