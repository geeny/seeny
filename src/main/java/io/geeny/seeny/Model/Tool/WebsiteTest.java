package io.geeny.seeny.Model.Tool;

import io.geeny.seeny.Model.Elements.SeenyElement;
import io.geeny.seeny.Model.Exception.StackNotEmptyException;
import io.geeny.seeny.Util.Enums;
import io.geeny.seeny.Util.HelpMethods;
import io.geeny.seeny.Util.Message;
import io.geeny.prettyprint.util.PrettyPrintConst;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for storing the website which is supposed to be tested
 * and execute the test against this website
 *
 * @author Marco Bierbach
 */
public class WebsiteTest {

    private Website websiteToTest;

    private List<WebElement> stack = new ArrayList<>();


    /**
     * Constructor for the website test
     * @param _websiteToTest
     */
    public WebsiteTest(Website _websiteToTest){
        websiteToTest = _websiteToTest;
    }

    /**
     * Method to start the testing of the website, after the preparation has been finished
     * @throws InterruptedException
     */
    public Enums.TEST_STATUS startTest(WebDriver _webDriver) throws InterruptedException {
        try {
            WebDriverWait wait = new WebDriverWait(_webDriver, Config.getInstance().getTimeout());
            wait.ignoring(NoSuchWindowException.class, WebDriverException.class);

            HelpMethods.getInstance().getPrinter().parseSeperator();
            //now iterate over all elements and call their executes
            for(SeenyElement element : websiteToTest.getScriptElementList()) {
                if(element == null)
                    HelpMethods.getInstance().getPrinter().parseText(Message.M_0056);
                else {
                    switch(element.execute(_webDriver, wait, getElementFromStack())){
                        case OK:
                            //check if the element has a child which is supposed to be used in the next step
                            if(element.getElementChild() != null && element.getElementChild().getNext() != null) {
                                addElementToStack(element.getElementChild().getElement());
                            }

                            break;
                        case WARNING:
                            //something went wrong, end the test
                            HelpMethods.getInstance().getPrinter().parseText(String.format(Message.M_0055, element.getActionId(), element.getActionType().getName()), PrettyPrintConst.COLOR.ANSI_YELLOW);
                            //Inform the testCycle - testCase - test step that this element has failed
                            //TODO: ZapiCall for reporting failure

                            //something was going wrong, make a screenshot!
                            //makeScreenShot(_webDriver, element);

                            break;
                        case FAILURE:
                            //something went wrong, end the test
                            HelpMethods.getInstance().getPrinter().parseText(String.format(Message.M_0054, element.getActionId(), element.getActionType().getName()), PrettyPrintConst.COLOR.ANSI_RED);
                            //Inform the testCycle - testCase - test step that this element has failed
                            //TODO: ZapiCall for reporting failure

                            //something was going wrong, make a screenshot!
                            //makeScreenShot(_webDriver, element);

                            if(element.isRequired())
                                return Enums.TEST_STATUS.FAILURE;
                            break;

                        case CRITICAL:
                            //something went wrong, end the test
                            HelpMethods.getInstance().getPrinter().parseText(String.format(Message.M_0053, element.getActionId(), element.getActionType().getName()), PrettyPrintConst.COLOR.ANSI_RED);
                            //Inform the testCycle - testCase - test step that this element has failed
                            //TODO: ZapiCall for reporting failure

                            //something was going wrong, make a screenshot!
                            //makeScreenShot(_webDriver, element);

                            if(element.isRequired())
                                return Enums.TEST_STATUS.CRITICAL;
                            break;
                    }
                }
                HelpMethods.getInstance().getPrinter().parseSeperator();
            }
            return Enums.TEST_STATUS.SUCCESS;
        } catch (IllegalArgumentException iae) {
            HelpMethods.getInstance().getPrinter().parseText(String.format(Message.M_0052, iae.getMessage()), PrettyPrintConst.COLOR.ANSI_RED);
            return Enums.TEST_STATUS.FAILURE;
        } catch (StackNotEmptyException e) {
            HelpMethods.getInstance().getPrinter().parseText(String.format(Message.M_0051), PrettyPrintConst.COLOR.ANSI_RED);
            return Enums.TEST_STATUS.FAILURE;
        }
        /*catch (IOException e) {
            e.printStackTrace();
            return Enums.TEST_STATUS.FAILURE;
        }*/
    }

    public void makeScreenShot(WebDriver _webDriver, SeenyElement _element) throws IOException {
        File scrFile = ((TakesScreenshot)_webDriver).getScreenshotAs(OutputType.FILE);
        ImageIO.write(ImageIO.read(scrFile), "jpg", new File(HelpMethods.getInstance().getJarPath()+"/screenshot.jpg"));
        // Now you can do whatever you need to do with it, for example copy somewhere

    }

    /**
     * Add an element to the stack, the stack should never contain more then one element
     * @param _element
     */
    public void addElementToStack(WebElement _element) throws StackNotEmptyException {
        if(stack.size()==0)
            stack.add(_element);
        else
            throw new StackNotEmptyException();
    }

    /**
     * Grab the element of the stack, the stack should never contain more then one element
     * @return
     */
    public WebElement getElementFromStack(){
        if(stack.size()>0) {
            WebElement toGet = stack.get(0);
            stack.clear();
            return toGet;
        }
        return null;
    }

    public boolean isZapProxyEnabled(){
        if(websiteToTest.getZapProxy() == null)
            return false;

        return websiteToTest.getZapProxy().isEnabled();
    }

    public Website getWebsiteToTest(){
        return websiteToTest;
    }
}
