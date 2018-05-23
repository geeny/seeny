package io.geeny.seeny.Util;

import io.geeny.prettyprint.PrettyPrint;
import io.geeny.prettyprint.util.PrettyPrintConst;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Method to initialize and handle a timer which can show time periods
 * @author Marco Bierbach
 */
public class Timer {

    private LocalDateTime startPoint;
    private LocalDateTime endPoint;

    private PrettyPrint printer = HelpMethods.getInstance().getPrinter();

    /**
     * Method to set the startpoint for the timer
     */
    public void start(){
        startPoint = LocalDateTime.now();
    }

    /**
     * Method to stop the timer
     */
    public void stop(){
        endPoint = LocalDateTime.now();
    }

    /**
     * Method to correctly format the
     * @param _value
     * @return
     */
    private String fillNull(long _value){
        return (_value > 9)? "" : "0";
    }


    public void print(){
        Duration duration = Duration.between(startPoint, endPoint);
        // total seconds of difference (using Math.abs to avoid negative values)
        long seconds = Math.abs(duration.getSeconds());
        long hours = seconds / 3600;
        seconds -= (hours * 3600);
        long minutes = seconds / 60;
        seconds -= (minutes * 60);
        printer.parseSeperator();
        printer.parseText(String.format(Message.M_0028, hours, minutes, seconds), PrettyPrintConst.COLOR.ANSI_CYAN);
    }


}
