package com.openthinks.others.safaribook;

import java.io.File;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.openthinks.others.webpages.HtmlPageTransfer;

public class SafariBookPageTransfer extends HtmlPageTransfer {
	private File commonDir = null;

	private SafariBookPageTransfer(HtmlPage htmlPage, File keepDir) {
		super(htmlPage, keepDir);
		this.commonDir = new File(keepDir.getParentFile(), COMMON_RESOURCE_DIR);
	}

	public static SafariBookPageTransfer create(HtmlPage htmlPage, File keepDir) {
		return new SafariBookPageTransfer(htmlPage, keepDir);
	}

	///////////////////////////////////////////////////////////////////////////
	public static final String RESOURCE_STYLE_DIR = "style";
	public static final String RESOURCE_SCRIPT_DIR = "js";
	public static final String RESOURCE_IMAGE_DIR = "images";
	public static final String RESOURCE_VIDEO_DIR = "videos";

	public static final String COMMON_RESOURCE_DIR = "common";

	public static final String RESOURCE_STYLE_REFERENCE_DIR = "styleref";
	public static final String RESOURCE_STYLE_REFERENCE_REGEX = "url\\(['\"]?([^\\(\\)'\"]+)['\"]?\\)";
	public static final Pattern RESOURCE_STYLE_REFERENCE_PATTERN = Pattern.compile(RESOURCE_STYLE_REFERENCE_REGEX);

	@Override
	public File getJsKeepDir() {
		return new File(commonDir, RESOURCE_SCRIPT_DIR);
	}

	@Override
	public String getJsPath() {
		return "../common/" + RESOURCE_SCRIPT_DIR;
	}

	@Override
	public File getCssKeepDir() {
		return new File(commonDir, RESOURCE_STYLE_DIR);
	}

	@Override
	public String getCssPath() {
		return "../common/" + RESOURCE_STYLE_DIR;
	}

}
