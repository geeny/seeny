package io.geeny.seeny.Util;

import io.geeny.prettyprint.util.PrettyPrintConst;

/**
 * Class to store all needed enums
 *
 * @Author Marco Bierbach
 */
public class Enums {

    public enum TEST_STEP_STATUS {
        OK, WARNING, FAILURE, CRITICAL}

    public enum TEST_STATUS {
        SUCCESS("Successful"),
        FAILURE("Failed"),
        CRITICAL("Critical"),
        WARNING("Warning"),
        WITH_ERRORS("with Errors");

        private String message;

        TEST_STATUS(String _message){
           message = _message;
        }

        public String getMessage(){
            return message;
        }
    }

    public enum PARAMETER_TYPE {
        CONSOLE("console"),
        STDIN("stdin");

        private String name;

        PARAMETER_TYPE(String _name){
            name = _name;
        }

        public String getName(){
            return name;
        }
    }

    public enum RANDOM_TYPE {
        ALPHABETIC("A"),
        ALPHANUMERIC("AN"),
        NUMERIC("N"),
        ASCII("AS");

        private String key;

        RANDOM_TYPE(String _key){
            key = _key;
        }

        public String getKey(){
            return key;
        }
    }

    public enum URL_STATUS {

        CONNECTION_REFUSED(-1, "Connection Refused", "ERROR", PrettyPrintConst.COLOR.ANSI_RED),
        LOCALHOST(-2, "Localhost link found", "WARNING", PrettyPrintConst.COLOR.ANSI_YELLOW),
        HTTP_OK(200, "Ok", "SUCCESS", PrettyPrintConst.COLOR.ANSI_GREEN),
        NO_CONTENT(204, "No Content", "SUCCESS", PrettyPrintConst.COLOR.ANSI_GREEN),
        MOVED_PERMANENTLY(301, "Moved Permanently", "SUCCESS", PrettyPrintConst.COLOR.ANSI_YELLOW),
        REDIRECT(302,"Redirect", "SUCCESS", PrettyPrintConst.COLOR.ANSI_GREEN),
        NOT_MODIFIED(304, "Not modified", "SUCCESS", PrettyPrintConst.COLOR.ANSI_GREEN),
        USE_PROXY(305, "Use Proxy", "SUCCESS", PrettyPrintConst.COLOR.ANSI_GREEN),
        INTERNAL_SERVER_ERROR(500, "Internal Server Error", "ERROR", PrettyPrintConst.COLOR.ANSI_RED),
        NOT_FOUND(404, "Not Found", "ERROR", PrettyPrintConst.COLOR.ANSI_RED),
        LINKEDIN(999, "Linkedin", "SUCCESS", PrettyPrintConst.COLOR.ANSI_YELLOW);

        private int statusCode;
        private String httpMessage;
        private String result;
        private PrettyPrintConst.COLOR colorCode;

        public int getStatusCode() {
            return statusCode;
        }
        public String getHttpMessage() { return httpMessage; }
        public String getStatus() { return result;}
        public PrettyPrintConst.COLOR getColorCode(){
            return colorCode;
        }

        URL_STATUS(int _code, String _message, String _status, PrettyPrintConst.COLOR _color) {
            statusCode = _code;
            httpMessage = _message;
            result = _status;
            colorCode = _color;
        }

        /**
         * Method to get a URL_STATUS by its status code
         * @param _statusCode
         * @return
         */
        public static URL_STATUS getURLStatusByCode(int _statusCode){
            for(URL_STATUS urlStatus : URL_STATUS.values()) {
                if(urlStatus.getStatusCode() == _statusCode) {
                    return urlStatus;
                }
            }
            return NOT_FOUND;
        }
    }

    public enum OS {
        WINDOWS("windows.exe"),
        LINUX("linux"),
        MACOSX("mac");

        private String osName;

        OS(String _os){
            osName = _os;
        }

        public String getOsName(){
            return osName;
        }
    }

    public static OS getOsByName(String _os){
        for(OS os : OS.values()){
            if(os.osName.equals(_os)){
                return os;
            }
        }
        return null;
    }

}
