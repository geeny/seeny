package io.geeny.seeny.Model.Elements.SubElements;

import io.geeny.seeny.Util.Enums;

import java.util.ArrayList;
import java.util.List;

/**
 * io.geeny.seeny.Model for the element which is used by the linkcheck to know if a link has been checked already
 *
 * @author Marco Bierbach
 * @version 1.0
 */
public class PageToCheck {

    private String url;
    private List<String> parents = new ArrayList<>();
    private boolean checked = false;
    private boolean checkedRecursive;
    private Enums.URL_STATUS result;

    public PageToCheck(String _url, String _parent){
        url = _url;
        parents.add(_parent);
    }


    public String getUrl() {
        return url;
    }

    public List<String> getParents(){
        return parents;
    }

    public void addParent(String _parent){
        parents.add(_parent);
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isCheckedRecursive(){
        return checkedRecursive;
    }

    public void setCheckedRecursive(boolean _checkedRecursive){
        checkedRecursive = _checkedRecursive;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Enums.URL_STATUS getResult() {
        return result;
    }

    public void setResult(Enums.URL_STATUS result) {
        this.result = result;
    }
}
