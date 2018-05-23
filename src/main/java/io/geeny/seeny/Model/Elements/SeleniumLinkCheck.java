package io.geeny.seeny.Model.Elements;

import io.geeny.seeny.Model.Elements.SubElements.PageToCheck;
import io.geeny.seeny.Util.Enums.*;
import io.geeny.seeny.Util.Message;
import io.geeny.seeny.Util.Timer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.geeny.prettyprint.util.PrettyPrintConst;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * io.geeny.seeny.Model class for a link check object of the script framework
 * @author Marco Bierbach & Klemen Samsa
 * @version 1.0
 * */
public class SeleniumLinkCheck extends SeleniumElement {

    @SerializedName("domain")
    @Expose
    private String domain;

    @SerializedName("subdomain")
    @Expose
    private String subdomain = ".*";

    @SerializedName("https")
    @Expose
    private boolean https = false;

    @SerializedName("depth")
    @Expose
    private int depth = 0;

    @SerializedName("excludes")
    @Expose
    private List<String> exludedRoutes = new ArrayList<>();

    @SerializedName("recursive")
    @Expose
    private boolean recursive = false;


    private Map<String,PageToCheck> knownUrls = new HashMap<>();

    private static String getStatusMessageForStatusCode(int httpcode) {
        String returnStatusMessage = "Status Not Defined";
        for (URL_STATUS object : URL_STATUS.values()) {
            if (object.getStatusCode() == httpcode) {
                returnStatusMessage = object.getHttpMessage();
            }
        }
        return returnStatusMessage;
    }

    private boolean isURL(String URL){
        try{

            //if .js, it doesnt matter
            if (URL.contains(".js")){
                return false;
            }

            String urlRegex = "^(http|https)://[-a-zA-Z0-9+&@#/%?=~_|,!:.;]*[-a-zA-Z0-9+@#/%=&_|]*";
            Pattern pattern = Pattern.compile(urlRegex);
            Matcher m = pattern.matcher(URL);

            if (m.matches()) {
                return true;
            } else {
                return false;
            }
        }
        catch (Exception e){

        }
        return false;
    }

    private URL_STATUS validateLink (String testedUrl) {

        URL url;
        HttpURLConnection myConnection = null;

        try {
            url = new URL(testedUrl);
            myConnection = (HttpURLConnection) url.openConnection();
            myConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.94 Safari/537.36");
            myConnection.setRequestProperty("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            myConnection.setRequestProperty("accept-encoding", "gzip, deflate, sdch, br");

            printer.parseSeperator();

            boolean result = false;
            URL_STATUS status = URL_STATUS.getURLStatusByCode(myConnection.getResponseCode());

            printer.queueText(String.format(Message.M_0013, testedUrl));
            printer.queueText(status.getHttpMessage(), status.getColorCode());
            printer.queueText(String.format(Message.M_0014, status.getStatusCode()));
            printer.parseQueue();
            return status;
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException | IOException e) {
            printer.queueText(String.format(Message.M_0015, testedUrl));
            printer.queueText(e.getMessage(), PrettyPrintConst.COLOR.ANSI_RED);
            printer.queueText("]");
            printer.parseQueue();
            if(testedUrl.contains("localhost")) {
                return URL_STATUS.LOCALHOST;
            } else {
                return URL_STATUS.CONNECTION_REFUSED;
            }
        } finally {
            myConnection.disconnect();
        }
    }

    @Override
    public TEST_STEP_STATUS execute(WebDriver _driver, WebDriverWait _wait, WebElement _lastElement) {
        setDriver(_driver);
        try {
            printer.parseText(String.format(Message.M_0016, this.getDescription()));

            Thread.sleep(getWaitBefore());

            TEST_STEP_STATUS action = action();

            Thread.sleep(getWaitAfter());

            return action;
        } catch (Exception e) {
            printer.parseText("Linkcheck could not be executed");
            e.printStackTrace();
        }
        return TEST_STEP_STATUS.FAILURE;
    }

    @Override
    public TEST_STEP_STATUS execute() {
        return null;
    }

    /**
     * Method to prepare a string which shows the configuration settings of the link check
     * @return
     */
    private String pepareConfigurationString(){
        return String.format("Domain: %s, ", domain) +
                ((subdomain != ".*")? String.format("Subdomain: %s, ", subdomain) : "") +
                (String.format("Recursive: %b, ", recursive)) +
                ((recursive && depth != 0)? String.format("Depth: %d, ", depth) : "") +
                (String.format(" Https:%b", https));
    }

    @Override
    public TEST_STEP_STATUS action() {
        Timer stopClock = new Timer();

        stopClock.start();

        int currentDepth = 0;

        printer.parseSeperator();
        printer.parseText(Message.M_0017);
        printer.parseText(pepareConfigurationString());
        printer.parseSeperator();


        //collect all links from the websites
        printer.parseText(Message.M_0018);
        collectLinksFromWebpage(currentDepth);


        printer.parseText(Message.M_0019);
        verifyHealthinessOfLinks();


        printer.parseSeperator();
        printer.parseText(Message.M_0020);

        TEST_STEP_STATUS success = TEST_STEP_STATUS.OK;
        //check if all links are healthy
        if(allLinksAreHealthy()){
            //seems there are no error pages
            //check if we have warnings
            printer.parseText(Message.M_0021);
            printer.parseSeperator();
            List<PageToCheck> warnings = getPagesByUrlStatus(URL_STATUS.LOCALHOST);
            if(warnings.size()>0){
                printer.parseText(String.format(Message.M_0022, warnings.size(), warnings.size()>1? "s" : ""));
                printPageLinkAndParents(warnings);
            }
        } else {
            printer.parseSeperator();
            //not all links are healthy grab the broken links and show them
            printer.parseText(Message.M_0023);

            //grab all links
            URL_STATUS[] urlStatusList = URL_STATUS.values();

            for(URL_STATUS urlStatus : urlStatusList){
                if((urlStatus.equals(URL_STATUS.HTTP_OK)) == false) {
                    List<PageToCheck> pages = getPagesByUrlStatus(urlStatus);
                    if (pages.size() > 0) {
                        if(urlStatus != URL_STATUS.LOCALHOST || urlStatus != URL_STATUS.LINKEDIN) {
                            // if not all links are healthy and it is one of the list except linkedin or localhost,
                            // then the link check is not successful
                            success = TEST_STEP_STATUS.FAILURE;
                        }

                        printer.parseSeperator();
                        printer.queueText(String.format(Message.M_0024, urlStatus.getStatusCode()));
                        printer.parseSeperator();
                        printer.queueText(urlStatus.getHttpMessage(), urlStatus.getColorCode());
                        printer.queueText("]");
                        printer.parseQueue();
                        printPageLinkAndParents(pages);
                    }
                }
            }
        }

        stopClock.stop();
        stopClock.print();
        return success;
    }

    /**
     * Method to iterate over all known urls and verify their health level
     */
    private void verifyHealthinessOfLinks(){
        //now we check all the links with a health check
        for(PageToCheck knownUrl : knownUrls.values()){
            knownUrl.setResult(validateLink(knownUrl.getUrl()));
        }
    }


    /**
     * Method to print the infos about a page to check and their parents
     * @param _pages
     */
    private void printPageLinkAndParents(List<PageToCheck> _pages){
        for(PageToCheck page : _pages) {
            printer.parseSeperator();
            printer.parseText(String.format(Message.M_0025, page.getUrl()));

            printParentInfo(page);
        }
    }

    /**
     * Method to print the parents of a page
     * @param _page
     */
    private void printParentInfo(PageToCheck _page){
        printer.parseText(Message.M_0026);
        for(String parent : _page.getParents()) {
            printer.parseText(String.format(Message.M_0027, parent));
        }
    }

    /**
     * Method to collect all pages which have a specific urlStatus
     * @param _urlStatus
     * @return
     */
    private List<PageToCheck> getPagesByUrlStatus(URL_STATUS _urlStatus){
        List<PageToCheck> pages = new ArrayList<>();
        for(PageToCheck page : knownUrls.values()){
            if(page.getResult().equals(_urlStatus)){
                pages.add(page);
            }
        }
        return pages;
    }

    private void collectLinksFromWebpage(int _currentDepth){
        printer.parseText(String.format(Message.M_0029, getDriver().getCurrentUrl(), _currentDepth), PrettyPrintConst.COLOR.ANSI_CYAN);

        //grab the links from this website and save them
        List<WebElement> links = getDriver().findElements(By.xpath("//a"));
        List<String> hrefs = new ArrayList<>();
        for(WebElement element : links){
            try {
                if (element.getAttribute("href") != null) {
                    String href = element.getAttribute("href");

                    if (isURL(href) && knownUrls.get(href) == null) {
                        if (href.equals(getDriver().getCurrentUrl()) == false)
                            hrefs.add(href);

                        knownUrls.put(href, new PageToCheck(href, getDriver().getCurrentUrl()));
                        //printer.parseText(String.format("Found link: %s", href));
                    } else if (isURL(href) && knownUrls.get(href) != null) {
                        if (knownUrls.get(href).getParents().contains(getDriver().getCurrentUrl()) == false) {
                            knownUrls.get(href).addParent(getDriver().getCurrentUrl());
                        }

                        //printer.parseText(
                        //        new TextElement(String.format("Duplicated Link found: %s", href))
                        //);
                    }
                }

            } catch (StaleElementReferenceException sere) {
                sere.printStackTrace();
            } catch (WebDriverException wde) {
                printer.parseText(Message.M_0060);
                //printer.parseText(String.format(Message.M_0061, wde.getMessage()));
                break;
            }
        }

        //check for going deeper and find links below this page which are unknown
        for (String href : hrefs){
            try {
                if (recursive) { //check if resursive is active
                    if (urlIsPartOfDomain(href) && ((_currentDepth < depth) || depth == 0) && containsExcludes(href) == false) {
                        getDriver().navigate().to(href); //goto the page and do the same for that page!

                        collectLinksFromWebpage(_currentDepth + 1);
                    }
                }
            } catch(StaleElementReferenceException sere) {
                sere.printStackTrace();
            } catch(NoSuchWindowException nswe) {
                printer.parseText(Message.M_0060, PrettyPrintConst.COLOR.ANSI_RED);
            }
        }
        //printer.parseText(
        //        new TextElement(String.format("Leaving: %s (Depth :%d)", getDriver().getCurrentUrl(), _currentDepth), PrettyPrintConst.COLOR.ANSI_CYAN));

    }

    private boolean containsExcludes(String _href){
        for(String exclude : exludedRoutes){
            if(_href.contains(exclude)){
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the domain is part of the domain which shall be checked
     * @param _url
     * @return
     */
    private boolean urlIsPartOfDomain(String _url){
        String protocol = (https)? "https" : "http";
        String urlRegex = protocol + "://" + subdomain + "." + domain + ".*";

        Pattern pattern = Pattern.compile(urlRegex);
        Matcher m = pattern.matcher(_url);

        return m.matches();
    }

    /**
     * Check if all links are healthy or at least just localhost links
     * @return
     */
    private boolean allLinksAreHealthy(){
        for(PageToCheck page : knownUrls.values()) {
            if((page.getResult() == URL_STATUS.HTTP_OK || page.getResult() == URL_STATUS.LOCALHOST) == false ) {
                return false;
            }
        }
        return true;
    }


}
