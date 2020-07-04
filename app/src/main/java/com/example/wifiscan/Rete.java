package com.example.wifiscan;

public class Rete {
    private String SSID;
    private String dettagli;
    private String level;
    private String password;

    public Rete(String SSID, String dettagli, String level) {
        this.SSID = SSID;
        this.dettagli = dettagli;
        this.level = level;
        this.password = "";
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }
    @Override
    public String toString() {
        return this.SSID + " | " + this.dettagli + " | " + this.level;
    }
}
