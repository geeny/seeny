package io.geeny.seeny.Model.Elements;

import io.geeny.seeny.Util.Enums;
import io.geeny.seeny.Util.HelpMethods;
import io.geeny.seeny.Util.Message;
import io.geeny.seeny.Util.VariableType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

/**
 * io.geeny.seeny.Model class for a input object of the script framework
 * @author Marco Bierbach & Klemen Samsa
 * @version 1.0
 */
public class SeleniumInput extends SeleniumElement {

    @SerializedName("content")
    @Expose
    private String content;

    /**
     * Setter method for the content of the script element
     *
     * @param _content
     */
    public void setContent(String _content) {
        content = _content;
    }

    /**
     * Getter method for the content of the script element
     *
     * @return
     */
    public String getContent() {
        return content;
    }

    /**
     * Method to do the action which i suposed to happen with the element
     */
    @Override
    public Enums.TEST_STEP_STATUS action() throws WebDriverException {
        WebElement inputField = (getSelectorType().equals(VariableType.SELECTOR_TYPE.COMP))? getElementChild().getElement() : getElement();
        switch(this.getActionType()) {
            case DELETE:
                HelpMethods.getInstance().getPrinter().parseText(String.format(Message.M_0058, getDescription() ));

                inputField.clear();
                break;
            case WRITE:
                this.content = HelpMethods.getInstance().resolveWildCards(this.content, this.getMemoryName());

                HelpMethods.getInstance().getPrinter().parseText(String.format(Message.M_0059, this.content, getDescription()));
                inputField.sendKeys(this.content);
                break;
        }

        return Enums.TEST_STEP_STATUS.OK;
    }

    @Override
    public Enums.TEST_STEP_STATUS execute() {
        return null;
    }
}
