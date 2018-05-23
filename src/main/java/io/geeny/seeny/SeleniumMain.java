package io.geeny.seeny;

import io.geeny.seeny.Model.Tool.ZapProxy;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.geeny.seeny.Deserializer.*;
import io.geeny.seeny.Model.Elements.SeleniumElement;
import io.geeny.seeny.Model.Elements.WebsiteTestElement;
import io.geeny.seeny.Model.Tool.Config;
import io.geeny.seeny.Model.Tool.Driver;
import io.geeny.seeny.Model.Tool.Website;
import io.geeny.seeny.Model.Tool.WebsiteTest;

import io.geeny.seeny.Util.Enums;
import io.geeny.seeny.Util.HelpMethods;
import io.geeny.seeny.Util.Message;
import io.geeny.seeny.Util.VariableType;
import io.geeny.prettyprint.util.PrettyPrintConst;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Main Selenium Interface Framework class, which will handle the starting of the test
 *
 * @author Marco Bierbach & Klemen Samsa
 * version 1.0
 */
public class SeleniumMain {

    public static Config config;
    public static HelpMethods helper;
    public static String osName;

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(SeleniumElement.class, new SeenyElementDeserializer())
            .registerTypeAdapter(VariableType.NAVIGATE.class, new NavigationTypeDeserializer())
            .registerTypeAdapter(VariableType.TASK.class, new TaskTypeDeserializer())
            .registerTypeAdapter(VariableType.SELECTOR_TYPE.class, new SelectorTypeDeserializer())
            .registerTypeAdapter(VariableType.ACTION_TYPE.class, new ActionTypeDeserializer())
            .create();
    public static File tempdir;

    /**
     * Init Method to read the json file and handle the creation of the website object for this test.
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        //init the HelpMethod object for later usage
        helper = new HelpMethods();
        //init the config
        config = new Config();

        if (args.length > 0 && args[0].equals("--help")) {
            helper.initPrinter(PrettyPrintConst.PRINT_TYPE.CONSOLE); //as we exit anyway, just use console log
            helper.getPrinter().parseBoxStart();
            helper.getPrinter().parseText("Parameter List");
            helper.getPrinter().parseHeadline();
            helper.getPrinter().parseText(" Parameter           | Description");
            helper.getPrinter().parseSeperator();
            helper.getPrinter().parseText("-w <filename>        | parse a website test to seeny");
            helper.getPrinter().parseText("-d <drivername>      | loads a specific webdriver ");
            helper.getPrinter().parseText("-r <#num>            | set the amount of retries for one test" );
            helper.getPrinter().parseText("-t <#num>            | set the timout between each test run ");
            helper.getPrinter().parseText("--log-file-only      | only write the information to a logfile");
            helper.getPrinter().parseText("--log-console-only   | only write the information to the console");
            helper.getPrinter().parseText("--silent             | don't use the logfile or console at all");
            helper.getPrinter().parseSeperator();
            helper.getPrinter().parseText("Pretty Print Parameter List");
            helper.getPrinter().parseSeperator();
            helper.getPrinter().parseText(" Parameter           | Description");
            helper.getPrinter().parseText("-pp-width            | Set the width of the frame for the printout");
            helper.getPrinter().parseText("-pp-padding          | Set the padding for the framebox for the printout");
            helper.getPrinter().parseBoxEnd();
            return;
        } else {
            config.splitArgs(args);
            helper.initPrinter(config.getPrintType());
        }

        //save the os for later usage
        osName = System.getProperty("os.name");


        helper.getPrinter().parseBoxStart();
        helper.getPrinter().parseText(String.format(Message.M_0036, osName), PrettyPrintConst.COLOR.ANSI_GREEN);
        helper.getPrinter().parseSeperator();

        try {
            tempdir = Files.createTempDir();
            tempdir.deleteOnExit();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }


        String availableDriverContent = helper.getFileFromFolder("config","availableDriver");
        //check if there is a folder next to the jar file


        //Declare the type for beeing able to deserialize a generic collection of an object
        Type listType = new TypeToken<ArrayList<Driver>>(){}.getType();
        List<Driver> availableDrivers = gson.fromJson(availableDriverContent, listType);
        config.setAvailablDrivers(availableDrivers);

        if(config.getWebsitesToTest() == null) {
            //the setup of the config has not found any arguments which were valid, the system is loading the json now
            //check if there is a folder next to the jar file
            String availableWebsitesToTest = helper.getFileFromFolder("config","websitesToTest");


            Type websiteTestListType = new TypeToken<ArrayList<WebsiteTestElement>>() {
            }.getType();
            List<WebsiteTestElement> websitesToTest = gson.fromJson(availableWebsitesToTest, websiteTestListType);
            config.setWebsitesToTest(websitesToTest);
        }

        helper.getPrinter().writeToFile(PrettyPrintConst.LOG_FILE_TYPE.DEFAULT);

        for(WebsiteTestElement websiteTestElement : config.getWebsitesToTest()) {
            String websiteToTestContent = helper.getFileFromFolder("websites", websiteTestElement.getWebsiteName());

            helper.getPrinter().writeToFile(PrettyPrintConst.LOG_FILE_TYPE.DEFAULT);

            if (websiteToTestContent.isEmpty() == false) {
                //prepare
                Website websiteTestScript = gson.fromJson(websiteToTestContent, Website.class);


                WebsiteTest test = new WebsiteTest(websiteTestScript);

                for (int i = 0; i < websiteTestElement.getRetries(); i++) {

                    //clear the queue for the new run
                    helper.getMemoryQueue().clear();

                    try {

                        //get and set driver
                        Driver driver = config.getDriverForTest(websiteTestElement);
                        WebDriver webDriver = null;
                        File webdriverFile;

                        //load the File of the webdriver from the resource folder
                        String driverVersion = "mac";
                        if (osName.startsWith("Mac")) {
                            driverVersion = "mac";
                        } else if (osName.startsWith("Windows")) {
                            driverVersion = "windows.exe";
                        } else if (osName.startsWith("Linux")) {
                            driverVersion = "linux";
                        }
                        //set the os in the config, that we can use it for later
                        config.setOs(Enums.getOsByName(driverVersion));

                        webdriverFile = helper.getFileByTempFolder(tempdir, driver.getDriverPath() + driverVersion, "");

                        //set the property for the driver that the system knows it
                        System.setProperty(driver.getDriverPropertyName(), webdriverFile.getAbsolutePath());

                        if (driver.getName().startsWith("chrome")) {
                            if(test.isZapProxyEnabled()) {
                                ChromeOptions capabilities = prepareZapProxy(test.getWebsiteToTest().getZapProxy(), "chrome", ChromeOptions.class);
                                webDriver = new ChromeDriver(capabilities);
                                webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                            } else {
                                webDriver = new ChromeDriver();
                            }
                        } else if (driver.getName().startsWith("gecko")) {
                            if(test.isZapProxyEnabled()) {
                                FirefoxOptions capabilities = prepareZapProxy(test.getWebsiteToTest().getZapProxy(),"firefox", FirefoxOptions.class);
                                webDriver = new FirefoxDriver(capabilities);
                                webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                            } else {
                                webDriver = new FirefoxDriver();
                            }
                        }

                        //show window in foreground
                        String parentWindow = webDriver.getWindowHandle();
                        webDriver.switchTo().window(parentWindow);

                        helper.getPrinter().switchLogFileTo(PrettyPrintConst.LOG_FILE_TYPE.DEFAULT);
                        helper.getPrinter().parseHeadline();
                        helper.getPrinter().parseText(String.format(Message.M_0037, (i+1), websiteTestElement.getWebsiteName()), PrettyPrintConst.COLOR.ANSI_CYAN);

                        //set the currentWebsite to test and add a new logFile to the printer
                        helper.setCurrentTestName(websiteTestElement.getWebsiteName());
                        helper.getPrinter().useCustomLog(websiteTestElement.getWebsiteName());

                        Enums.TEST_STATUS result = test.startTest(webDriver);

                        if (websiteTestScript.getClose()) {
                            //what is close for? quit is the new close :D
                            //chrome.close();
                            webDriver.quit();
                        }

                        if(config.getPrintType().equals(PrettyPrintConst.PRINT_TYPE.SILENT) == false) {
                            helper.getPrinter().switchLogFileTo(PrettyPrintConst.LOG_FILE_TYPE.CUSTOM); //switch to the info file
                            helper.getPrinter().parseText(String.format(Message.M_0038, (i+1), websiteTestElement.getWebsiteName(), result.getMessage()),result.equals(Enums.TEST_STATUS.SUCCESS)? PrettyPrintConst.COLOR.ANSI_GREEN : PrettyPrintConst.COLOR.ANSI_RED);
                            helper.getPrinter().switchLogFileTo(PrettyPrintConst.LOG_FILE_TYPE.DEFAULT);
                        }

                        helper.getPrinter().parseSeperator();

                        if(websiteTestElement.getRetries() > 1 ) {
                            //now check for the delay
                            helper.getPrinter().parseText(
                                    String.format(Message.M_0039, websiteTestElement.getTimeout()));
                            Thread.sleep(websiteTestElement.getTimeout() * 1000);
                            helper.getMemoryQueue().clear(); //for safety reasons
                        }
                    } catch (InterruptedException e) {
                        helper.getPrinter().switchLogFileTo(PrettyPrintConst.LOG_FILE_TYPE.DEFAULT);
                        helper.getPrinter().parseText(Message.M_0040, PrettyPrintConst.COLOR.ANSI_RED);
                        helper.getPrinter().parseBoxEnd();
                    }

                    helper.getPrinter().writeToFile(PrettyPrintConst.LOG_FILE_TYPE.CUSTOM);
                }

                helper.getPrinter().switchLogFileTo(PrettyPrintConst.LOG_FILE_TYPE.DEFAULT);
                helper.getPrinter().parseText(String.format(Message.M_0041, websiteTestElement.getRetries(), (websiteTestElement.getRetries() > 1)? "s" : "", websiteTestElement.getWebsiteName()));
                helper.getPrinter().parseBoxEnd();
            } else {
                helper.getPrinter().parseText(String.format(Message.M_0042, websiteTestElement.getWebsiteName()), PrettyPrintConst.COLOR.ANSI_RED);
                helper.getPrinter().parseBoxEnd();
            }
            helper.getPrinter().writeToFile(PrettyPrintConst.LOG_FILE_TYPE.DEFAULT);
        }

    }

    public static <T extends MutableCapabilities> T prepareZapProxy(ZapProxy _proxy, String _webDriverType, Class<T> type) {
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(_proxy.getUrl()+":"+_proxy.getPort());
        proxy.setFtpProxy(_proxy.getUrl()+":"+_proxy.getPort());
        proxy.setSslProxy(_proxy.getUrl()+":"+_proxy.getPort());

        MutableCapabilities options = null;
        if(_webDriverType.equals("chrome")) {
            options = new ChromeOptions();
            options.setCapability(CapabilityType.PROXY, proxy);
        } else if(_webDriverType.equals("firefox")) {
            options = new FirefoxOptions();
            options.setCapability(CapabilityType.PROXY, proxy);
        }

        return type.cast(options);
    }
}
