package io.geeny.seeny.Model.Elements.SubElements;

import io.geeny.seeny.Util.VariableType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.openqa.selenium.WebElement;

/**
 * Condition Element for a Scriptelement.
 * This Condition Element is supposed to be used, when the elements, needs to find a specific child element based on conditions
 *
 * "child" : {
 * "selectorType" : "css",
 * "selector" : "input[type=text]",
 * "bool" : true
 * }
 *
 * @author Marco Bierbach
 *
 */
public class ElementChild {

    private WebElement element;

    @SerializedName("selectorType")
    @Expose
    private VariableType.SELECTOR_TYPE selectorType;
    @SerializedName("selectorExpression")
    @Expose
    private String selector;
    @SerializedName("bool")
    @Expose
    private boolean bool;
    @SerializedName("next")
    @Expose
    private String next;

    public ElementChild(VariableType.SELECTOR_TYPE _selectorType, String _selector, boolean _bool, String _next){
        this.selectorType = _selectorType;
        this.selector = _selector;
        this.bool = _bool;
        this.next = _next;
    }

    /**
     * Setter method for the selector type
     * @param _selectorType
     */
    public void setSelectorType(VariableType.SELECTOR_TYPE _selectorType){
        this.selectorType = _selectorType;
    }

    /**
     * Getter method for the selector type
     */
    public VariableType.SELECTOR_TYPE getSelectorType(){
        return this.selectorType;
    }

    /**
     * Setter method for the element of the condition
     * @param _element
     */
    public void setElement(WebElement _element){
        this.element = _element;
    }

    /**
     * Getter method for the element of the condition
     * @return
     */
    public WebElement getElement(){
        return this.element;
    }

    /**
     * Setter method for the selector of the condition
     * @param _selector
     */
    public void setSelector(String _selector){
        this.selector = _selector;
    }

    /**
     * Getter method for the selector of the condition
     * @return
     */
    public String getSelector(){
        return this.selector;
    }

    /**
     * Setter method for the bool value of the condition, if this condition shall be true or false
     * @param _bool
     */
    public void setBool(boolean _bool){
        this.bool = _bool;
    }

    /**
     * Getter method for the bool value of the condition, if this condition shall be true or false
     * @return
     */
    public boolean getBool(){
        return this.bool;
    }


    /**
     * Setter method for the next value of the script element
     * @param _next
     */
    public void setNext(String _next){
        this.next = _next;
    }

    /**
     * Getter method for the next value of the script element
     * @return
     */
    public String getNext(){
        return this.next;
    }

}
