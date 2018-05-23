package io.geeny.seeny.Model.Tool;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * io.geeny.seeny.Model Class which stores the information about a specific driver, used fo the selenium test
 * @author Marco Bierbach
 * @version 1.0
 */
public class Driver {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("path")
    @Expose
    private String driverPath;

    @SerializedName("propertyName")
    @Expose
    private String driverPropertyName;


    /**
     * Setter method for the name of the file
     * @param _name
     */
    public void setName(String _name) {
        name = _name;
    }

    /**
     * Getter method for the name of the file
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     * Setter method for the driver path to the file
     * @param _driverPath
     */
    public void setDriverPath(String _driverPath) {
        driverPath = _driverPath;
    }

    /**
     * Getter method for the driver path to the file
     * @return
     */
    public String getDriverPath(){
        return driverPath;
    }

    /**
     * Setter method for the driver property name of the file
     * @param _driverPropertyName
     */
    public void setDriverPropertyName(String _driverPropertyName) {
        driverPropertyName = _driverPropertyName;
    }

    /**
     * Getter method for the property name, used for loading the webdriver into selenium
     * @return
     */
    public String getDriverPropertyName() {
        return driverPropertyName;
    }
}
