package com.xlm.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SeleniumMain {
    static {
        System.setProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "D:\\download\\phantomjs-2.1.1-windows\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
    }
    public static void main1(String[] args) {

        PhantomJSDriver driver = getDriver();
        driver.get("https://list.tmall.com/search_product.htm?spm=a221t.1710963.8073444875.1.6b291135b3qB61&q=%CF%C4%BC%BE&cat=53636001&active=1&style=g&from=sn_1_rightnav&acm=lb-zebra-7499-262419.1003.4.408088&sort=s&search_condition=23&scm=1003.4.lb-zebra-7499-262419.OTHER_14895286190510_408088#J_crumbs");
        List<WebElement> webElements = driver.findElementsByClassName("product");
        List<WebElement> detailList = new ArrayList<>(webElements.size());
        int i = 0;
        for (WebElement webElement : webElements) {
            if (webElement.getAttribute("class").indexOf("album-new") > 0) {
                continue;
            }
            detailList.add(webElement.findElement(By.className("productImg")));
            System.out.println(i++);
        }

        System.out.println(detailList);
    }
    public static void main(String[] args){
        PhantomJSDriver driver = getDriver();
        driver.get("https://www.tmall.com");
        System.out.println(driver.getPageSource());
        WebElement webElement  = driver.findElementByName("q");
        webElement.sendKeys("女单鞋");
        WebElement submit = driver.findElementByCssSelector("form[name=searchTop] button");
        submit.click();
        System.out.println(driver.getPageSource());

    }

    public static PhantomJSDriver getDriver() {
        DesiredCapabilities dcaps = new DesiredCapabilities();
        //ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
        //截屏支持
        dcaps.setCapability("takesScreenshot", true);
        //css搜索支持
        dcaps.setCapability("cssSelectorsEnabled", true);
        dcaps.setCapability("phantomjs.page.settings.XSSAuditingEnabled", true);
        dcaps.setCapability("phantomjs.page.settings.webSecurityEnabled", false);
        dcaps.setCapability("phantomjs.page.settings.localToRemoteUrlAccessEnabled", true);
        dcaps.setCapability("phantomjs.page.settings.XSSAuditingEnabled", true);

        dcaps.setCapability("phantomjs.page.settings.loadImages", false);
        //js支持
        dcaps.setJavascriptEnabled(true);
        //驱动支持
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, System.getProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY));
        //dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,systemProps.getPhantomjsPath());

        //dcaps.setCapability("phantomjs.page.settings.userAgent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
        //dcaps.setCapability("phantomjs.page.customHeaders.User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:50.0) Gecko/20100101 　　Firefox/50.0");
        dcaps.setCapability("ignoreProtectedModeSettings", true);
//        org.openqa.selenium.Proxy proxy = new org.openqa.selenium.Proxy();
//        proxy.setProxyType(org.openqa.selenium.Proxy.ProxyType.MANUAL);
//        proxy.setHttpProxy("http://180.155.128.87:47593/");
//        dcaps.setCapability(CapabilityType.PROXY, proxy);


        //创建无界面浏览器对象
        PhantomJSDriver driver = new PhantomJSDriver(dcaps);
        driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(120, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();
        driver.manage().window().setSize(new Dimension(1920, 1080));
        return driver;
    }
}
