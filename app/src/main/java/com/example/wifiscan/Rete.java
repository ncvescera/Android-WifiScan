package com.example.wifiscan;

public class Rete {
    private String SSID;
    private String dettagli;
    private String level;

    public Rete(String SSID, String dettagli, String level) {
        this.SSID = SSID;
        this.dettagli = dettagli;
        this.level = level;
    }

    public String getSSID() {
        return SSID;
    }

    public String getDettagli() {
        return dettagli;
    }

    public String getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return this.SSID + " | " + this.dettagli + " | " + this.level;
    }
}
