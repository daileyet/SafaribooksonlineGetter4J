/**
 * 
 */
package com.openthinks.others.safaribook.ui;

import javax.swing.JPanel;

/**
 * @author dailey.yet@outlook.com
 *
 */
public abstract class LanucherPanel extends JPanel {
  private static final long serialVersionUID = -7944412115559867869L;
  protected SafariBookUILauncher launcher;

  final SafariBookUILauncher launcher() {
    return launcher;
  }

  void setLauncher(final SafariBookUILauncher launcher) {
    this.launcher = launcher;
    this.launcher.register(this);
  }
}
