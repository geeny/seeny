package io.geeny.seeny.Model.Elements;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model for representing a website test element
 *
 * @Author Marco Bierbach & Klemen Samsa
 */
public class WebsiteTestElement {

    @SerializedName("name")
    @Expose
    private String websiteName;
    @SerializedName("driver")
    @Expose
    private String driver = "chromedriver";
    @SerializedName("retries")
    @Expose
    private int retries = 1;
    @SerializedName("timeout")
    @Expose
    private int timeout = 10; //default 10 seconds

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public int getRetries() {
        return retries;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
