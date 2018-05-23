package io.geeny.seeny.Model.Tool;

import io.geeny.seeny.Model.Elements.WebsiteTestElement;
import io.geeny.seeny.Util.Enums;
import io.geeny.seeny.Util.HelpMethods;
import io.geeny.seeny.Util.Message;
import io.geeny.prettyprint.util.PrettyPrintConst;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Config Method for the selenium json interpreter
 *
 * @author Marco Bierbach & Klemen Samsa
 */
public class Config {

    private static Config instance;

    private int timeout = 10;
    private List<Driver> availableDrivers;
    private List<WebsiteTestElement> websitesToTest;
    private PrettyPrintConst.PRINT_TYPE printType = PrettyPrintConst.PRINT_TYPE.ALL;
    private Map<String,Integer> prettyPrintValues = new HashMap<>();
    private Enums.OS os;

    public Config(){
        instance = this;
    }

    public static Config getInstance() {
        return instance;
    }

    public void setOs(Enums.OS _os){
        os = _os;
    }

    public Enums.OS getOs() {
        return os;
    }

    public void splitArgs(String [] _args){

        //first check for

        boolean singleTestExists = false;

        WebsiteTestElement singleTest = new WebsiteTestElement();
        for (int i = 0; i < _args.length;) {
            switch (_args[i]) {
                case "-d":
                    singleTest.setDriver(_args[i + 1]);
                    singleTestExists = true;
                    i+=2;
                    break;
                case "-w":
                    singleTest.setWebsiteName(_args[i + 1]);
                    singleTestExists = true;
                    i+=2;
                    break;
                case "-r":
                    singleTest.setRetries(getInt(_args[i + 1]));
                    singleTestExists = true;
                    i+=2;
                    break;
                case "-t":
                    timeout = getInt(_args[i + 1]);
                    singleTestExists = true;
                    i+=2;
                    break;
                case "--log-file-only":
                    printType = PrettyPrintConst.PRINT_TYPE.FILE;
                    i+=1;
                    break;
                case "--log-console-only":
                    printType = PrettyPrintConst.PRINT_TYPE.CONSOLE;
                    i+=1;
                    break;
                case "--silent":
                    printType = PrettyPrintConst.PRINT_TYPE.SILENT;
                    i+=1;
                    break;
                case "-pp-padding":
                    String prettyPrintPadding = _args[i+1];
                    getPrettyPrintValues().put("padding", Integer.parseInt(prettyPrintPadding));
                    i+=2;
                    break;
                case "-pp-width":
                    String prettyPrintWidth = _args[i+1];
                    getPrettyPrintValues().put("width", Integer.parseInt(prettyPrintWidth));
                    i+=2;
                    break;
                default:
                    i+=1;
                    break;
            }
        }

        if(singleTestExists) {
            websitesToTest = new ArrayList<>();
            websitesToTest.add(singleTest);
        }

    }

    /**
     * Setter method for the available drivers of the config
     * @param _availableDrivers
     */
    public void setAvailablDrivers(List<Driver> _availableDrivers) {
        availableDrivers = _availableDrivers;
    }

    /**
     * Getter method for the timeout for the webdriver
     * @return
     */
    public int getTimeout(){
        return timeout;
    }

    /**
     * Getter method for the specific driver which shall be used for this test from the list of available drivers
     * @return
     */
    public Driver getDriverForTest(WebsiteTestElement _websiteTestElement) {
        for (Driver driverToCheck: availableDrivers ) {
            if(driverToCheck.getName().equals(_websiteTestElement.getDriver())){
                return driverToCheck;
            }
        }
        return null;
    }

    /**
     * Getter method for the integer value of a parameter
     * @param _parameter
     * @return
     */
    private int getInt(String _parameter){
        int returnValue = -1;
        try {
            returnValue = Integer.parseInt(_parameter);
        } catch (NumberFormatException nfe){
            HelpMethods.getInstance().getPrinter().parseText(String.format(Message.M_0001,_parameter));
        }
        return returnValue;
    }

    public List<WebsiteTestElement> getWebsitesToTest() {
        return websitesToTest;
    }

    public void setWebsitesToTest(List<WebsiteTestElement> websitesToTest) {
        this.websitesToTest = websitesToTest;
    }

    public PrettyPrintConst.PRINT_TYPE getPrintType() {
        return printType;
    }

    public Map<String, Integer> getPrettyPrintValues() {
        return prettyPrintValues;
    }
}
