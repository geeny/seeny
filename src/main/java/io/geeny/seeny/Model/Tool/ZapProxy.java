package io.geeny.seeny.Model.Tool;

/**
 * Model for the ZapProxy configuration
 *
 * @Author Marco Bierbach
 * @Version 1.0
 */
public class ZapProxy {

    private boolean enabled;
    private String url;
    private int port;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
