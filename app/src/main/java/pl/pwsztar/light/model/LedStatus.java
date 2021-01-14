package pl.pwsztar.light.model;


import java.io.Serializable;

public class LedStatus implements Serializable {
    public static String CommandLedOff = "Off";
    public static String CommandLedOn = "On";

    private boolean ledStatus;

    public LedStatus() {
        this.ledStatus = false;
    }

    public boolean getLedStatus() {
        return ledStatus;
    }

    public void setLedStatus(boolean ledStatus) {
        this.ledStatus = ledStatus;
    }
}
