package com.openthinks.others.safaribook;

import java.util.List;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlMeta;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.openthinks.others.webpages.WebPagesLaunch;
import com.openthinks.others.webpages.additional.AdditionalBooks;
import com.openthinks.others.webpages.additional.AdditionalProcessor;
import com.openthinks.others.webpages.agent.HtmlPageResourceAgent;
import com.openthinks.others.webpages.conf.WebPagesConfigure;

/**
 * Safari book online getter entry
 * 
 * @author dailey.yet@outlook.com
 *
 */
public final class SafariBookLaunch extends WebPagesLaunch {

  public SafariBookLaunch() {
    initialAdditionals();
  }

  public SafariBookLaunch(WebPagesConfigure config) {
    super(config);
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
         * htmlContent.replaceAll("�t", "'t"); htmlContent = htmlContent.replaceAll("�ll", "'ll");
         * htmlContent = htmlContent.replaceAll("�re", "'re"); htmlContent =
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

}
