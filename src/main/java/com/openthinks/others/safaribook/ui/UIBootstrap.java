/**
 * 
 */
package com.openthinks.others.safaribook.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.apache.xpath.operations.String;

/**
 * @author dailey.yet@outlook.com
 *
 */
public final class UIBootstrap implements Runnable {
  private UIBootstrap() {}

  public static void main(String[] args) {
    UIBootstrap instance = new UIBootstrap();
    // instance.run();
    SwingUtilities.invokeLater(instance);
  }

  @Override
  public void run() {
    SafariBookDownloadPanel mainPanel = new SafariBookDownloadPanel();
    mainPanel.setLauncher(new SafariBookUILauncher());
    JFrame frame = new JFrame("SafariBook Downloader");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.setPreferredSize(new Dimension(500, 400));

    frame.setLayout(new BorderLayout());
    // frame.add(mainPanel, BorderLayout.CENTER);
    frame.setVisible(true);
  }

}
