package io.geeny.seeny.Model.Elements;

import io.geeny.seeny.Model.Elements.SubElements.Item;
import io.geeny.seeny.Util.Enums;
import io.geeny.seeny.Util.HelpMethods;
import io.geeny.seeny.Util.Message;
import io.geeny.prettyprint.util.PrettyPrintConst;
import org.openqa.selenium.WebDriverException;

import java.io.*;

/**
 * io.geeny.seeny.Model class for a cli command which is gathering information for seeny over the command line interface
 * @Author Marco Bierbach
 */
public class ConsoleCommand extends ConsoleElement {

    @Override
    public Enums.TEST_STEP_STATUS action() throws WebDriverException {
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

            if(getItems() != null) {
                //iterate over the content with the regexps from the items to find all needed elements
                for (Item item : getItems()) {
                    String resultMatch = HelpMethods.getInstance().checkContent(content, item.getRegexp());
                    boolean success = HelpMethods.getInstance().handleResult(resultMatch, item);
                    if (success) {
                        HelpMethods.getInstance().getPrinter().parseText(String.format(Message.M_0002, item.getDescription(), (success) ? "" : "not "), ((success) ? PrettyPrintConst.COLOR.ANSI_GREEN : PrettyPrintConst.COLOR.ANSI_RED));
                    }
                }
            }

            return Enums.TEST_STEP_STATUS.OK;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                brCleanUp.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return Enums.TEST_STEP_STATUS.FAILURE;
    }

}
