package io.geeny.seeny.Model.Elements;

import io.geeny.seeny.Util.Enums;
import io.geeny.seeny.Util.HelpMethods;
import io.geeny.seeny.Util.Message;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.openqa.selenium.StaleElementReferenceException;

/**
 * io.geeny.seeny.Model class for a Clickable object of the script framework
 *
 * @author Marco Bierbach & Klemen Samsa
 * @version 1.0
 */
public class SeleniumClickable extends SeleniumElement {

    @SerializedName("redirect")
    @Expose
    private String redirect;

    /**
     * Setter method for the redirect info of the scriptClickable element of the website
     * @param _redirect
     */
    public void setRedirect(String _redirect) {
        redirect = _redirect;
    }

    /**
     * Getter method for the redirect info of the scriptClickable element of the website
     * @return
     */
    public String getRedirect(){
        return redirect;
    }

    /**
     * Method to do the action which i suposed to happen with the element
     */
    @Override
    public Enums.TEST_STEP_STATUS action() {
        try {
            HelpMethods.getInstance().getPrinter().parseText(String.format(Message.M_0004, getDescription()));

            getElement().click();

            return Enums.TEST_STEP_STATUS.OK;
        } catch (StaleElementReferenceException sere){
            HelpMethods.getInstance().getPrinter().parseText(String.format(Message.M_0005,this.getDescription()));
        }
        return Enums.TEST_STEP_STATUS.FAILURE;
    }

    @Override
    public Enums.TEST_STEP_STATUS execute() {
        return null;
    }
}
