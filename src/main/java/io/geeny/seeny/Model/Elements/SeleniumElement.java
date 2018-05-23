package io.geeny.seeny.Model.Elements;

import io.geeny.seeny.Model.Elements.SubElements.ElementChild;
import io.geeny.seeny.Util.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.geeny.prettyprint.PrettyPrint;
import io.geeny.prettyprint.util.PrettyPrintConst;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Base Class for every script element in a website script
 * @author Marco Bierbach & Klemen Samsa
 * @version 1.0
 */
public abstract class SeleniumElement implements SeenyElement {
    @SerializedName("actionId")
    @Expose
    private int actionId;
    @SerializedName("selectorType")
    @Expose
    private VariableType.SELECTOR_TYPE selectorType;
    @SerializedName("dataComponentName")
    @Expose
    private String dataComponentName;
    @SerializedName("actionType")
    @Expose
    private VariableType.ACTION_TYPE actionType;
    @SerializedName("selectorExpression")
    @Expose
    private String selectorExpression;
    @SerializedName("child")
    @Expose
    private ElementChild elementChild;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("waitBefore")
    @Expose
    private int waitBefore = 0;
    @SerializedName("waitAfter")
    @Expose
    private int waitAfter = 0;
    @SerializedName("required")
    @Expose
    private boolean required = true;
    @SerializedName("memory")
    @Expose
    private String memoryName;

    private WebElement element;
    private WebDriver driver;
    public PrettyPrint printer = HelpMethods.getInstance().getPrinter();

    /**
     * Setter method of the action id of a script element
     *
     * @param _actionId
     */
    public void setActionId(int _actionId) {
        actionId = _actionId;
    }

    /**
     * Getter method of the action id of a script element
     *
     * @return
     */
    public int getActionId() {
        return actionId;
    }

    /**
     * Setter method for the action type of a script element
     *
     * @param _actionType
     */
    public void setActionType(VariableType.ACTION_TYPE _actionType) {
        actionType = _actionType;
    }

    /**
     * Getter method for the action type of a script element
     *
     * @return
     */
    public VariableType.ACTION_TYPE getActionType() {
        return actionType;
    }

    /**
     * Setter method for the selectorExpression type of the script element
     * @param _selectorType
     */
    public void setSelectorType(VariableType.SELECTOR_TYPE _selectorType) {
        selectorType = _selectorType;
    }

    /**
     * Getter method for the selectorExpression tye of the script element
     * @return
     */
    public VariableType.SELECTOR_TYPE getSelectorType(){
        return selectorType;
    }

    /**
     * Setter method for the selectorExpression of a script element
     *
     * @param _selector
     */
    public void setSelectorExpression(String _selector) {
        selectorExpression = _selector;
    }

    public WebElement getElement() {
        return element;
    }

    /**
     * Getter method for the element child of the script element
     * @return
     */
    public ElementChild getElementChild(){
        return  elementChild;
    }

    /**
     * Setter method for the element child of the script element
     * @param _elementChild
     */
    public void setElementChild(ElementChild _elementChild){
        this.elementChild = _elementChild;
    }

    /**
     * Setter method for the data-component-name of the element
     * @param _dataComp
     */
    public void setDataComponentName(String _dataComp){
        dataComponentName = _dataComp;
    }

    /**
     * Getter method for the data-component-name of the element
     * @return
     */
    public String getDataComponentName(){
        return dataComponentName;
    }

    /**
     * Setter method for the description of the script element
     * @param _description
     */
    public void setDescription(String _description) {
        description = _description;
    }

    /**
     * Getter method for the description of the script element
     * @return
     */
    public String getDescription(){
        return description;
    }

    /**
     * Setter method for the wait before time of the element
     * @param _waitBefore
     */
    public void setWaitBefore(int _waitBefore){
        waitBefore = _waitBefore;
    }

    /**
     * Getter method for the wait before time of the element
     * @return
     */
    public int getWaitBefore(){
        return waitBefore;
    }

    /**
     * Setter method for the wait after time of the element
     * @param _waitAfter
     */
    public void setWaitAfter(int _waitAfter){
        waitAfter = _waitAfter;
    }

    /**
     * Getter method for the wait after time of the element
     * @return
     */
    public int getWaitAfter(){
        return waitAfter;
    }

    /**
     * Getter method for the selectorExpression of a script element
     *
     * @return
     */
    public String getSelectorExpression() {
        return selectorExpression;
    }

    /**
     * Setter method for the driver of the test
     * @param _driver
     */
    public void setDriver(WebDriver _driver){
        driver = _driver;
    }

    /**
     * Getter method for the driver of the test
     * @return
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Getter method for the required flag of a script element
     * @return
     */
    public boolean isRequired(){
        return required;
    }


    /**
     * Setter method for the required flag of a script element
     * @param _required
     */
    public void setRequired(boolean _required){
        required = _required;
    }

    /**
     * Execute method for the script element
     *
     * @return
     */
    public Enums.TEST_STEP_STATUS execute(WebDriver _driver, WebDriverWait _wait, WebElement _lastElement){
        driver = _driver;
        try {
            if(_lastElement == null) {
                try {
                    //check if there are any wildcards in this selectorExpression and resolve them with the given variable from the memoryqueue
                    if(this.selectorExpression != null)
                        this.selectorExpression = HelpMethods.getInstance().resolveWildCards(this.selectorExpression);

                    _wait.until(ExpectedConditions.visibilityOfElementLocated(getBy(this.selectorType, this.selectorExpression)));
                    printer.parseText(String.format(Message.M_0007, this.description));

                    //element is now visible, now go and grab it and have fun!
                    element = driver.findElement(getBy(this.selectorType, this.selectorExpression));

                    if (elementChild != null) //if child selectorExpression is not null, get the correct child
                        elementChild.setElement(getDataComponentChild());
                } catch (NoSuchWindowException nswe){
                    printer.parseText(Message.M_0008);
                    return Enums.TEST_STEP_STATUS.CRITICAL;
                } catch (WebDriverException wde){
                    printer.parseText(Message.M_0009);
                    return Enums.TEST_STEP_STATUS.CRITICAL;
                } catch (RuntimeException re){
                    printer.parseText(Message.M_0010);
                    return Enums.TEST_STEP_STATUS.CRITICAL;
                }
            } else {
                element = _lastElement;
            }

            Thread.sleep(waitBefore);

            Enums.TEST_STEP_STATUS status = action();

            Thread.sleep(waitAfter);

            return status;
        } catch (Exception e){
            printer.parseText(String.format(Message.M_0011, this.description ), PrettyPrintConst.COLOR.ANSI_RED);
            return Enums.TEST_STEP_STATUS.FAILURE;
        }
    }

    /**
     * Method to get the correct By object via the type selectorExpression
     * @return
     */
    private By getBy(VariableType.SELECTOR_TYPE _selectorType, String _selector) {
        try {
            switch (_selectorType) {
                case ID:
                    return By.id(_selector);
                case CSS:
                    return By.cssSelector(_selector);
                case COMP:
                    String toSearchFor = String.format(SeenyConst.GEENY_COMPONENT_IDENTIFIER, this.dataComponentName);
                    return By.cssSelector(toSearchFor);
                case XPATH:
                    return By.xpath(_selector);
            }
        } catch (NoSuchWindowException nswe){
            printer.parseText(Message.M_0012);
        }
        return null;
    }

    public abstract Enums.TEST_STEP_STATUS action() throws WebDriverException;

    /**
     * Method to get a child element from a react ui element
     * @return
     */
    protected WebElement getDataComponentChild(){
        return getElement().findElement(getBy(getElementChild().getSelectorType(), getElementChild().getSelector()));
    }

    public String getMemoryName() {
        return memoryName;
    }

}

