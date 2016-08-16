package com.openthinks.others.webpages.additional;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

public interface AdditionalProcessor {

	void process(HtmlElement element);

	String process(String htmlContent);

}
