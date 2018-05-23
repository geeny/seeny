package io.geeny.seeny.Model.Elements;

import io.geeny.seeny.Util.Enums;
import io.geeny.seeny.Util.HelpMethods;
import io.geeny.seeny.Util.Message;
import io.geeny.seeny.Util.VariableType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.geeny.prettyprint.util.PrettyPrintConst;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;

/**
 * io.geeny.seeny.Model class for a navigate object of the script framework
 * @author Marco Bierbach & Klemen Samsa
 * @version 1.0
 */
public class SeleniumNavigate extends SeleniumElement {

    @SerializedName("expected")
    @Expose
    private String expectedPage;

    @SerializedName("navigateType")
    @Expose
    private VariableType.NAVIGATE navigateType;
    @SerializedName("tabId")
    @Expose
    private int tabId;

    /**
     * Setter method for the expected page for this element
     *
     * @param _expectedPage
     */
    public void setExpectedPage(String _expectedPage){
        expectedPage = _expectedPage;
    }

    /**
     * Getter method for the expected page for this element
     * @return
     */
    public String getExpectedPage(){
        return expectedPage;
    }

    /**
     * Setter method for the navigateType of the element
     * @param _navigateType
     */
    public void setNavigateType(VariableType.NAVIGATE _navigateType){
        navigateType = _navigateType;
    }

    /**
     * Getter method for the navigateType of the element
     * @return
     */
    public VariableType.NAVIGATE getNavigateType() {
        return navigateType;
    }

    /**
     * Getter method for the id of a tab
     * @return
     */
    public int getTabId() {
        return tabId;
    }

    /**
     * Setter method for the id of a tab
     * @param tabId
     */
    public void setTabId(int tabId) {
        this.tabId = tabId;
    }

    @Override
    public Enums.TEST_STEP_STATUS execute(WebDriver _driver, WebDriverWait _wait, WebElement _lastElement){
        try {
            setDriver(_driver);

            if(this.expectedPage != null)
                this.expectedPage = HelpMethods.getInstance().resolveWildCards(this.expectedPage);

            if(navigateType.equals(VariableType.NAVIGATE.GOTO)) {
                HelpMethods.getInstance().getPrinter().parseText(String.format(Message.M_0062, navigateType.getName(), expectedPage));
            } else {
                HelpMethods.getInstance().getPrinter().parseText(String.format(Message.M_0030, navigateType.getName()));
            }

            Enums.TEST_STEP_STATUS action = action();

            Thread.sleep(getWaitBefore());

            return action;
        }
        catch (Exception e){
            HelpMethods.getInstance().getPrinter().parseText(String.format(Message.M_0031, navigateType), PrettyPrintConst.COLOR.ANSI_RED);
            return Enums.TEST_STEP_STATUS.FAILURE;
        }
    }

    @Override
    public Enums.TEST_STEP_STATUS execute() {
        return null;
    }

    @Override
    public Enums.TEST_STEP_STATUS action() {
        switch(navigateType){
            case REFRESH:
                getDriver().navigate().refresh();
                break;
            case BACK:
                getDriver().navigate().back();
                break;
            case FORWARD:
                getDriver().navigate().forward();
                break;
            case GOTO:
                getDriver().navigate().to(expectedPage);
                break;
            case CLOSE_TAB:
                getDriver().switchTo().window(getWindowHandleById(getTabId())); //if the tab is not the correct id, something goes wrong
                getDriver().close();
                getDriver().switchTo().window(getWindowHandleById(0)); //for now we use 0 as we wanna always go back to the root window
                break;
            case SWITCH_TAB:
                getDriver().switchTo().window(getWindowHandleById(getTabId())); //if the tab is not the correct id, something goes wrong
                break;
        }
        return Enums.TEST_STEP_STATUS.OK;
    }

    /**
     * Method to iterate over the window tabs in a browser and select the correct one based on the defined tab id
     * @param _tabId
     * @return
     */
    private String getWindowHandleById(int _tabId){
        Set<String> windows = getDriver().getWindowHandles();
        int counter = 0;
        for(String window : windows){
            if(counter == _tabId)
                return window;
            else
                counter++;
        }
        return getDriver().getWindowHandle();
    }
}
