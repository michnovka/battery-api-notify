package net.michnovka.batteryapinotify.model.infromation;

/**
 * Created by kishon on 18,November,2021
 */
public class BatteryInformation {
    private String status;
    private String source;
    private Integer level;
    private String health;
    private Integer temp;
    private Integer voltage;

    public BatteryInformation() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public Integer getTemp() {
        return temp;
    }

    public void setTemp(Integer temp) {
        this.temp = temp;
    }

    public Integer getVoltage() {
        return voltage;
    }

    public void setVoltage(Integer voltage) {
        this.voltage = voltage;
    }

    @Override
    public String toString() {
        return "BatteryInformation{" +
                "status='" + status + '\'' +
                ", source='" + source + '\'' +
                ", level=" + level +
                ", health='" + health + '\'' +
                ", temp=" + temp +
                ", voltage=" + voltage +
                '}';
    }
}
