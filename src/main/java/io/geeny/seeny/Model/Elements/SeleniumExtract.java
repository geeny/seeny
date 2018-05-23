package io.geeny.seeny.Model.Elements;

import io.geeny.seeny.Model.Elements.SubElements.Item;
import io.geeny.seeny.Util.Enums;
import io.geeny.seeny.Util.HelpMethods;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.geeny.prettyprint.util.PrettyPrintConst;
import org.openqa.selenium.WebDriverException;

import java.util.List;

/**
 * Element to extract text for the memory queue from a website, to use it later
 *
 * @author Marco Bierbach
 * @version 1.0
 */
public class SeleniumExtract extends SeleniumElement {

    @SerializedName("items")
    @Expose
    private List<Item> items;
    @SerializedName("extractElement")
    @Expose
    private String extractElement;

    @Override
    public Enums.TEST_STEP_STATUS action() throws WebDriverException {
        if(this.getMemoryName() != null) { //if this script element shall memorize the generated string
            String content;
            if(extractElement != null) {
                //we have a variable or id to grab, not the text
                content = this.getElement().getAttribute(extractElement);
            } else {
                content = this.getElement().getText();
            }

            HelpMethods.getInstance().memorizeContent(content, this.getMemoryName());

            //if there are no items, nothing should be extracted additionally
            if(items != null) {
                //we have something in this element which we wanna save additionally
                //iterate over the content with the regexps from the items to find all needed elements
                for (Item item : getItems()) {
                    String resultMatch = HelpMethods.getInstance().checkContent(content, item.getRegexp());
                    boolean success = HelpMethods.getInstance().handleResult(resultMatch, item);
                    HelpMethods.getInstance().getPrinter().parseText(String.format("[%s] was %sfound", item.getDescription(), (success) ? "" : "not "), ((success) ? PrettyPrintConst.COLOR.ANSI_GREEN : PrettyPrintConst.COLOR.ANSI_RED));
                }
            }

        }

        return Enums.TEST_STEP_STATUS.OK;
    }

    @Override
    public Enums.TEST_STEP_STATUS execute() {
        return null;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
