package net.michnovka.batteryapinotify.model.configuration;

/**
 * Created by kishon on 18,November,2021
 */
public class Below {
    private Boolean isEnabled;
    private Integer level;

    public Below() {
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Integer getLevel() {
        return level;
    }
    public String getLevelString(){
        return String.valueOf(level);
    }
    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Below{" +
                "isEnabled=" + isEnabled +
                ", level=" + level +
                '}';
    }
}
