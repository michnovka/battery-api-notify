package net.michnovka.batteryapinotify.model.configuration;

/**
 * Created by kishon on 18,November,2021
 */
public class Configuration {
    private String url;
    private Above above;
    private Below below;
    private Integer interval;

    public Configuration() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Above getAbove() {
        return above;
    }

    public void setAbove(Above above) {
        this.above = above;
    }

    public Below getBelow() {
        return below;
    }

    public void setBelow(Below below) {
        this.below = below;
    }

    public Integer getInterval() {
        return interval;
    }

    public String getIntervalString() {
        return String.valueOf(interval);
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }


    @Override
    public String toString() {
        return "Configuration{" +
                "url='" + url + '\'' +
                ", above=" + above +
                ", below=" + below +
                ", interval=" + interval +
                '}';
    }
}
