package io.geeny.seeny.Util;

/**
 * Constant class for storing constants which are used by seeny
 *
 *@Author Marco Bierbach
 */
public class SeenyConst {

    public static String RAND_SYMBOL = "[?";
    public static String RAND_PATTERN = "\\[\\?([a-zA-Z0-9_]*)\\]";
    public static String MEMORY_SYMBOL = "[!";
    public static String MEMORY_PATTERN =  "\\"+MEMORY_SYMBOL+"([a-z0-9_]*)\\]";
    public static String EMPTY = "";
    public static String GEENY_COMPONENT_IDENTIFIER = "[data-component-name='%s']";
    public static String FILE_ENDING = ".json";

}
