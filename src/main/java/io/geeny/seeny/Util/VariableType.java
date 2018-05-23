package io.geeny.seeny.Util;

/**
 * Class representing the variable types which can be used in the scripts system
 *
 * @Author Marco Bierbach
 */
public class VariableType {

    public enum ACTION_TYPE {

        CLICK("click"),
        WRITE("write"),
        DELETE("delete"),
        CONDITION("condition"),
        CLI("cli"),
        NAVIGATE("navigate"),
        LINK_CHECK("linkCheck"),
        LIST("list"),
        EXTRACT("extract");

        private String name;

        ACTION_TYPE(String _name){
            name = _name;
        }

        public String getName(){
            return name;
        }
    }

    public enum SELECTOR_TYPE {

        CSS("css"),
        ID("id"),
        COMP("comp"),
        XPATH("xpath");

        private String name;

        SELECTOR_TYPE(String _name) {
            name = _name;
        }

        public String getName() {
            return name;
        }
    }

    public enum TASK {

        ENABLED("enabled"),
        DISPLAYED("displayed"),
        EMPTY("empty"),
        SELECTED("selected"),
        COMPARE("compare"),
        FIND("find");


        private String name;

        TASK(String _name) {
            name = _name;
        }

        public String getName() {
            return name;
        }
    }

    public enum NAVIGATE {

        BACK("back"),
        FORWARD("forward"),
        REFRESH("refresh"),
        GOTO("goto"),
        CLOSE_TAB("closeTab"),
        SWITCH_TAB("switchTab");

        private String name;

        NAVIGATE(String _name) {
            name = _name;
        }

        public String getName() {
            return name;
        }
    }
}
