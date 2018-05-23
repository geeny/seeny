package io.geeny.seeny.Model.Elements;

import io.geeny.seeny.Model.Tool.Config;
import io.geeny.seeny.Model.Elements.SubElements.CliParameter;
import io.geeny.seeny.Model.Elements.SubElements.ElementChild;
import io.geeny.seeny.Model.Elements.SubElements.Item;
import io.geeny.seeny.Util.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.geeny.prettyprint.util.PrettyPrintConst;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract io.geeny.seeny.Model class for an element extended for the console
 *
 * @author Marco Bierbach
 * @version 1.0
 */
public abstract class ConsoleElement implements SeenyElement {
    @SerializedName("actionId")
    @Expose
    private int actionId;
    @SerializedName("actionType")
    @Expose
    private VariableType.ACTION_TYPE actionType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("waitBefore")
    @Expose
    private int waitBefore = 0;
    @SerializedName("waitAfter")
    @Expose
    private int waitAfter = 0;
    @SerializedName("required")
    @Expose
    private boolean required = true;
    @SerializedName("memory")
    @Expose
    private String memoryName;
    @SerializedName("command")
    @Expose
    private String command;
    @SerializedName("parameters")
    @Expose
    private List<CliParameter> parameters;
    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("fromRoot")
    @Expose
    private boolean fromRoot = true;
    @SerializedName("directory")
    @Expose
    private String directory;

    /**
     * Getter method for the command for the cli element
     * @return
     */
    public String getCommand() {
        return command;
    }

    /**
     *
     * @param command
     */
    public void setCommand(String command) {
        this.command = command;
    }

    public List<CliParameter> getParameters() {
        return parameters;
    }

    public void setParameter(List<CliParameter> _parameters) {
        this.parameters = parameters;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    InputStream errorFromProcess;
    InputStream outputFromProcess;

    @Override
    public abstract Enums.TEST_STEP_STATUS action();

    public Enums.TEST_STEP_STATUS execute(){
        try {
            String parameterString = "";
            //build the parameter
            for(CliParameter parameter : getParameterListByType(Enums.PARAMETER_TYPE.CONSOLE)){
                parameterString = parameterString.concat(" ").concat(parameter.getParameter());
            }

            this.command = HelpMethods.getInstance().resolveWildCards(this.command);
            parameterString = HelpMethods.getInstance().resolveWildCards(parameterString);

            //define the workingDirectory for this call
            File directory;
            if(isFromRoot()){
                String path = HelpMethods.getInstance().getJarPath();
                if(getDirectory() != null){
                    path = path.concat(getDirectory());
                }
                directory = new File(path);
            } else {
                directory = new File(getDirectory());
            }

            String[] args;
            if(Config.getInstance().getOs().equals(Enums.OS.WINDOWS))
                args = new String[] {"cmd", "/c", this.command+parameterString};
            else
                args = new String[] {"/bin/bash", "-c", this.command+parameterString};

            Process process = new ProcessBuilder(args)
                    .redirectErrorStream(true)
                    .directory(directory)
                    .start();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream ()));


            for(CliParameter parameter : getParameterListByType(Enums.PARAMETER_TYPE.STDIN)){
                writer.write(parameter.getParameter());
                writer.flush();
            }

            errorFromProcess = process.getErrorStream ();
            outputFromProcess = process.getInputStream ();

            try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Thread.sleep(waitBefore);


            if(process.exitValue() == 0) {
                action(); //let the child action handle the result

                Thread.sleep(waitAfter);

                return Enums.TEST_STEP_STATUS.OK;
            } else {
                // launch EXE and grab stdin/stdout and stderr
                BufferedReader brCleanUp =
                        new BufferedReader (new InputStreamReader(outputFromProcess));

                try {
                    String line;
                    // clean up if any output in stdout

                    String content = "";
                    while ((line = brCleanUp.readLine()) != null) {
                        content += line + " ";
                    }

                    HelpMethods.getInstance().getPrinter().parseText(String.format(Message.M_0003, (required)? "Error" : "Warning", this.command, process.exitValue(), content), (required)? PrettyPrintConst.COLOR.ANSI_RED : PrettyPrintConst.COLOR.ANSI_YELLOW);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        brCleanUp.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return (required)? Enums.TEST_STEP_STATUS.FAILURE : Enums.TEST_STEP_STATUS.WARNING;
    }

    /**
     * Getter method for all parameters from an cli object based on the type
     * @param _type
     * @return
     */
    private List<CliParameter> getParameterListByType(Enums.PARAMETER_TYPE _type){
        List<CliParameter> parameterList = new ArrayList<>();
        if(parameters != null) {
            for (CliParameter parameter : parameters) {
                if (parameter.getType().equals(_type)) {
                    parameterList.add(parameter);
                }
            }
        }
        return parameterList;
    }

    @Override
    public Enums.TEST_STEP_STATUS execute(WebDriver _driver, WebDriverWait _wait, WebElement _lastElement) {
        return execute();
    }

    @Override
    public int getActionId(){
        return actionId;
    }

    @Override
    public VariableType.ACTION_TYPE getActionType(){
        return actionType;
    }

    @Override
    public ElementChild getElementChild() {
        return null;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    /**
     * Getter method for the directory element
     * @return
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Setter method for the directory element
     * @param directory
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * Check method to check what booelan value fromRoot has
     * @return
     */
    public boolean isFromRoot() {
        return fromRoot;
    }

    /**
     * Setter method for the fromRoot value
     * @param fromRoot
     */
    public void setFromRoot(boolean fromRoot) {
        this.fromRoot = fromRoot;
    }
}
