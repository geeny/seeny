package io.geeny.seeny.Model.Elements.SubElements;

import com.sun.org.apache.xpath.internal.operations.Variable;
import io.geeny.seeny.Util.Enums;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.geeny.seeny.Util.VariableType;

/**
 * Model for an item which is used by a seeny element for extracting elements
 *
 * @Author Marco Bierbach
 */

public class Item {

    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("task")
    @Expose
    private VariableType.TASK task;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("regexp")
    @Expose
    private String regexp;
    @SerializedName("description")
    @Expose
    private String description;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    public VariableType.TASK getTask() {
        return task;
    }

    public void setTask(VariableType.TASK task) {
        this.task = task;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
