package com.openthinks.others.safaribook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlMeta;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.openthinks.libs.utilities.CommonUtilities;
import com.openthinks.libs.utilities.logger.ProcessLogger;
import com.openthinks.others.webpages.HtmlPageTransfer;
import com.openthinks.others.webpages.WebPagesLaunch;
import com.openthinks.others.webpages.additional.AdditionalBooks;
import com.openthinks.others.webpages.additional.AdditionalProcessor;
import com.openthinks.others.webpages.agent.HtmlPageResourceAgent;
import com.openthinks.others.webpages.exception.LaunchFailedException;
import com.openthinks.others.webpages.exception.LostConfigureItemException;

/**
 * Safari book online getter entry
 * 
 * @author dailey.yet@outlook.com
 *
 */
public final class SafariBookLaunch extends WebPagesLaunch {


	public SafariBookLaunch(SafariBookConfigure config) {
		super();
		this.config = config;
		initialAdditionals();
	}

	private void initialAdditionals() {
		AdditionalBooks.register(HtmlPageResourceAgent.class, new AdditionalProcessor() {

			@Override
			public void process(HtmlElement element) {
				clearScript(element);
				clearLoading(element);
			}

			private void clearLoading(HtmlElement element) {
				HtmlPage page = (HtmlPage) element.getPage();
				HtmlElement body = page.getBody();

				List<Object> images = (List<Object>) body.getByXPath("//img[contains(@src,'loading.gif')]");
				for (Object obj : images) {
					if (obj instanceof DomNode) {
						((DomNode) obj).remove();
					}
				}

			}

			/**
			 * @param element
			 */
			protected void clearScript(HtmlElement element) {
				HtmlPage page = (HtmlPage) element.getPage();
				HtmlElement head = page.getHead();
				head.getElementsByTagName("script").stream().filter((HtmlElement script) -> {
					return !script.hasAttribute("src")
							&& "text/javascript".equalsIgnoreCase(script.getAttribute("type"))
							&& script.getTextContent().contains("CookieState");
				}).forEach((HtmlElement script) -> {
					script.setTextContent("");
					// clear script
				});

				HtmlMeta metaElement = (HtmlMeta) page.createElement("meta");
				metaElement.setAttribute("content", "text/html; charset=utf-8");
				metaElement.setAttribute("http-equiv", "Content-Type");
				head.appendChild(metaElement);
			}

			@Override
			public String process(String htmlContent) {
				/*
				 * htmlContent = htmlContent.replaceAll("�s", "'s"); htmlContent =
				 * htmlContent.replaceAll("�t", "'t"); htmlContent =
				 * htmlContent.replaceAll("�ll", "'ll"); htmlContent =
				 * htmlContent.replaceAll("�re", "'re"); htmlContent =
				 * htmlContent.replaceAll("�ve", "'ve");
				 * 
				 * htmlContent = htmlContent.replaceAll("�(?=(re|s|t|ll|ve))", "'");
				 * 
				 * htmlContent = htmlContent.replaceAll("ï¿½ï¿½", "'"); htmlContent =
				 * htmlContent.replaceAll("\\?\\?", "&nbsp;&nbsp;");
				 */
				htmlContent = htmlContent.replaceAll("�", " ");
				return htmlContent;
			}
		});
	}

	public void start() throws LaunchFailedException {
		ProcessLogger.debug("SafariBookLanuch start...");
		try {
			LogManager.getLogManager()
					.readConfiguration(SafariBookLaunch.class.getResourceAsStream("/logging.properties"));
		} catch (SecurityException | IOException e1) {
			ProcessLogger.warn(CommonUtilities.getCurrentInvokerMethod(), e1);
		}
		ProcessLogger.currentLevel = config.getLoggerLevel();
		try {
			this.doSafariBookConfigure();
			this.launch();
		} catch (SecurityException | IOException | LostConfigureItemException e) {
			ProcessLogger.fatal(CommonUtilities.getCurrentInvokerMethod(), e);
			throw new LaunchFailedException(e);
		}
	}

	/**
	 * 
	 */
	protected void doSafariBookConfigure() {
		SafariBookConfigure sbConfig = (SafariBookConfigure) this.config;
		if (sbConfig.getBookName().isPresent()) {
			String bookFolder = sbConfig.getBookName().get();
			File finalKeepDir = new File(sbConfig.getKeepDir().get(), bookFolder);
			sbConfig.setKeepDir(finalKeepDir.getAbsolutePath());
		}
	}

	public static void main(String[] args) throws SecurityException, IOException {
		SafariBookConfigure config = initialConfig(args);
		// remove script
		/*
		 * <script language="javascript" type="text/javascript"> //<![CDATA[ var
		 * CookieState =
		 * 'uicode=oracle&PromoCode=&itemsperpage=10&view=book&xmlid=9781783988020%
		 * 2findex_html&reader=html&displaygrbooks=0&isbn=9781783988020&portal=techbus&
		 * omniturexmlidown=1'; var Xsl = { _1 :
		 * '/static/201508\u002D7989\u002Dtechbus', _2 : '2015/08/21T08/35/37', _3 :
		 * '9781783988020/index_html', _4 : 'techbus', _5 :
		 * '4F027A71A5A648D480DF1A1FE9C361B3129ADEF308D44217FC0C6F9580BC3428FB971F59A3002BDA0853EE835C29AB929EBBAEB21BF2FEC6A0D3A600F9410F10C3E0EBDE4AF6334092FA557971869710F7726285917F8F6FA903DB29917EDB160CA9E04CFABDD38A380F7F07C52F2830715EF5466791611AA1AC9C8C6AF9AD98
		 * ', _6 : '9b908e61\u002Dc2a7\u002D4dc4\u002Db216\u002Dd49d03df90cf', _7 :
		 * 'Undefined', _8 : '6.0.3', _9 : '', _10 : '0', _11 : '/book', _12 : '', _13 :
		 * '1', _14 : '0', _15 : '3645443', _16 : '', _17 : '', _18 : '1', _19 : '' };
		 * var BvD_StaticPath = Xsl._1; //]]> </script>
		 */
		if (config == null)
			return;
		if (hasGroupTask(config)) {
			doGroupTask(config);
		} else {
			SafariBookLaunch bookLaunch = new SafariBookLaunch(config);
			try {
				bookLaunch.start();
			} catch (LaunchFailedException e) {
				ProcessLogger.fatal(CommonUtilities.getCurrentInvokerMethod(), e);
			}
		}
	}

	protected static SafariBookConfigure initialConfig(String[] args) {
		String config_path = null;
		if (args != null && args.length > 0) {
			if ("-help".equalsIgnoreCase(args[0])) {
				showUsage();
				return null;
			}
			if ("-config".equalsIgnoreCase(args[0])) {
				if (args.length >= 2) {
					config_path = args[1];
				} else {
					System.out.println("miss configure file path!");
					showUsage();
					return null;
				}
			}
		} else {
			showUsage();
			return null;
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

	@Override
	public HtmlPageTransfer getHtmlPageTransfer(HtmlPage htmlPage, File file) {
		return SafariBookPageTransfer.create(htmlPage, file);
	}

	///////////////////////////////////////////////////////////////////////////////////
	private static void doGroupTask(SafariBookConfigure config) {
		String groupTaskDefDir = config.getGroupTaskDir().get();
		if (groupTaskDefDir == null || groupTaskDefDir.trim().length() == 0) {
			ProcessLogger.fatal("Configure item {0} cannot be empty.", SafariBookConfigure.DOWNLOADGROUPTASKDIR);
			return;
		}
		File dir = new File(groupTaskDefDir.trim());
		if (!dir.exists()) {
			ProcessLogger.fatal("Configure item {0}:{1} is not exist in local file system.",
					SafariBookConfigure.DOWNLOADGROUPTASKDIR, groupTaskDefDir);
			return;
		}
		File[] acceptXmlFiles = dir.listFiles((d, n) -> {
			return n.endsWith(DownloadBookTaskInfo.CONFIG_XML);
		});

		File[] acceptPropFiles = dir.listFiles((d, n) -> {
			return n.endsWith(DownloadBookTaskInfo.CONFIG_PROPERTIES);
		});

		List<DownloadBookTaskInfo> taskInfos = new ArrayList<>();
		for (File xml : acceptXmlFiles) {
			try {
				DownloadBookTaskInfo task = DownloadBookTaskInfo.readXML(xml);
				taskInfos.add(task);
			} catch (IOException e) {
				ProcessLogger.warn("Failed to read task configuration:{0} by reason:{1}", xml, e);
			}
		}
		for (File prop : acceptPropFiles) {
			try {
				DownloadBookTaskInfo task = DownloadBookTaskInfo.readProps(prop);
				taskInfos.add(task);
			} catch (IOException e) {
				ProcessLogger.warn("Failed to read task configuration:{0} by reason:{1}", prop, e);
			}
		}
		List<SafariBookConfigure> configList = new ArrayList<>();
		taskInfos.stream().filter(ti -> {
			if (ti.getIsProcessed().isPresent()) {
				return !ti.getIsProcessed().get().trim().equalsIgnoreCase("true");
			} else {
				return true;
			}
		}).forEach(ti -> {
			SafariBookConfigure cf = (SafariBookConfigure) config.clone();
			cf.setBookTaskInfo(ti);
			cf.setBookName(ti.getBookName().orElse(null));
			cf.setCatalogPageUrl(ti.getCatalogPageUrl().orElse(null));
			cf.setPageLinkOfCatalogSelector(ti.getPageLinkOfCatalogSelector().orElse(null));
			configList.add(cf);
		});
		
		for(SafariBookConfigure cf:configList) {
			SafariBookLaunch bookLaunch = new SafariBookLaunch(cf);
			try {
				bookLaunch.start();
				cf.getBookTaskInfo().ifPresent(ti->{
					ti.setIsProcessed("true");
				});
			} catch (LaunchFailedException e) {
				ProcessLogger.fatal(CommonUtilities.getCurrentInvokerMethod(), e);
				cf.getBookTaskInfo().ifPresent(ti->{
					ti.setIsProcessed("false");
				});
			}
			cf.getBookTaskInfo().ifPresent(ti->{
				ti.keep();
			});
		}
		
	}

	private static boolean hasGroupTask(SafariBookConfigure config) {
		return config.getGroupTaskDir().isPresent();
	}
}