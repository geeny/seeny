package io.geeny.seeny.Model.Elements;

import io.geeny.seeny.Util.Enums;
import org.openqa.selenium.WebDriverException;

/**
 * Element to find and handle a List element based on the explenation
 *
 * @author Marco Bierbach
 */
public class SeleniumListElement extends SeleniumElement {

    @Override
    public Enums.TEST_STEP_STATUS action() throws WebDriverException {
        return ((getElementChild().getElement() != null) == getElementChild().getBool())?
                Enums.TEST_STEP_STATUS.OK : Enums.TEST_STEP_STATUS.FAILURE;
    }

    @Override
    public Enums.TEST_STEP_STATUS execute() {
        return null;
    }

}
