package io.geeny.seeny.Model.Elements.SubElements;

import io.geeny.seeny.Util.Enums;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Sub io.geeny.seeny.Model for an CliParameter used by CliCommand
 * @Author Marco Bierbach
 */
public class CliParameter {

    @SerializedName("parameter")
    @Expose
    private String parameter;
    @SerializedName("type")
    @Expose
    private Enums.PARAMETER_TYPE type;

    /**
     * Getter method for the parameter to parse
     * @return
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * Getter method for the type of the parameter
     * @return
     */
    public Enums.PARAMETER_TYPE getType() {
        return type;
    }

    /**
     * Setter method for the parameter
     * @param parameter
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    /**
     * Setter method for the type
     * @param type
     */
    public void setType(Enums.PARAMETER_TYPE type) {
        this.type = type;
    }
}
