package io.geeny.seeny.Model.Elements;

import io.geeny.seeny.Util.Enums;
import io.geeny.seeny.Util.Message;
import io.geeny.seeny.Util.VariableType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.geeny.prettyprint.util.PrettyPrintConst;

/**
 * io.geeny.seeny.Model class for a script condition element
 * @Author Klemen Samsa
 */
public class SeleniumCondition extends SeleniumElement {

    @SerializedName("bool")
    @Expose
    private boolean bool;

    @SerializedName("task")
    @Expose
    private VariableType.TASK task;

    @SerializedName("value")
    @Expose
    private String value;

    /**
     * Setter method for the boolean of the condition
     * @param _bool
     */
    public void setBool(boolean _bool){
        bool = _bool;
    }

    /**
     * Getter method for the boolean of the condition
     * @return
     */
    public boolean getBool(){
        return bool;
    }

    /**
     * Setter method for the task of the condition check
     *
     * @param _task
     */
    public void setTask(VariableType.TASK _task) {
        task = _task;
    }

    /**
     * Getter method for the task of the condition check
      * @return
     */
    public VariableType.TASK getTask(){
        return task;
    }

    /**
     * Setter method for the value of the condition
     * @param _value
     */
    public void setValue(String _value){
        value = _value;
    }

    /**
     * Getter method for the value of the condition
     * @return
     */
    public String getValue(){
        return value;
    }

    @Override
    public Enums.TEST_STEP_STATUS action(){
        boolean success;
        printer.queueText(Message.M_0006);
        switch(task) {
            case ENABLED:
                printer.queueText((bool)? "enabled" : "disabled");
                success = (getElement().isEnabled() == bool);
                break;
            case DISPLAYED:
                printer.queueText((bool)? "displayed" : "invisible");
                success = (getElement().isDisplayed() == bool);
                break;
            case EMPTY:
                printer.queueText((bool)? "empty" : "containing text");
                success = ((getElement().getText().length() == 0) == bool);
                break;
            case SELECTED:
                printer.queueText((bool)? "selected" : "not selected");
                success = (getElement().isSelected() == bool);
                break;
            case COMPARE:
            default:
                printer.queueText(String.format(Message.M_0049, this.getDescription()));
                success = (getElement().getText().contains(value.toUpperCase()) == bool); //we have to set the value to uppercase because of w3c specification
                break;
        }
        printer.queueText(String.format(Message.M_0050,(success? "success" : "fail")), (success? PrettyPrintConst.COLOR.ANSI_GREEN : PrettyPrintConst.COLOR.ANSI_RED));
        printer.parseQueue();

        return Enums.TEST_STEP_STATUS.OK;
    }

    @Override
    public Enums.TEST_STEP_STATUS execute() {
        return null;
    }
}
