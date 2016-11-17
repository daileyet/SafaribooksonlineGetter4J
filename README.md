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

### Book structure on disk
When you set download book save directory as W:\Book on windows OS or /Book on Linux OS, then you will get following download book structure:
```
|
|-- **common**
|---- **js**
|------ js 1
|------ js 2
|------ ...
|---- **style**
|------ css 1
|------ css 2
|------ ...
|------ **styleref**
|-------- image 1
|-------- image 2
|-------- ...
|-- *download book name 1*
|---- **images**
|------ image 1
|------ image 2
|------ ...
|---- page 1
|---- page 2
|---- ...
|-- *download book name 2*
|---- **images**
|------ image 1
|------ image 2
|------ ...
|---- page 1
|---- page 2
|---- ...
```
### Reference project
This project has been used as a task type in [WebScheduler](https://github.com/daileyet/webscheduler) system.
![Used in WebScheduler](https://ggkwjw.bn1303.livefilestore.com/y3miOVDLzcZySfoSGCZ2nAnLOOzGA__wEx1PTfpg5nnTnxVUzkOdmZT5rZXJAVcviYu8kMIKEjUB04doOlH7JW-GVj4zBpMuF3_6WIy2T-djW1GNY9mYKE6VtI7Yl7U7DllqHj0ZLeWVVdS8Ahl6Zjg-LIYwRZKkpf8FCn9HF_sJBc?width=1319&height=838&cropmode=none "Used in WebScheduler")

[1]:http://techbus.safaribooksonline.com/
