package io.geeny.seeny.Model.Tool;

import io.geeny.seeny.Model.Elements.SeleniumElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * io.geeny.seeny.Model for the website script to test.
 * @author Marco Bierbach & Klemen Samsa
 * @version 1.0
 */
public class Website {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("close")
    @Expose
    private boolean close;
    @SerializedName("script")
    @Expose
    private List<? extends SeleniumElement> script;
    @SerializedName("zap-proxy")
    @Expose
    private ZapProxy zapProxy;


    /**
     * Setter method for the name of a website
     * @param _name
     */
    public void setName(String _name) {
        name = _name;
    }

    /**
     * Getter method for the name of the website
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     * Setter method for the close element of the script
     * @param _close
     */
    public void setClose(boolean _close){
        close = _close;
    }

    /**
     * Getter method for the close element of the script
     * @return
     */
    public boolean getClose(){
        return close;
    }
    /**
     * Setter method for the script element list of the website
     * @param _scriptElementList
     */
    public void setScriptElementList(List<SeleniumElement> _scriptElementList){
        script = _scriptElementList;
    }

    /**
     * Getter method for the script element list of the website
     * @return
     */
    public List<? extends SeleniumElement> getScriptElementList(){
        return script;
    }

    /**
     * Getter method to get the description of this test script
     * @return
     */
    public String getDescription() {
        return description;
    }
    /**
     * Setter method to set the description of this test script
     * @param _description
     */
    public void setDescription(String _description) {
        this.description = _description;
    }

    public ZapProxy getZapProxy() {
        return zapProxy;
    }

    public void setZapProxy(ZapProxy zapProxy) {
        this.zapProxy = zapProxy;
    }
}
