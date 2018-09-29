/**
 * 
 */
package com.openthinks.others.safaribook.ui;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.DefaultListModel;

import com.openthinks.libs.utilities.CommonUtilities;
import com.openthinks.libs.utilities.logger.ProcessLogger;
import com.openthinks.others.safaribook.DownloadBookTaskInfo;
import com.openthinks.others.safaribook.SafariBookConfigure;
import com.openthinks.others.safaribook.SafariBookLaunch;
import com.openthinks.others.webpages.exception.LaunchFailedException;

/**
 * @author dailey.yet@outlook.com
 */
final class SafariBookUILauncher {
	private final BlockingQueue<DownloadBookTaskInfo> bookQueue = new LinkedBlockingQueue<>();
	private transient boolean isRunning = false;
	private transient SafariBookConfigure baseConfig = null;
	private final ExecutorService executorService;
	private Future<?> mainFuture;
	private Map<Class<?>, LanucherPanel> uis = new ConcurrentHashMap<>();

	SafariBookUILauncher() {
		this.executorService = Executors.newScheduledThreadPool(10);
	}

	void start(SafariBookConfigure baseConfig) {
		if (isRunning == true) {
			ProcessLogger.warn("SafariBookUILauncher is already started");
			return;
		}
		this.baseConfig = baseConfig;
		isRunning = true;
		if (baseConfig == null)
			return;
		if (hasGroupTask(baseConfig)) {
			mainFuture = executorService.submit(() -> {
				doGroupTask(baseConfig);
			});
		} else {
			ProcessLogger.fatal("Only support execute group task, not support single task in UI mode.");
		}
	}

	void stop() {
		isRunning = false;
		if (mainFuture != null) {
			mainFuture.cancel(true);
		}
		this.executorService.shutdownNow();
	}

	void addBook(DownloadBookTaskInfo downloadBook) {
		bookQueue.offer(downloadBook);
	}

	void register(LanucherPanel lanucherPanel) {
		uis.put(lanucherPanel.getClass(), lanucherPanel);
	}

	@SuppressWarnings("unchecked")
	<T extends LanucherPanel> T getUI(Class<T> clazz) {
		return (T) uis.get(clazz);
	}

	private void doGroupTask(final SafariBookConfigure config) {
		String groupTaskDefDir = config.getGroupTaskDir().get();
		if (groupTaskDefDir == null || groupTaskDefDir.trim().length() == 0) {
			ProcessLogger.fatal("Configure item {0} cannot be empty.", SafariBookConfigure.DOWNLOADGROUPTASKDIR);
			return;
		}
		File dir = new File(groupTaskDefDir.trim());
		if (!dir.exists()) {
			ProcessLogger.warn("Configure item {0}:{1} is not exist in local file system.",
					SafariBookConfigure.DOWNLOADGROUPTASKDIR, groupTaskDefDir);
		} else {
			loadConfDirBook(config, dir);
		}
		while (isRunning && !Thread.currentThread().isInterrupted()) {
			try {
				DownloadBookTaskInfo task = bookQueue.take();
				executeSingleTask(task);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}

	}

	private void loadConfDirBook(final SafariBookConfigure config, File dir) {
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
		taskInfos.stream().filter(ti -> {
			if (ti.getIsProcessed().isPresent()) {
				return !ti.getIsProcessed().get().trim().equalsIgnoreCase("true");
			} else {
				return true;
			}
		}).forEach(ti -> {
			bookQueue.add(ti);
		});
	}

	private SafariBookConfigure wrapper(DownloadBookTaskInfo bookTaskInfo) {
		SafariBookConfigure cf = (SafariBookConfigure) baseConfig.clone();
		cf.setBookTaskInfo(bookTaskInfo);
		cf.setBookName(bookTaskInfo.getBookName().orElse(null));
		cf.setCatalogPageUrl(bookTaskInfo.getCatalogPageUrl().orElse(null));
		cf.setPageLinkOfCatalogSelector(bookTaskInfo.getPageLinkOfCatalogSelector().orElse(null));
		return cf;
	}

	private void executeSingleTask(final DownloadBookTaskInfo task) {
		SafariBookConfigure cf = wrapper(task);
		SafariBookLaunch bookLaunch = new SafariBookLaunch(cf);
		try {
			bookLaunch.start();
			cf.getBookTaskInfo().ifPresent(ti -> {
				ti.setIsProcessed("true");
			});
		} catch (LaunchFailedException e) {
			ProcessLogger.fatal(CommonUtilities.getCurrentInvokerMethod(), e);
			cf.getBookTaskInfo().ifPresent(ti -> {
				ti.setIsProcessed("false");
			});
		}
		cf.getBookTaskInfo().ifPresent(ti -> {
			ti.keep();
		});
	}

	private boolean hasGroupTask(SafariBookConfigure config) {
		return config.getGroupTaskDir().isPresent();
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	String getBookNumberFromUrl(String url) {
		String tmp = url.trim();
		while(tmp.lastIndexOf("/") == tmp.length()-1) {
			tmp = tmp.substring(0, tmp.length()-1);
		}
		int lastToken = tmp.lastIndexOf("/");
		
		return tmp.substring(lastToken+1);
	}
	final ActionListener BOOK_ADD = (e) -> {
		OperatePanel UI1 = getUI(OperatePanel.class);
		String bookName = UI1.textBookName.getText().trim();
		String bookUrl =  UI1.textURL.getText().trim();
		if(bookName.length()==0 || bookUrl.length()==0) {
			ProcessLogger.warn("Book name or catalog page url must not be empty.");
			return;
		}
		DownloadBookTaskInfo task = new DownloadBookTaskInfo();
		task.setProperty(DownloadBookTaskInfo.BOOKNAME, bookName);
		task.setProperty(DownloadBookTaskInfo.CATALOGPAGEURL, bookUrl);
		task.setProperty(DownloadBookTaskInfo.PAGELINKOFCATALOGSELECTOR, "div.catalog_container a[href*='"+getBookNumberFromUrl(bookUrl)+"']");
		task.setProperty(DownloadBookTaskInfo.DOWNLOADED, "false");
		WorklistPanel UI2 = getUI(WorklistPanel.class);
		DefaultListModel<DownloadBookTaskInfo> listModel = (DefaultListModel<DownloadBookTaskInfo>) UI2.listWorklist.getModel();
		bookQueue.offer(task);
		listModel.addElement(task);
	};

	final ActionListener START = (e) -> {
	};

	final ActionListener STOP = (e) -> {
	};

	
}
