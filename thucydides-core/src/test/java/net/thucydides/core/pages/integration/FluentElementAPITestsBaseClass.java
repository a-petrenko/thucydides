package net.thucydides.core.pages.integration;


import net.thucydides.core.webdriver.WebDriverFacade;
import net.thucydides.core.webdriver.WebDriverFactory;
import net.thucydides.core.webdriver.WebdriverProxyFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

public class FluentElementAPITestsBaseClass {

    protected static WebDriver driver;
    protected static WebDriver chromeDriver;
    protected static WebDriver htmlUnitDriver;
    protected static StaticSitePage page;
//
//    @BeforeClass
//    public static void initDriver() {
//        driver = new WebDriverFacade(FirefoxDriver.class, new WebDriverFactory());
//        chromeDriver = new WebDriverFacade(ChromeDriver.class, new WebDriverFactory());
//        htmlUnitDriver = new WebDriverFacade(HtmlUnitDriver.class, new WebDriverFactory());
//        //page = new StaticSitePage(driver, 1);
//    }
//
//
//    @Before
//    public void openStaticPage() {
//        page.setWaitForTimeout(5000);
//        page.open();
//    }

    protected boolean runningOnLinux() {
        return System.getProperty("os.name").contains("Linux");
    }

}
