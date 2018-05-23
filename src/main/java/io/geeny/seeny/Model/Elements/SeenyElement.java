package io.geeny.seeny.Model.Elements;

import io.geeny.seeny.Model.Elements.SubElements.ElementChild;
import io.geeny.seeny.Util.Enums;
import io.geeny.seeny.Util.VariableType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Interface to be able to combine two different abstracts in the scriptlist
 *
 * @Author Marco Bierbach & Klemen Samsa
 */
public interface SeenyElement {

    Enums.TEST_STEP_STATUS action();

    Enums.TEST_STEP_STATUS execute(WebDriver _driver, WebDriverWait _wait, WebElement _lastElement);

    Enums.TEST_STEP_STATUS execute();

    ElementChild getElementChild();

    int getActionId();

    VariableType.ACTION_TYPE getActionType();

    boolean isRequired();
}
