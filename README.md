# SafaribooksonlineGetter4J
The auto download for [Safari online book][1]

### How to use it?
Execute the following command line:
```
java -jar SafariBookGetter.jar -config W:\Book\default_config.xml
```

### Configuration items
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
<!--[option]Browser client: FF31; FF38; IE8; IE11; CHROME-->
<entry key="browser-version">FF38</entry>
<!--[option]proxy host if present-->
<entry key="proxy-host"></entry>
<!--[option]proxy host port-->
<entry key="proxy-port">80</entry>
<!--[required]download save directory-->
<entry key="save-dir">W:\Book\android-studio-essentials</entry>
<!--[required]identity the authorized for the download pages-->
<entry key="need-login">true</entry>

<!--required when need login-->
<!--[required]the login page url-->
<entry key="login-url">https://www.safaribooksonline.com/accounts/login/</entry>
<!--[required]the login page form css selector-->
<entry key="login-form-selector">form</entry>
<!--[option]the login page form index-->
<entry key="login-form-index">0</entry>
<!--[required]the login page form user name input name-->
<entry key="login-form-username-input-name"></entry>
<!--[required]the login page form user name value-->
<entry key="login-form-username-input-value"></entry>
<!--[required]the login page form user pass input name-->
<entry key="login-form-password-input-name"></entry>
<!--[required]the login page form user pass value-->
<entry key="login-form-password-input-value"></entry>
<!--[required]the login page form submit button name-->
<entry key="login-form-submit-name"></entry>

<!--option when the first page url configured-->
<!--The catalog page url-->
<entry key="pages-catalog-url">http://techbus.safaribooksonline.com/book/programming/android/9781784397203</entry>
<!--The css selector for each download page anchor on catalog page-->
<entry key="catalog-pagelinks-selector">div.catalog_container a[href*='9781784397203']</entry>


<!--option when the catalog page url configured-->
<!--The first page url-->
<entry key="pages-first-url">http://techbus.safaribooksonline.com/book/programming/android/9781784397203/android-studio-essentials/index_html</entry>
<!--The css selector for next chain page anchor on each page -->
<entry key="pages-next-anchor-selector">a.next[title*='Next (Key: n)']</entry>

<!--show message in CMD-->
<entry key="logger-level">INFO</entry>
</properties>

```

[1]:http://techbus.safaribooksonline.com/
